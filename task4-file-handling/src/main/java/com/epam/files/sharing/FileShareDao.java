package com.epam.files.sharing;

import com.epam.files.db.DatabaseConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class FileShareDao {

    public void initDatabase() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS files (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    file_name VARCHAR(255) NOT NULL,
                    file_size BIGINT NOT NULL,
                    data BLOB NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP
                );
            """);

            stmt.execute("DROP ALIAS IF EXISTS SAVE_FILE;");
            stmt.execute("CREATE ALIAS SAVE_FILE FOR \"com.epam.files.sharing.FileSharingProcedures.saveFile\";");

            stmt.execute("DROP ALIAS IF EXISTS GET_FILE;");
            stmt.execute("CREATE ALIAS GET_FILE FOR \"com.epam.files.sharing.FileSharingProcedures.getFile\";");
        }
    }

    public void saveFile(String fileName, InputStream stream, long size, Instant expiresAt) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cs = conn.prepareCall("CALL SAVE_FILE(?, ?, ?, ?)")) {
            cs.setString(1, fileName);
            cs.setBinaryStream(2, stream, size);
            cs.setLong(3, size);
            cs.setTimestamp(4, expiresAt != null ? Timestamp.from(expiresAt) : null);
            cs.execute();
        }
    }

    public boolean retrieveFile(long fileId, OutputStream destination) throws SQLException, IOException {
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cs = conn.prepareCall("CALL GET_FILE(?)")) {
            cs.setLong(1, fileId);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    try (InputStream blobStream = rs.getBinaryStream("data")) {
                        byte[] buffer = new byte[64 * 1024]; // 64 KB buffer for streaming up to 200MB+
                        int read;
                        while ((read = blobStream.read(buffer)) != -1) {
                            destination.write(buffer, 0, read);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public long getFileCount() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM files")) {
            return rs.next() ? rs.getLong(1) : 0L;
        }
    }
}
