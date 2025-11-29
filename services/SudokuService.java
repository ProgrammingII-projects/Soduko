import model.SudokuModel;
import utils.SudokuGenerator;

package services;
public class SudokuService {
    private static final int SIZE = 9;

    public SudokuModel generateNewPuzzle(int cellsToRemove) {
        SudokuGenerator generator = new SudokuGenerator(SIZE);
        int[][] solution = generator.getBoard();
        generator.generatePuzzle(cellsToRemove, 1);
        int[][] puzzle = generator.getBoard();

        SudokuModel model = new SudokuModel();
        model.setSolution(solution);
        model.setPuzzle(puzzle);

        // Mark fixed cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                model.setFixed(r, c, puzzle[r][c] != 0);
            }
        }

        return model;
    }

    public boolean canSelectCell(SudokuModel model, int row, int col) {
        return !model.isFixed(row, col) && !model.isCorrect(row, col);
    }

    public boolean selectCell(SudokuModel model, int row, int col) {
        if (!canSelectCell(model, row, col)) {
            return false;
        }
        model.setSelectedCell(row, col);
        return true;
    }

    public boolean validateMove(SudokuModel model, int row, int col, int number) {
        int[][] solution = model.getSolution();
        return number == solution[row][col];
    }

    public MoveResult makeMove(SudokuModel model, int number) {
        if (!model.hasSelection()) {
            return new MoveResult(false, false, "Please select a cell first!", -1, -1);
        }

        int row = model.getSelectedRow();
        int col = model.getSelectedCol();

        if (model.isFixed(row, col)) {
            return new MoveResult(false, false, "Cannot modify fixed cell", row, col);
        }

        if (model.isCorrect(row, col)) {
            return new MoveResult(false, false, "Cell is already correct", row, col);
        }

        model.setCellValue(row, col, number);
        boolean isCorrect = validateMove(model, row, col, number);

        if (isCorrect) {
            model.setCorrect(row, col, true);
            model.clearSelection();
            return new MoveResult(true, true, null, row, col);
        } else {
            model.incrementErrorCount();
            return new MoveResult(true, false, null, row, col);
        }
    }

    public static class MoveResult {
        private final boolean moveMade;
        private final boolean isCorrect;
        private final String errorMessage;
        private final int row;
        private final int col;

        public MoveResult(boolean moveMade, boolean isCorrect, String errorMessage, int row, int col) {
            this.moveMade = moveMade;
            this.isCorrect = isCorrect;
            this.errorMessage = errorMessage;
            this.row = row;
            this.col = col;
        }

        public boolean isMoveMade() {
            return moveMade;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}

