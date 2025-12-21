package utils;

import model.GameState;
import java.util.ArrayList;
import java.util.List;

/**
 * Verifier for Sudoku boards that returns GameState
 * Strategy Pattern: Different verification strategies can be implemented
 * Encapsulation: Encapsulates verification logic
 */
public class SudokuVerifier {
    
    /**
     * Verifies a Sudoku board and returns its state
     * @param board The board to verify
     * @return GameState (VALID, INVALID, or INCOMPLETE)
     */
    public GameState verify(int[][] board) {
        if (board == null || board.length == 0) {
            return GameState.INVALID;
        }
        
        boolean hasZeros = false;
        List<String> invalidPositions = new ArrayList<>();
        
        int size = board.length;
        int boxSize = (int) Math.sqrt(size);
        
        // Check for zeros and validate rows
        for (int row = 0; row < size; row++) {
            boolean[] seen = new boolean[size + 1];
            for (int col = 0; col < size; col++) {
                int value = board[row][col];
                if (value == 0) {
                    hasZeros = true;
                } else {
                    if (seen[value]) {
                        invalidPositions.add(row + "," + col);
                    }
                    seen[value] = true;
                }
            }
        }
        
        // Validate columns
        for (int col = 0; col < size; col++) {
            boolean[] seen = new boolean[size + 1];
            for (int row = 0; row < size; row++) {
                int value = board[row][col];
                if (value != 0) {
                    if (seen[value]) {
                        invalidPositions.add(row + "," + col);
                    }
                    seen[value] = true;
                }
            }
        }
        
        // Validate boxes
        for (int boxRow = 0; boxRow < boxSize; boxRow++) {
            for (int boxCol = 0; boxCol < boxSize; boxCol++) {
                boolean[] seen = new boolean[size + 1];
                int startRow = boxRow * boxSize;
                int startCol = boxCol * boxSize;
                
                for (int row = startRow; row < startRow + boxSize; row++) {
                    for (int col = startCol; col < startCol + boxSize; col++) {
                        int value = board[row][col];
                        if (value != 0) {
                            if (seen[value]) {
                                invalidPositions.add(row + "," + col);
                            }
                            seen[value] = true;
                        }
                    }
                }
            }
        }
        
        // If there are conflicts, it's INVALID
        if (!invalidPositions.isEmpty()) {
            return GameState.INVALID;
        }
        
        // If no conflicts but has zeros, it's INCOMPLETE
        if (hasZeros) {
            return GameState.INCOMPLETE;
        }
        
        // No zeros and no conflicts means VALID
        return GameState.VALID;
    }
    
    /**
     * Verifies and returns invalid positions
     * @param board The board to verify
     * @return List of invalid positions as "row,col" strings
     */
    public List<String> getInvalidPositions(int[][] board) {
        List<String> invalidPositions = new ArrayList<>();
        if (board == null || board.length == 0) {
            return invalidPositions;
        }
        
        int size = board.length;
        int boxSize = (int) Math.sqrt(size);
        
        // Check rows
        for (int row = 0; row < size; row++) {
            boolean[] seen = new boolean[size + 1];
            for (int col = 0; col < size; col++) {
                int value = board[row][col];
                if (value != 0 && seen[value]) {
                    invalidPositions.add(row + "," + col);
                }
                if (value != 0) seen[value] = true;
            }
        }
        
        // Check columns
        for (int col = 0; col < size; col++) {
            boolean[] seen = new boolean[size + 1];
            for (int row = 0; row < size; row++) {
                int value = board[row][col];
                if (value != 0 && seen[value]) {
                    invalidPositions.add(row + "," + col);
                }
                if (value != 0) seen[value] = true;
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
                        int value = board[row][col];
                        if (value != 0 && seen[value]) {
                            invalidPositions.add(row + "," + col);
                        }
                        if (value != 0) seen[value] = true;
                    }
                }
            }
        }
        
        return invalidPositions;
    }
}

