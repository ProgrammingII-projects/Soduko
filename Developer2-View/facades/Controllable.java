package facades;

import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import view.UserAction;
import java.io.IOException;

/**
 * Controllable is the interface of the viewer representing the actions 
 * that GUI components signal according to user actions on screen
 * MVC Pattern: View Facade Interface
 * <<view facade>>
 */
public interface Controllable {
    /**
     * Gets the catalog of available games
     * @return boolean array [hasUnfinished, hasEasyMediumHard]
     */
    boolean[] getCatalog();
    
    /**
     * Gets a game by difficulty level
     * @param level Character representing difficulty ('e'=easy, 'm'=medium, 'h'=hard)
     * @return The game board as 2D int array
     * @throws NotFoundException if game not found
     */
    int[][] getGame(char level) throws NotFoundException;
    
    /**
     * Generates games from a source solution file path
     * @param sourcePath Path to the solved Sudoku file
     * @throws SolutionInvalidException if source solution is invalid
     */
    void driveGames(String sourcePath) throws SolutionInvalidException;
    
    /**
     * Verifies a game board
     * @param game The game board as 2D int array
     * @return Boolean array indicating if each cell is correct or invalid
     */
    boolean[][] verifyGame(int[][] game);
    
    /**
     * Solves a game board
     * Contains the cell x, y and solution for each missing cell
     * @param game The game board as 2D int array
     * @return 2D array with [x, y, solution] for each missing cell
     * @throws InvalidGameException if game cannot be solved
     */
    int[][] solveGame(int[][] game) throws InvalidGameException;
    
    /**
     * Logs the user action
     * @param userAction The user action to log
     * @throws IOException if logging fails
     */
    void logUserAction(UserAction userAction) throws IOException;
}

