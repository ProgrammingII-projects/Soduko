package controller;

import facades.Controllable;
import model.Game;
import model.GameState;
import exceptions.InvalidGameException;
import utils.SudokuVerifier;
import utils.SudokuSolver;
import java.util.List;


public class ControllerB {
    private final SudokuVerifier verifier;
    private final SudokuSolver solver;
    private Controllable view; 
    
    public ControllerB() {
        this.verifier = new SudokuVerifier();
        this.solver = new SudokuSolver();
    }
    
    
    public void setView(Controllable view) {
        this.view = view;
    }
    
    
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
    
    
    public int[] solveGame(Game game) throws InvalidGameException {
        return solver.solve(game);
    }
}

