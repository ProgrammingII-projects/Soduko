package facades;

import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import view.UserAction;
import java.io.IOException;


public interface Controllable {
    
    boolean[] getCatalog();
    
    
    int[][] getGame(char level) throws NotFoundException;
    
    
    void driveGames(String sourcePath) throws SolutionInvalidException;
    
    
    boolean[][] verifyGame(int[][] game);
    
    
    int[][] solveGame(int[][] game) throws InvalidGameException;
    
    
    void logUserAction(UserAction userAction) throws IOException;
}

