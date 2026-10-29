package com.epam.files;

import com.epam.files.sharing.FileShareDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

class FileShareTest {

    private static final FileShareDao dao = new FileShareDao();

    @BeforeAll
    static void setup() throws Exception {
        dao.initDatabase();
    }

    @Test
    void testSaveAndRetrieveFile() throws Exception {
        var content = "This is a test large binary payload simulation.".repeat(100);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        dao.saveFile("report.txt", new ByteArrayInputStream(bytes), bytes.length, Instant.now().plus(1, ChronoUnit.HOURS));

        var output = new ByteArrayOutputStream();
        boolean found = dao.retrieveFile(1L, output);

        Assertions.assertTrue(found);
        Assertions.assertEquals(content, output.toString(StandardCharsets.UTF_8));
    }
}
