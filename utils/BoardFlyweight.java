package utils;


public class BoardFlyweight {
    private final int[][] originalBoard;
    private final int[][] emptyCellPositions; 
    private final int size;
    
    
    public BoardFlyweight(int[][] board, int[][] emptyCellPositions) {
        this.originalBoard = board;
        this.emptyCellPositions = emptyCellPositions;
        this.size = board.length;
    }
    
    
    public boolean verifyPermutation(int[] permutation) {
        if (permutation.length != emptyCellPositions.length) {
            return false;
        }
        
        
        
        return verifyWithValues(permutation);
    }
    
    
    private boolean verifyWithValues(int[] values) {
        int boxSize = (int) Math.sqrt(size);
        
        
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
    
    
    private int getValueAt(int row, int col, int[] permutation) {
        
        for (int i = 0; i < emptyCellPositions.length; i++) {
            if (emptyCellPositions[i][0] == row && emptyCellPositions[i][1] == col) {
                return permutation[i];
            }
        }
        
        return originalBoard[row][col];
    }
    
    
    public int[][] getEmptyCellPositions() {
        return emptyCellPositions;
    }
    
    
    public int getSize() {
        return size;
    }
}

