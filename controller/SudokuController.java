import model.SudokuModel;
import services.SudokuService;
import veiw.SudokuView;

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
        // Unhighlight previous selection
        if (model.hasSelection()) {
            view.unhighlightCell(model.getSelectedRow(), model.getSelectedCol());
        }

        // Use service to validate and select cell
        if (service.selectCell(model, row, col)) {
            view.highlightCell(row, col);
        }
    }

    private void handleNumberPlacement(int number) {
        // Make the move through service
        SudokuService.MoveResult result = service.makeMove(model, number);

        // Handle result
        if (!result.isMoveMade()) {
            if (result.getErrorMessage() != null) {
                view.showMessage(result.getErrorMessage(), "Invalid Move");
            }
            return;
        }

        // Update view based on service result
        int row = result.getRow();
        int col = result.getCol();
        view.updateCell(row, col, number, false, result.isCorrect());

        if (result.isCorrect()) {
            // Cell is now correct, unhighlight it
            view.unhighlightCell(row, col);
        }

        // Update error count display
        view.updateErrorCount(model.getErrorCount());
    }
}

