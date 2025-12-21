package utils;

import model.GameState;
import java.util.ArrayList;
import java.util.List;


public class SudokuVerifier {
    
    
    public GameState verify(int[][] board) {
        if (board == null || board.length == 0) {
            return GameState.INVALID;
        }
        
        boolean hasZeros = false;
        List<String> invalidPositions = new ArrayList<>();
        
        int size = board.length;
        int boxSize = (int) Math.sqrt(size);
        
        
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
        
        
        if (!invalidPositions.isEmpty()) {
            return GameState.INVALID;
        }
        
        
        if (hasZeros) {
            return GameState.INCOMPLETE;
        }
        
        
        return GameState.VALID;
    }
    
    
    public List<String> getInvalidPositions(int[][] board) {
        List<String> invalidPositions = new ArrayList<>();
        if (board == null || board.length == 0) {
            return invalidPositions;
        }
        
        int size = board.length;
        int boxSize = (int) Math.sqrt(size);
        
        
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

