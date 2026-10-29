package com.epam.files.mover;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FastFileMover {

    public enum Strategy {
        FILE_STREAM,
        BUFFERED_STREAM_100KB,
        FILE_CHANNEL,
        NIO2_FILES
    }

    public static void move(Path source, Path target, Strategy strategy) throws IOException {
        switch (strategy) {
            case FILE_STREAM -> moveUsingFileStreams(source, target);
            case BUFFERED_STREAM_100KB -> moveUsingBufferedStreams(source, target);
            case FILE_CHANNEL -> moveUsingFileChannel(source, target);
            case NIO2_FILES -> moveUsingNio(source, target);
        }
    }

    public static void moveUsingFileStreams(Path source, Path target) throws IOException {
        try (var in = new FileInputStream(source.toFile());
             var out = new FileOutputStream(target.toFile())) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        }
        Files.delete(source);
    }

    public static void moveUsingBufferedStreams(Path source, Path target) throws IOException {
        byte[] buffer = new byte[100 * 1024]; // 100 KB
        try (var in = new FileInputStream(source.toFile());
             var out = new FileOutputStream(target.toFile())) {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        Files.delete(source);
    }

    public static void moveUsingFileChannel(Path source, Path target) throws IOException {
        try (var inChannel = new FileInputStream(source.toFile()).getChannel();
             var outChannel = new FileOutputStream(target.toFile()).getChannel()) {
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, size - position, outChannel);
            }
        }
        Files.delete(source);
    }

    public static void moveUsingNio(Path source, Path target) throws IOException {
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: FastFileMover <source_file> <target_file> <strategy (1-4)>");
            return;
        }

        var source = Path.of(args[0]);
        var target = Path.of(args[1]);
        int opt = Integer.parseInt(args[2]);

        Strategy strategy = switch (opt) {
            case 1 -> Strategy.FILE_STREAM;
            case 2 -> Strategy.BUFFERED_STREAM_100KB;
            case 3 -> Strategy.FILE_CHANNEL;
            case 4 -> Strategy.NIO2_FILES;
            default -> throw new IllegalArgumentException("Unknown option: " + opt);
        };

        try {
            long start = System.currentTimeMillis();
            move(source, target, strategy);
            System.out.println("Moved using " + strategy + " in " + (System.currentTimeMillis() - start) + " ms");
        } catch (IOException e) {
            System.err.println("Failed to move file: " + e.getMessage());
        }
    }
}
