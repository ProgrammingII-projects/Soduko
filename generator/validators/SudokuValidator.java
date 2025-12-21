package validators;

import model.SudokuBoard;
import model.ValidationResult;

public abstract class SudokuValidator {
    protected final SudokuBoard board;
    protected final ValidationResult result = new ValidationResult();

    protected SudokuValidator(SudokuBoard board) {
        this.board = board;
    }

    public abstract ValidationResult validate();

    protected int[][] boardCopy() {
        return board.getCopy();
    }
}
