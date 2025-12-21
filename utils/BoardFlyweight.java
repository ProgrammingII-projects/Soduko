package utils;

/**
 * Flyweight Pattern: Lightweight board representation for verification
 * Avoids copying the entire board for each permutation check
 * Stores only the positions and values of empty cells
 * Encapsulation: Encapsulates board verification logic
 */
public class BoardFlyweight {
    private final int[][] originalBoard;
    private final int[][] emptyCellPositions; // [i][0]=row, [i][1]=col for each empty cell
    private final int size;
    
    /**
     * Create flyweight board
     * @param board Original board
     * @param emptyCellPositions Array of [row, col] pairs for empty cells
     */
    public BoardFlyweight(int[][] board, int[][] emptyCellPositions) {
        this.originalBoard = board;
        this.emptyCellPositions = emptyCellPositions;
        this.size = board.length;
    }
    
    /**
     * Verify if a permutation creates a valid board
     * Does not modify the original board
     * @param permutation Values to fill in empty cells
     * @return true if valid, false otherwise
     */
    public boolean verifyPermutation(int[] permutation) {
        if (permutation.length != emptyCellPositions.length) {
            return false;
        }
        
        // Create a temporary board for verification without copying the entire board
        // We'll verify by checking if adding these values creates conflicts
        return verifyWithValues(permutation);
    }
    
    /**
     * Verify board with given values in empty cells
     */
    private boolean verifyWithValues(int[] values) {
        int boxSize = (int) Math.sqrt(size);
        
        // Check rows
        for (int row = 0; row < size; row++) {
            boolean[] seen = new boolean[size + 1];
            for (int col = 0; col < size; col++) {
                int value = getValueAt(row, col, values);
                if (value != 0) {
                    if (seen[value]) {
                        return false;
                    }
                    seen[value] = true;
                }
            }
        }
        
        // Check columns
        for (int col = 0; col < size; col++) {
            boolean[] seen = new boolean[size + 1];
            for (int row = 0; row < size; row++) {
                int value = getValueAt(row, col, values);
                if (value != 0) {
                    if (seen[value]) {
                        return false;
                    }
                    seen[value] = true;
                }
            }
        }
        
        // Check boxes
        for (int boxRow = 0; boxRow < boxSize; boxRow++) {
            for (int boxCol = 0; boxCol < boxSize; boxCol++) {
                boolean[] seen = new boolean[size + 1];
                int startRow = boxRow * boxSize;
                int startCol = boxCol * boxSize;
                
                for (int row = startRow; row < startRow + boxSize; row++) {
                    for (int col = startCol; col < startCol + boxSize; col++) {
                        int value = getValueAt(row, col, values);
                        if (value != 0) {
                            if (seen[value]) {
                                return false;
                            }
                            seen[value] = true;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Get value at position, using permutation for empty cells
     */
    private int getValueAt(int row, int col, int[] permutation) {
        // Check if this is an empty cell position
        for (int i = 0; i < emptyCellPositions.length; i++) {
            if (emptyCellPositions[i][0] == row && emptyCellPositions[i][1] == col) {
                return permutation[i];
            }
        }
        // Not an empty cell, return original value
        return originalBoard[row][col];
    }
    
    /**
     * Get empty cell positions
     */
    public int[][] getEmptyCellPositions() {
        return emptyCellPositions;
    }
    
    /**
     * Get original board size
     */
    public int getSize() {
        return size;
    }
}

