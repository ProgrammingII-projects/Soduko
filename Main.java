import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create MVC components
            SudokuService service = new SudokuService();
            SudokuModel model = service.generateNewPuzzle(20);
            SudokuView view = new SudokuView();
            SudokuController controller = new SudokuController(model, view, service);
            
            // Show the view
            view.setVisible(true);
        });
    }
}

