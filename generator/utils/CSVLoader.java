package utils;

import model.SudokuBoard;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVLoader {
    public static SudokuBoard loadFromCsv(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<Integer> numbers = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.trim().split("[,\\s]+");
            for (String p : parts) {
                if (p.trim().isEmpty()) continue;
                numbers.add(Integer.parseInt(p.trim()));
            }
        }
        if (numbers.size() != 81) {
            throw new IllegalArgumentException("CSV must contain exactly 81 numbers (9x9). Found: " + numbers.size());
        }
        int[][] grid = new int[9][9];
        for (int i = 0; i < 81; i++) {
            grid[i / 9][i % 9] = numbers.get(i);
        }
        return new SudokuBoard(grid);
    }
}
