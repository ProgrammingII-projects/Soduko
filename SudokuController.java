public class SudokuController {
    private SudokuModel model;
    private SudokuView view;
    private SudokuService service;

    public SudokuController(SudokuModel model, SudokuView view, SudokuService service) {
        this.model = model;
        this.view = view;
        this.service = service;
        initializeController();
    }

    private void initializeController() {
        // Initialize puzzle display
        view.initializePuzzle(model);

        // Set up cell click listeners
        for (int r = 0; r < model.getSize(); r++) {
            for (int c = 0; c < model.getSize(); c++) {
                final int row = r;
                final int col = c;
                view.setCellListener(row, col, () -> handleCellSelection(row, col));
            }
        }

        // Set up number button listeners
        for (int i = 1; i <= 9; i++) {
            final int number = i;
            view.setNumberButtonListener(number, () -> handleNumberPlacement(number));
        }
    }

    private void handleCellSelection(int row, int col) {
        if (model.isFixed(row, col) || model.isCorrect(row, col)) {
            return;
        }

        // Unhighlight previous selection
        if (model.hasSelection()) {
            view.unhighlightCell(model.getSelectedRow(), model.getSelectedCol());
        }

        // Set new selection
        model.setSelectedCell(row, col);
        view.highlightCell(row, col);
    }

    private void handleNumberPlacement(int number) {
        if (!model.hasSelection()) {
            view.showMessage("Please select a cell first!", "No Cell Selected");
            return;
        }

        int row = model.getSelectedRow();
        int col = model.getSelectedCol();

        if (model.isFixed(row, col)) {
            return;
        }

        // Make the move through service
        service.makeMove(model, row, col, number);

        // Update view
        boolean isCorrect = model.isCorrect(row, col);
        view.updateCell(row, col, number, false, isCorrect);

        if (isCorrect) {
            // Cell is now correct, unhighlight it
            view.unhighlightCell(row, col);
        }

        // Update error count display
        view.updateErrorCount(model.getErrorCount());
    }
}

