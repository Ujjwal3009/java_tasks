package com.epam.files;

import com.epam.files.analyzer.DiskAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class DiskAnalyzerTest {

    @Test
    void testDiskAnalyzerFunctions(@TempDir Path tempDir) throws IOException {
        // Create files: 'mississippi.txt', 'simple.txt', 'folderA/sample.txt'
        var file1 = tempDir.resolve("mississippi.txt");
        Files.writeString(file1, "1234567890"); // 10 bytes, 4 's'

        var file2 = tempDir.resolve("simple.txt");
        Files.writeString(file2, "12345"); // 5 bytes, 1 's'

        var subDir = Files.createDirectory(tempDir.resolve("SubDir"));
        var file3 = subDir.resolve("test.txt");
        Files.writeString(file3, "123456789012345"); // 15 bytes

        // Function 1: max 's'
        var res1 = DiskAnalyzer.executeFunction(tempDir, 1);
        Assertions.assertTrue(res1.contains("mississippi.txt"));

        // Function 2: top 5 largest
        var res2 = DiskAnalyzer.executeFunction(tempDir, 2);
        Assertions.assertTrue(res2.contains("test.txt"));

        // Function 3: average size
        var res3 = DiskAnalyzer.executeFunction(tempDir, 3);
        Assertions.assertTrue(res3.contains("Average file size: 10.0 bytes"));

        // Function 4: alphabetical count
        var res4 = DiskAnalyzer.executeFunction(tempDir, 4);
        Assertions.assertTrue(res4.contains("Letter M: 1 files"));
        Assertions.assertTrue(res4.contains("Letter S: 1 files, 1 folders"));
    }
}
