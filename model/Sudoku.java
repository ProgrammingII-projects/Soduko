package model;

import java.util.Random;

public class Sudoku {
    private int[][] board;
    private int size;
    private int boxSize;
    private Random rand = new Random();

    public Sudoku(int size) {
        this.size = size;
        double sqrt = Math.sqrt(size);
        if (sqrt - Math.floor(sqrt) != 0) {
            throw new IllegalArgumentException("Size must be a perfect square (e.g., 4, 9, 16)");
        }
        this.boxSize = (int) sqrt;
        board = new int[size][size];
        generateBoard();
    }

    // Generate a complete Sudoku board
    private void generateBoard() {
        fillDiagonal();
        fillRemaining(0, boxSize);
    }

    // Fill the diagonal boxes to help with the generation
    private void fillDiagonal() {
        for (int i = 0; i < size; i += boxSize) {
            fillBox(i, i);
        }
    }

    // Fill a box starting at (rowStart, colStart)
    private void fillBox(int rowStart, int colStart) {
        for (int i = 0; i < boxSize; i++) {
            for (int j = 0; j < boxSize; j++) {
                int num;
                do {
                    num = randomGenerator(size);
                } while (!isUnusedInBox(rowStart, colStart, num));
                board[rowStart + i][colStart + j] = num;
            }
        }
    }

    // Get random number between 1 and max
    private int randomGenerator(int max) {
        return rand.nextInt(max) + 1;
    }

    // Check if num is not used in the current box
    private boolean isUnusedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < boxSize; i++) {
            for (int j = 0; j < boxSize; j++) {
                if (board[rowStart + i][colStart + j] == num)
                    return false;
            }
        }
        return true;
    }

    // Fill the remaining positions on the board
    private boolean fillRemaining(int i, int j) {
        if (j >= size && i < size - 1) {
            i++;
            j = 0;
        }
        if (i >= size && j >= size)
            return true;

        if (i < boxSize) {
            if (j < boxSize)
                j = boxSize;
        } else if (i < size - boxSize) {
            if (j == (i / boxSize) * boxSize)
                j += boxSize;
        } else {
            if (j == size - boxSize) {
                i++;
                j = 0;
                if (i >= size)
                    return true;
            }
        }

        for (int num = 1; num <= size; num++) {
            if (isSafe(i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(i, j + 1))
                    return true;
                board[i][j] = 0;
            }
        }
        return false;
    }

    // Check if it's safe to place num at (i, j)
    public boolean isSafe(int i, int j, int num) {
        return isUnusedInRow(i, num) &&
               isUnusedInCol(j, num) &&
               isUnusedInBox(i - i % boxSize, j - j % boxSize, num);
    }

    private boolean isUnusedInRow(int i, int num) {
        for (int j = 0; j < size; j++)
            if (board[i][j] == num)
                return false;
        return true;
    }

    private boolean isUnusedInCol(int j, int num) {
        for (int i = 0; i < size; i++)
            if (board[i][j] == num)
                return false;
        return true;
    }

    // Print the Sudoku board
    public void printBoard() {
        for (int r = 0; r < size; r++) {
            for (int d = 0; d < size; d++) {
                System.out.print(board[r][d]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

     // Get a copy of the current board
     public int[][] getBoard() {
        int[][] copy = new int[size][size];
        for (int r = 0; r < size; r++) {
            copy[r] = board[r].clone();
        }
        return copy;
    }

    // Generate a Sudoku puzzle by deleting cells so it has up to maxSolutionsAllowed solutions
    public void generatePuzzle(int numToRemove, int maxSolutionsAllowed) {
        int count = numToRemove;
        while (count > 0) {
            int i = rand.nextInt(size);
            int j = rand.nextInt(size);

            if (board[i][j] == 0)
                continue;

            int backup = board[i][j];
            board[i][j] = 0;

            // Make a copy of the board to test solutions
            int[][] boardCopy = new int[size][size];
            for (int r = 0; r < size; r++)
                boardCopy[r] = board[r].clone();

            int[] solutionCount = new int[] {0};
            countSolutions(boardCopy, solutionCount, maxSolutionsAllowed);

            if (solutionCount[0] > maxSolutionsAllowed) {
                board[i][j] = backup; // revert if too many solutions
            } else {
                count--;
            }
        }
    }

    // Counts the number of solutions to the current Sudoku grid.
    // Stops counting if the number exceeds max.
    private void countSolutions(int[][] grid, int[] count, int max) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == 0) {
                    for (int num = 1; num <= size; num++) {
                        if (isSafeForGrid(grid, i, j, num)) {
                            grid[i][j] = num;
                            countSolutions(grid, count, max);
                            grid[i][j] = 0;
                            if (count[0] > max) return;
                        }
                    }
                    return; // only continue from first empty cell
                }
            }
        }
        count[0]++;
    }

    // Similar to isSafe but works on arbitrary grid
    private boolean isSafeForGrid(int[][] grid, int i, int j, int num) {
        // Check row
        for (int col = 0; col < size; col++)
            if (grid[i][col] == num) return false;
        // Check column
        for (int row = 0; row < size; row++)
            if (grid[row][j] == num) return false;
        // Check box
        int boxStartRow = i - i % boxSize;
        int boxStartCol = j - j % boxSize;
        for (int row = 0; row < boxSize; row++)
            for (int col = 0; col < boxSize; col++)
                if (grid[boxStartRow + row][boxStartCol + col] == num) return false;
        return true;
    }

}
