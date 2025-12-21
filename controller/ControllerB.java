package controller;

import facades.Controllable;
import model.Game;
import model.GameState;
import exceptions.InvalidGameException;
import utils.SudokuVerifier;
import utils.SudokuSolver;
import java.util.List;

/**
 * ControllerB: Handles verification and solving
 * MVC Pattern: Controller layer component
 * Uses Controllable interface to update view
 * Encapsulation: Encapsulates verification and solving logic
 */
public class ControllerB {
    private final SudokuVerifier verifier;
    private final SudokuSolver solver;
    private Controllable view; // Reference to view for updates
    
    public ControllerB() {
        this.verifier = new SudokuVerifier();
        this.solver = new SudokuSolver();
    }
    
    /**
     * Set the view reference for updates
     * Observer Pattern: Controller can notify view
     */
    public void setView(Controllable view) {
        this.view = view;
    }
    
    /**
     * Verify a game and return result as string
     */
    public String verifyGame(Game game) {
        GameState state = verifier.verify(game.getBoard());
        
        switch (state) {
            case VALID:
                return "valid";
            case INCOMPLETE:
                return "incomplete";
            case INVALID:
                List<String> invalidPositions = verifier.getInvalidPositions(game.getBoard());
                StringBuilder sb = new StringBuilder("invalid");
                for (String pos : invalidPositions) {
                    sb.append(" ").append(pos);
                }
                return sb.toString();
            default:
                return "unknown";
        }
    }
    
    /**
     * Solve a game
     */
    public int[] solveGame(Game game) throws InvalidGameException {
        return solver.solve(game);
    }
}

