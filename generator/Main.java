import java.io.IOException;
import java.util.Map;
import factory.ValidatorFactory;
import model.SudokuBoard;
import model.ValidationResult;
import utils.CSVLoader;
import validators.SudokuValidator;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar SudokuVerifier.jar <solution.csv> <mode>");
            System.out.println("mode: 0 (sequential), 3 (rows/cols/boxes threads), 27 (one per row/col/box)");
            return;
        }

        String csv = args[0];
        int mode;
        try {
            mode = Integer.parseInt(args[1]);
            if (mode != 0 && mode != 3 && mode != 27) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            System.out.println("Mode must be 0, 3 or 27");
            return;
        }

        try {
            SudokuBoard board = CSVLoader.loadFromCsv(csv);
            SudokuValidator validator = ValidatorFactory.create(mode, board);
            ValidationResult res = validator.validate();

            if (res.isValid()) {
                System.out.println("VALID");
            } else {
                System.out.println("INVALID");
                for (Map.Entry<Integer, Map<Integer, java.util.List<Integer>>> rowEntry : res.getRowDuplicates().entrySet()) {
                    int rowIdx = rowEntry.getKey();
                    Map<Integer, java.util.List<Integer>> map = rowEntry.getValue();
                    for (Map.Entry<Integer, java.util.List<Integer>> e : map.entrySet()) {
                        System.out.printf("ROW %d, #%d, %s%n", rowIdx, e.getKey(), e.getValue());
                    }
                }
                System.out.println("------------------------------------------");
                for (Map.Entry<Integer, Map<Integer, java.util.List<Integer>>> colEntry : res.getColDuplicates().entrySet()) {
                    int colIdx = colEntry.getKey();
                    Map<Integer, java.util.List<Integer>> map = colEntry.getValue();
                    for (Map.Entry<Integer, java.util.List<Integer>> e : map.entrySet()) {
                        System.out.printf("COL %d, #%d, %s%n", colIdx, e.getKey(), e.getValue());
                    }
                }
                System.out.println("------------------------------------------");
                for (Map.Entry<Integer, Map<Integer, java.util.List<Integer>>> boxEntry : res.getBoxDuplicates().entrySet()) {
                    int boxIdx = boxEntry.getKey();
                    Map<Integer, java.util.List<Integer>> map = boxEntry.getValue();
                    for (Map.Entry<Integer, java.util.List<Integer>> e : map.entrySet()) {
                        System.out.printf("BOX %d, #%d, %s%n", boxIdx, e.getKey(), e.getValue());
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Error reading CSV: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Input error: " + ex.getMessage());
        }
    }
}
