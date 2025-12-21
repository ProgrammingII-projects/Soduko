package Factory;

import model.SudokuBoard;
import validators.*;

public class ValidatorFactory {
    public static SudokuValidator create(int mode, SudokuBoard board) {
        switch (mode) {
            case 0: return new Mode0Validator(board);
            case 3: return new Mode3Validator(board);
            case 27: return new Mode27Validator(board);
            default: throw new IllegalArgumentException("Mode must be 0, 3 or 27");
        }
    }
}