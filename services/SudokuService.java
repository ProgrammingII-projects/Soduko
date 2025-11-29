package services;

import model.SudokuModel;
import utils.SudokuGenerator;

public class SudokuService {
    private static final int SIZE = 9;

    public SudokuModel generateNewPuzzle(int cellsToRemove) {
        SudokuGenerator generator = new SudokuGenerator(SIZE);
        generator.generatePuzzle(cellsToRemove, 1);
        int[][] puzzle = generator.getBoard();

        SudokuModel model = new SudokuModel();
        model.setPuzzle(puzzle);

        // Mark fixed cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                model.setFixed(r, c, puzzle[r][c] != 0);
            }
        }

        return model;
    }

    // Check if a number is NOT repeated in the given row
    public boolean checkRow(SudokuModel model, int row, int number) {
        int[][] puzzle = model.getPuzzle();
        int count = 0;
        for (int c = 0; c < SIZE; c++) {
            if (puzzle[row][c] == number) {
                count++;
            }
        }
        // Valid if the number occurs at most once
        return count <= 1;
    }

    // Check if a number is NOT repeated in the given column
    public boolean checkColumn(SudokuModel model, int col, int number) {
        int[][] puzzle = model.getPuzzle();
        int count = 0;
        for (int r = 0; r < SIZE; r++) {
            if (puzzle[r][col] == number) {
                count++;
            }
        }
        // Valid if the number occurs at most once
        return count <= 1;
    }

    // Check if a number is NOT repeated in the 3x3 box containing (row, col)
    public boolean checkBox(SudokuModel model, int row, int col, int number) {
        int[][] puzzle = model.getPuzzle();
        int boxStartRow = (row / 3) * 3;
        int boxStartCol = (col / 3) * 3;
        int count = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (puzzle[boxStartRow + r][boxStartCol + c] == number) {
                    count++;
                }
            }
        }
        // Valid if the number occurs at most once in the box
        return count <= 1;
    }

    public boolean canSelectCell(SudokuModel model, int row, int col) {
        return !model.isFixed(row, col);
    }

    public boolean selectCell(SudokuModel model, int row, int col) {
        if (!canSelectCell(model, row, col)) {
            return false;
        }
        model.setSelectedCell(row, col);
        return true;
    }

    public boolean isValidMove(SudokuModel model, int row, int col, int number) {
        // Check if the move satisfies all Sudoku constraints
        return checkRow(model, row, number) && 
               checkColumn(model, col, number) && 
               checkBox(model, row, col, number);
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

        // Set the new value
        model.setCellValue(row, col, number);
        
        // Check if the move is valid (satisfies row, column, and box constraints)
        boolean isValid = isValidMove(model, row, col, number);
        
        // Update correctness status based on constraints
        model.setCorrect(row, col, isValid);

        // Increment error count only when constraints are violated
        if (!isValid) {
            model.incrementErrorCount();
        }

        return new MoveResult(true, isValid, null, row, col);
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

