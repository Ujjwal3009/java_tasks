package com.epam.files;

import com.epam.files.mover.FastFileMover;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FastFileMoverTest {

    @Test
    void testAllMoveStrategies(@TempDir Path tempDir) throws IOException {
        for (FastFileMover.Strategy strategy : FastFileMover.Strategy.values()) {
            var src = tempDir.resolve("src_" + strategy.name() + ".txt");
            var dest = tempDir.resolve("dest_" + strategy.name() + ".txt");

            Files.writeString(src, "Test payload for " + strategy.name());

            FastFileMover.move(src, dest, strategy);

            Assertions.assertFalse(Files.exists(src), "Source file should be removed after move");
            Assertions.assertTrue(Files.exists(dest), "Destination file should exist");
            Assertions.assertEquals("Test payload for " + strategy.name(), Files.readString(dest));
        }
    }
}
