package model;

/**
 * Represents a Sudoku game with its board state
 * Used only in controller layer
 * IMPORTANT: Uses reference to board, not a copy
 */
public class Game {
    private int[][] board;
    
    public Game(int[][] board) {
        // IMPORTANT: DON'T COPY THE BOARD BY VALUE
        // USE REFERENCES
        this.board = board;
    }
    
    /**
     * Gets the board reference
     * @return Reference to the board array
     */
    public int[][] getBoard() {
        return board;
    }
    
    /**
     * Sets the board reference
     * @param board The board to reference
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    /**
     * Gets the size of the board
     * @return Board size (typically 9)
     */
    public int getSize() {
        return board != null ? board.length : 0;
    }
    
    /**
     * Checks if the board is complete (no zeros)
     * @return true if complete, false otherwise
     */
    public boolean isComplete() {
        if (board == null) return false;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Counts the number of empty cells (zeros)
     * @return Number of empty cells
     */
    public int countEmptyCells() {
        if (board == null) return 0;
        int count = 0;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    count++;
                }
            }
        }
        return count;
    }
}

