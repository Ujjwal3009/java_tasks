package com.epam.files.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class DiskAnalyzer {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: DiskAnalyzer <path> <function_number(1-4)> <output_file>");
            return;
        }

        var dirPath = Paths.get(args[0]);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            System.err.println("Error: Directory does not exist or is invalid: " + args[0]);
            return;
        }

        int functionNumber;
        try {
            functionNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid function number: " + args[1]);
            return;
        }

        var outputFile = Paths.get(args[2]);

        try {
            String result = executeFunction(dirPath, functionNumber);
            writeToFile(outputFile, result);
            System.out.println("Result written successfully to " + outputFile);
        } catch (Exception ex) {
            System.err.println("Execution failed: " + ex.getMessage());
        }
    }

    public static String executeFunction(Path rootDir, int functionNumber) throws IOException {
        return switch (functionNumber) {
            case 1 -> findFileWithMaxLetterS(rootDir);
            case 2 -> findTop5LargestFiles(rootDir);
            case 3 -> calculateAverageFileSize(rootDir);
            case 4 -> countFilesAndFoldersByAlphabet(rootDir);
            default -> throw new IllegalArgumentException("Unknown function number: " + functionNumber + " (Expected 1-4)");
        };
    }

    public static String findFileWithMaxLetterS(Path rootDir) throws IOException {
        try (Stream<Path> stream = Files.walk(rootDir)) {
            var file = stream.filter(Files::isRegularFile)
                    .max(Comparator.comparingInt(p -> countLetterS(p.getFileName().toString())))
                    .orElse(null);

            if (file == null) {
                return "No files found in " + rootDir;
            }
            int count = countLetterS(file.getFileName().toString());
            return "File with max 's': " + file.toAbsolutePath() + " (count: " + count + ")";
        }
    }

    public static String findTop5LargestFiles(Path rootDir) throws IOException {
        try (Stream<Path> stream = Files.walk(rootDir)) {
            var top5 = stream.filter(Files::isRegularFile)
                    .map(p -> {
                        try {
                            return new FileSize(p, Files.size(p));
                        } catch (IOException e) {
                            return new FileSize(p, 0L);
                        }
                    })
                    .sorted(Comparator.comparingLong(FileSize::size).reversed())
                    .limit(5)
                    .toList();

            var sb = new StringBuilder("Top 5 largest files:\n");
            for (var fs : top5) {
                sb.append(fs.path().toAbsolutePath()).append(" - ").append(fs.size()).append(" bytes\n");
            }
            return sb.toString();
        }
    }

    public static String calculateAverageFileSize(Path rootDir) throws IOException {
        try (Stream<Path> stream = Files.walk(rootDir)) {
            var stats = stream.filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .summaryStatistics();

            return "Average file size: " + (stats.getCount() == 0 ? 0 : stats.getAverage()) + " bytes (total files: " + stats.getCount() + ")";
        }
    }

    public static String countFilesAndFoldersByAlphabet(Path rootDir) throws IOException {
        Map<Character, int[]> counts = new TreeMap<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            counts.put(c, new int[]{0, 0}); // [0] files, [1] folders
        }

        try (Stream<Path> stream = Files.walk(rootDir)) {
            stream.filter(p -> !p.equals(rootDir)).forEach(p -> {
                var name = p.getFileName().toString();
                if (!name.isEmpty()) {
                    var firstChar = Character.toUpperCase(name.charAt(0));
                    if (counts.containsKey(firstChar)) {
                        if (Files.isDirectory(p)) {
                            counts.get(firstChar)[1]++;
                        } else {
                            counts.get(firstChar)[0]++;
                        }
                    }
                }
            });
        }

        var sb = new StringBuilder("Alphabetical breakdown:\n");
        counts.forEach((ch, arr) -> {
            if (arr[0] > 0 || arr[1] > 0) {
                sb.append("Letter ").append(ch).append(": ").append(arr[0]).append(" files, ").append(arr[1]).append(" folders\n");
            }
        });
        return sb.toString();
    }

    public static void writeToFile(Path target, String content) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(target)) {
            writer.write(content);
        }
    }

    private static int countLetterS(String s) {
        int count = 0;
        for (char c : s.toLowerCase().toCharArray()) {
            if (c == 's') count++;
        }
        return count;
    }

    private record FileSize(Path path, long size) {}
}
