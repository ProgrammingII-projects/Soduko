package model;

import java.util.*;

public class ValidationResult {
    private final Map<Integer, Map<Integer, List<Integer>>> rowDuplicates = new LinkedHashMap<>();
    private final Map<Integer, Map<Integer, List<Integer>>> colDuplicates = new LinkedHashMap<>();
    private final Map<Integer, Map<Integer, List<Integer>>> boxDuplicates = new LinkedHashMap<>();

    public synchronized void addRowDuplicate(int rowIndex, int value, List<Integer> positions) {
        rowDuplicates.computeIfAbsent(rowIndex, k -> new LinkedHashMap<>()).put(value, new ArrayList<>(positions));
    }

    public synchronized void addColDuplicate(int colIndex, int value, List<Integer> positions) {
        colDuplicates.computeIfAbsent(colIndex, k -> new LinkedHashMap<>()).put(value, new ArrayList<>(positions));
    }

    public synchronized void addBoxDuplicate(int boxIndex, int value, List<Integer> positions) {
        boxDuplicates.computeIfAbsent(boxIndex, k -> new LinkedHashMap<>()).put(value, new ArrayList<>(positions));
    }

    public Map<Integer, Map<Integer, List<Integer>>> getRowDuplicates() { return rowDuplicates; }
    public Map<Integer, Map<Integer, List<Integer>>> getColDuplicates() { return colDuplicates; }
    public Map<Integer, Map<Integer, List<Integer>>> getBoxDuplicates() { return boxDuplicates; }

    public boolean isValid() {
        return rowDuplicates.isEmpty() && colDuplicates.isEmpty() && boxDuplicates.isEmpty();
    }
}
