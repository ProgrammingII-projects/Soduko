public class SudokuService {
    private static final int SIZE = 9;

    public SudokuModel generateNewPuzzle(int cellsToRemove) {
        SudokuGenerator generator = new SudokuGenerator(SIZE);
        int[][] solution = generator.getBoard();
        generator.generatePuzzle(cellsToRemove, 1);
        int[][] puzzle = generator.getBoard();

        SudokuModel model = new SudokuModel();
        model.setSolution(solution);
        model.setPuzzle(puzzle);

        // Mark fixed cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                model.setFixed(r, c, puzzle[r][c] != 0);
            }
        }

        return model;
    }

    public boolean validateMove(SudokuModel model, int row, int col, int number) {
        int[][] solution = model.getSolution();
        return number == solution[row][col];
    }

    public void makeMove(SudokuModel model, int row, int col, int number) {
        if (model.isFixed(row, col) || model.isCorrect(row, col)) {
            return;
        }

        model.setCellValue(row, col, number);
        boolean isCorrect = validateMove(model, row, col, number);

        if (isCorrect) {
            model.setCorrect(row, col, true);
            model.clearSelection();
        } else {
            model.incrementErrorCount();
        }
    }
}

