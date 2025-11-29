public class SudokuModel {
    private static final int SIZE = 9;
    private int[][] solution;
    private int[][] puzzle;
    private boolean[][] isFixed;
    private boolean[][] isCorrect;
    private int errorCount;
    private int selectedRow;
    private int selectedCol;

    public SudokuModel() {
        this.solution = new int[SIZE][SIZE];
        this.puzzle = new int[SIZE][SIZE];
        this.isFixed = new boolean[SIZE][SIZE];
        this.isCorrect = new boolean[SIZE][SIZE];
        this.errorCount = 0;
        this.selectedRow = -1;
        this.selectedCol = -1;
    }

    public int getSize() {
        return SIZE;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void setSolution(int[][] solution) {
        this.solution = solution;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public boolean isFixed(int row, int col) {
        return isFixed[row][col];
    }

    public void setFixed(int row, int col, boolean fixed) {
        this.isFixed[row][col] = fixed;
    }

    public boolean isCorrect(int row, int col) {
        return isCorrect[row][col];
    }

    public void setCorrect(int row, int col, boolean correct) {
        this.isCorrect[row][col] = correct;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public void incrementErrorCount() {
        this.errorCount++;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public void setSelectedCell(int row, int col) {
        this.selectedRow = row;
        this.selectedCol = col;
    }

    public void clearSelection() {
        this.selectedRow = -1;
        this.selectedCol = -1;
    }

    public boolean hasSelection() {
        return selectedRow != -1 && selectedCol != -1;
    }

    public int getCellValue(int row, int col) {
        return puzzle[row][col];
    }

    public void setCellValue(int row, int col, int value) {
        this.puzzle[row][col] = value;
    }
}

