package com.epam.files.sharing;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FileSharingProcedures {

    public static void saveFile(Connection conn, String fileName, InputStream dataStream, long fileSize, Timestamp expiresAt) throws SQLException {
        String sql = "INSERT INTO files (file_name, file_size, data, expires_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fileName);
            ps.setLong(2, fileSize);
            ps.setBinaryStream(3, dataStream, fileSize);
            ps.setTimestamp(4, expiresAt);
            ps.executeUpdate();
        }
    }

    public static ResultSet getFile(Connection conn, long fileId) throws SQLException {
        String sql = "SELECT id, file_name, file_size, data, expires_at FROM files WHERE id = ? AND (expires_at IS NULL OR expires_at > CURRENT_TIMESTAMP)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, fileId);
        return ps.executeQuery();
    }
}
