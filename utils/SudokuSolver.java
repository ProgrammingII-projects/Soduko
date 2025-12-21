package utils;

import model.Game;
import exceptions.InvalidGameException;

/**
 * Sudoku Solver using Iterator and Flyweight patterns
 * Solves boards with exactly 5 empty cells using permutation verification
 * Encapsulation: Encapsulates solving logic
 */
public class SudokuSolver {
    private static final int REQUIRED_EMPTY_CELLS = 5;
    
    /**
     * Solve a game with exactly 5 empty cells
     * @param game The game to solve
     * @return Array of solution values for empty cells
     * @throws InvalidGameException if game cannot be solved or doesn't have exactly 5 empty cells
     */
    public int[] solve(Game game) throws InvalidGameException {
        int[][] board = game.getBoard();
        
        // Find empty cell positions
        int[][] emptyCells = findEmptyCells(board);
        
        if (emptyCells.length != REQUIRED_EMPTY_CELLS) {
            throw new InvalidGameException(
                "Solver only works with exactly " + REQUIRED_EMPTY_CELLS + " empty cells. Found: " + emptyCells.length);
        }
        
        // Create flyweight board (Iterator Pattern + Flyweight Pattern)
        BoardFlyweight flyweight = new BoardFlyweight(board, emptyCells);
        
        // Create iterator for permutations (Iterator Pattern)
        PermutationIterator iterator = new PermutationIterator(REQUIRED_EMPTY_CELLS, 9);
        
        // Try each permutation until we find a valid solution
        while (iterator.hasNext()) {
            int[] permutation = iterator.next();
            
            // Verify this permutation using flyweight (no board copying)
            if (flyweight.verifyPermutation(permutation)) {
                return permutation; // Found solution!
            }
        }
        
        // No solution found
        throw new InvalidGameException("No valid solution found for this puzzle");
    }
    
    /**
     * Find all empty cell positions
     * @param board The board
     * @return Array of [row, col] pairs for empty cells
     */
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

