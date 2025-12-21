import javax.swing.SwingUtilities;
import controller.ControllerFacade;
import view.SudokuGameGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ControllerFacade controllerFacade = new ControllerFacade();

            SudokuGameGUI gui = new SudokuGameGUI(controllerFacade);

            controllerFacade.setView(gui);

            gui.setVisible(true);
        });
    }
}
