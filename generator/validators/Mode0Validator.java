package validators;

import model.SudokuBoard;
import model.ValidationResult;

import java.util.*;

public class Mode0Validator extends SudokuValidator {

    public Mode0Validator(SudokuBoard board) {
        super(board);
    }

    @Override
    public ValidationResult validate() {
        int[][] g = boardCopy();

        for (int r = 0; r < 9; r++) {
            Map<Integer, List<Integer>> positions = new LinkedHashMap<>();
            for (int c = 0; c < 9; c++) {
                int v = g[r][c];
                positions.computeIfAbsent(v, k -> new ArrayList<>()).add(c + 1);
            }
            for (Map.Entry<Integer, List<Integer>> e : positions.entrySet()) {
                if (e.getValue().size() > 1) {
                    result.addRowDuplicate(r + 1, e.getKey(), e.getValue());
                }
            }
        }

        for (int c = 0; c < 9; c++) {
            Map<Integer, List<Integer>> positions = new LinkedHashMap<>();
            for (int r = 0; r < 9; r++) {
                int v = g[r][c];
                positions.computeIfAbsent(v, k -> new ArrayList<>()).add(r + 1);
            }
            for (Map.Entry<Integer, List<Integer>> e : positions.entrySet()) {
                if (e.getValue().size() > 1) {
                    result.addColDuplicate(c + 1, e.getKey(), e.getValue());
                }
            }
        }

        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                int boxIndex = br * 3 + bc + 1;
                Map<Integer, List<Integer>> positions = new LinkedHashMap<>();
                int baseRow = br * 3;
                int baseCol = bc * 3;
                int pos = 1;
                for (int r = baseRow; r < baseRow + 3; r++) {
                    for (int c = baseCol; c < baseCol + 3; c++) {
                        int v = g[r][c];
                        positions.computeIfAbsent(v, k -> new ArrayList<>()).add(pos);
                        pos++;
                    }
                }
                for (Map.Entry<Integer, List<Integer>> e : positions.entrySet()) {
                    if (e.getValue().size() > 1) {
                        result.addBoxDuplicate(boxIndex, e.getKey(), e.getValue());
                    }
                }
            }
        }

        return result;
    }
}
