import javax.swing.SwingUtilities;
import controller.ControllerFacade;
import view.SudokuGameGUI;

/**
 * Main entry point for Sudoku Game Application
 * MVC Pattern: Initializes View and Controller layers
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create ControllerFacade (implements Viewable interface)
            ControllerFacade controllerFacade = new ControllerFacade();
            
            // Create GUI (View layer) - implements Controllable interface
            // View uses Viewable to invoke use cases
            SudokuGameGUI gui = new SudokuGameGUI(controllerFacade);
            
            // Set view reference in facade so controllers can update view
            controllerFacade.setView(gui);
            
            gui.setVisible(true);
        });
    }
}