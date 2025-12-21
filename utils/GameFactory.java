package utils;

import model.DifficultyEnum;
import model.Game;

/**
 * Factory Pattern: Creates games based on difficulty
 * Additional design pattern beyond required ones
 * Encapsulation: Encapsulates game creation logic
 */
public class GameFactory {
    
    /**
     * Create a game by removing cells from a source game
     * @param sourceGame The source solved game
     * @param difficulty The difficulty level
     * @param randomPairs RandomPairs instance for generating positions
     * @return A new Game instance with cells removed
     */
    public static Game createDifficultyGame(Game sourceGame, DifficultyEnum difficulty, RandomPairs randomPairs) {
        int[][] board = sourceGame.getBoard();
        int size = board.length;
        
        // Create a deep copy of the board
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, size);
        }
        
        // Generate distinct pairs and remove cells
        java.util.List<int[]> pairs = randomPairs.generateDistinctPairs(difficulty.getCellsToRemove());
        for (int[] pair : pairs) {
            int row = pair[0];
            int col = pair[1];
            newBoard[row][col] = 0; // Remove cell
        }
        
        return new Game(newBoard);
    }
}

