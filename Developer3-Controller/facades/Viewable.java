package facades;

import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import java.io.IOException;

/**
 * Viewable is the interface of the controller representing the actions 
 * that are exposed to the viewer
 * MVC Pattern: Controller Facade Interface
 * <<controller facade>>
 */
public interface Viewable {
    /**
     * Gets the catalog of available games
     * @return Catalog with current and allModesExist flags
     */
    Catalog getCatalog();
    
    /**
     * Returns a random game with the specified difficulty
     * Note: the Game class is the representation of the sudoku game in the controller
     * @param level The difficulty level
     * @return A Game instance
     * @throws NotFoundException if no game of that difficulty is found
     */
    Game getGame(DifficultyEnum level) throws NotFoundException;
    
    /**
     * Gets a sourceSolution and generates three levels of difficulty
     * @param sourceGame The solved game to use as source
     * @throws SolutionInvalidException if the source solution is invalid or incomplete
     */
    void driveGames(Game sourceGame) throws SolutionInvalidException;
    
    /**
     * Given a game, if invalid returns invalid and locates the invalid duplicates
     * if valid and complete, return a value
     * if valid and incomplete, returns another value
     * The exact representation as a string is done as you best see fit
     * Example for return values:
     * Game Valid -> "valid"
     * Game incomplete -> "incomplete"
     * Game Invalid -> "invalid 1,2 3,3 6,7"
     * @param game The game to verify
     * @return Verification result as string
     */
    String verifyGame(Game game);
    
    /**
     * Returns the correct combination for the missing numbers
     * Hint: So, there are many ways you can approach this, one way is
     * to have a way to map an index in the combination array to its location in the board
     * one other way to to try to encode the location and the answer all in just one int
     * @param game The game to solve
     * @return Array containing solution values for empty cells
     * @throws InvalidGameException if game cannot be solved
     */
    int[] solveGame(Game game) throws InvalidGameException;
    
    /**
     * Logs the user action
     * @param userAction The action to log
     * @throws IOException if logging fails
     */
    void logUserAction(String userAction) throws IOException;
}

