package utils;

import model.DifficultyEnum;
import model.Game;


public class GameFactory {
    
    
    public static Game createDifficultyGame(Game sourceGame, DifficultyEnum difficulty, RandomPairs randomPairs) {
        int[][] board = sourceGame.getBoard();
        int size = board.length;
        
        
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, size);
        }
        
        
        java.util.List<int[]> pairs = randomPairs.generateDistinctPairs(difficulty.getCellsToRemove());
        for (int[] pair : pairs) {
            int row = pair[0];
            int col = pair[1];
            newBoard[row][col] = 0; 
        }
        
        return new Game(newBoard);
    }
}

