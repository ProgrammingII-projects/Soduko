package model;

public class SudokuBoard {
    private final int[][] grid;

    public SudokuBoard(int[][] grid) {
        if (grid.length != 9) throw new IllegalArgumentException("Grid must be 9x9");
        this.grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            if (grid[i].length != 9) throw new IllegalArgumentException("Grid must be 9x9");
            System.arraycopy(grid[i], 0, this.grid[i], 0, 9);
        }
    }

    public int get(int r, int c) {
        return grid[r][c];
    }

    public int[][] getCopy() {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(grid[i], 0, copy[i], 0, 9);
        return copy;
    }
}
