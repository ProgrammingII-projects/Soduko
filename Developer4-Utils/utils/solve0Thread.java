package utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Sequential thread implementation for Sudoku validation (Mode 0)
 * Validates the entire board sequentially using a single thread
 * Template Method Pattern: Defines skeleton of validation algorithm
 * Encapsulation: Encapsulates thread-based validation logic
 */
public class solve0Thread extends Thread {
    private final int[][] board;
    private final AtomicBoolean isValid;

    public solve0Thread(int[][] board, AtomicBoolean isValid) {
        this.board = board;
        this.isValid = isValid;
    }

    @Override
    public void run() {
        validateBoard();
    }

    /**
     * Template Method: Defines the validation algorithm structure
     */
    public void validateBoard() {
        boolean boardValid = true;
        
        // Validate rows
        for (int row = 0; row < board.length; row++) {
            if (!validateRow(row)) {
                boardValid = false;
            }
        }
        
        // Validate columns
        for (int col = 0; col < board[0].length; col++) {
            if (!validateColumn(col)) {
                boardValid = false;
            }
        }
        
        // Validate boxes
        int boxSize = (int) Math.sqrt(board.length);
        for (int boxRow = 0; boxRow < boxSize; boxRow++) {
            for (int boxCol = 0; boxCol < boxSize; boxCol++) {
                if (!validateBox(boxRow, boxCol, boxSize)) {
                    boardValid = false;
                }
            }
        }
        
        isValid.set(boardValid);
    }

    /**
     * Validates a single row for duplicates
     */
    private boolean validateRow(int row) {
        boolean[] seen = new boolean[board.length + 1];
        for (int col = 0; col < board[row].length; col++) {
            int value = board[row][col];
            if (value != 0) {
                if (seen[value]) {
                    return false; // Duplicate found
                }
                seen[value] = true;
            }
        }
        return true;
    }

    /**
     * Validates a single column for duplicates
     */
    private boolean validateColumn(int col) {
        boolean[] seen = new boolean[board.length + 1];
        for (int row = 0; row < board.length; row++) {
            int value = board[row][col];
            if (value != 0) {
                if (seen[value]) {
                    return false; // Duplicate found
                }
                seen[value] = true;
            }
        }
        return true;
    }

    /**
     * Validates a single box for duplicates
     */
    private boolean validateBox(int boxRow, int boxCol, int boxSize) {
        boolean[] seen = new boolean[board.length + 1];
        int startRow = boxRow * boxSize;
        int startCol = boxCol * boxSize;
        
        for (int row = startRow; row < startRow + boxSize; row++) {
            for (int col = startCol; col < startCol + boxSize; col++) {
                int value = board[row][col];
                if (value != 0) {
                    if (seen[value]) {
                        return false; // Duplicate found
                    }
                    seen[value] = true;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a specific position is safe (no conflicts)
     */
    public boolean Check_Board(int row, int column, int num) {
        if (row < 0 || row >= board.length || column < 0 || column >= board[0].length) {
            return false;
        }
        // Check if placing num at (row, column) would create a conflict
        int originalValue = board[row][column];
        board[row][column] = num;
        boolean isValid = validateRow(row) && validateColumn(column) && 
                          validateBox(row / (int)Math.sqrt(board.length), 
                                     column / (int)Math.sqrt(board.length), 
                                     (int)Math.sqrt(board.length));
        board[row][column] = originalValue;
        return isValid;
    }
}

