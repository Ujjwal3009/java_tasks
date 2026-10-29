package com.epam.files;

import com.epam.files.db.DatabaseConfig;
import com.epam.files.orm.JabaORM;
import com.epam.files.orm.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

class JabaOrmTest {

    private final JabaORM orm = new JabaORM();

    @BeforeAll
    static void initTable() throws Exception {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100)
                );
            """);
        }
    }

    @Test
    void testSaveLoadUpdateDelete() throws Exception {
        var user = new UserEntity("Test User", "test@example.com");

        // Save
        orm.save(user);
        Assertions.assertNotNull(user.getId());
        long savedId = user.getId();

        // Load
        UserEntity loaded = orm.load(UserEntity.class, savedId);
        Assertions.assertNotNull(loaded);
        Assertions.assertEquals("Test User", loaded.getName());
        Assertions.assertEquals("test@example.com", loaded.getEmail());

        // Update
        loaded.setEmail("updated@example.com");
        orm.update(loaded);

        UserEntity reloaded = orm.load(UserEntity.class, savedId);
        Assertions.assertEquals("updated@example.com", reloaded.getEmail());

        // Delete
        orm.delete(reloaded);
        UserEntity deleted = orm.load(UserEntity.class, savedId);
        Assertions.assertNull(deleted);
    }
}
