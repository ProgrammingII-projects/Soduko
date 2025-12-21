package utils;

import model.Game;
import exceptions.InvalidGameException;


public class SudokuSolver {
    private static final int REQUIRED_EMPTY_CELLS = 5;
    
    
    public int[] solve(Game game) throws InvalidGameException {
        int[][] board = game.getBoard();
        
        
        int[][] emptyCells = findEmptyCells(board);
        
        if (emptyCells.length != REQUIRED_EMPTY_CELLS) {
            throw new InvalidGameException(
                "Solver only works with exactly " + REQUIRED_EMPTY_CELLS + " empty cells. Found: " + emptyCells.length);
        }
        
        
        BoardFlyweight flyweight = new BoardFlyweight(board, emptyCells);
        
        
        PermutationIterator iterator = new PermutationIterator(REQUIRED_EMPTY_CELLS, 9);
        
        
        while (iterator.hasNext()) {
            int[] permutation = iterator.next();
            
            
            if (flyweight.verifyPermutation(permutation)) {
                return permutation; 
            }
        }
        
        
        throw new InvalidGameException("No valid solution found for this puzzle");
    }
    
    
    private int[][] findEmptyCells(int[][] board) {
        java.util.List<int[]> emptyList = new java.util.ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    emptyList.add(new int[]{row, col});
                }
            }
        }
        return emptyList.toArray(new int[emptyList.size()][]);
    }
}

