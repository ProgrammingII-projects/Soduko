package facades;

import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import java.io.IOException;


public interface Viewable {
    
    Catalog getCatalog();
    
    
    Game getGame(DifficultyEnum level) throws NotFoundException;
    
    
    void driveGames(Game sourceGame) throws SolutionInvalidException;
    
    
    String verifyGame(Game game);
    
    
    int[] solveGame(Game game) throws InvalidGameException;
    
    
    void logUserAction(String userAction) throws IOException;
}

