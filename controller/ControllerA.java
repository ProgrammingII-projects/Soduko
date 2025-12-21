package controller;

import facades.Controllable;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import model.GameState;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import utils.GameStorage;
import utils.RandomPairs;
import utils.GameFactory;
import utils.SudokuVerifier;
import java.io.IOException;
import java.util.List;
import view.UserAction;

/**
 * ControllerA: Handles game management (catalog, loading, saving, generation)
 * MVC Pattern: Controller layer component
 * Uses Controllable interface to update view
 * Encapsulation: Encapsulates game management logic
 */
public class ControllerA {
    private final GameStorage storage;
    private final RandomPairs randomPairs;
    private final SudokuVerifier verifier;
    private Controllable controllerView; // Reference to view for updates

    public ControllerA() {
        this.storage = GameStorage.getInstance();
        this.randomPairs = new RandomPairs();
        this.verifier = new SudokuVerifier();
    }

    /**
     * Set the view reference for updates
     * Observer Pattern: Controller can notify view
     */
    public void setView(Controllable view) {
        this.controllerView = view;
    }

    /**
     * Get catalog of available games
     */
    public Catalog getCatalog() {
        boolean hasCurrent = storage.hasCurrentGame();
        boolean hasAllModes = storage.hasAllDifficultyGames();
        return new Catalog(hasCurrent, hasAllModes);
    }

    /**
     * Get a game by difficulty level
     */
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        try {
            Game game = storage.loadRandomGame(level);
            // Save as current game
            storage.saveCurrentGame(game);
            return game;
        } catch (IOException e) {
            throw new NotFoundException("Error loading game: " + e.getMessage(), e);
        }
    }

    /**
     * Load current game if it exists
     */
    public Game loadCurrentGame() throws NotFoundException {
        try {
            return storage.loadCurrentGame();
        } catch (IOException e) {
            throw new NotFoundException("Error loading current game: " + e.getMessage(), e);
        }
    }

    /**
     * Generate games from source solution
     */
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        // Verify the source solution first
        GameState state = verifier.verify(sourceGame.getBoard());

        if (state != GameState.VALID) {
            throw new SolutionInvalidException(
                    "Source solution is " + state + ". Must be VALID to generate games.");
        }

        // Generate three difficulty levels using Factory Pattern
        try {
            // Generate Easy (remove 10 cells)
            Game easyGame = GameFactory.createDifficultyGame(sourceGame, DifficultyEnum.EASY, randomPairs);
            storage.saveGame(DifficultyEnum.EASY, easyGame);

            // Generate Medium (remove 20 cells)
            Game mediumGame = GameFactory.createDifficultyGame(sourceGame, DifficultyEnum.MEDIUM, randomPairs);
            storage.saveGame(DifficultyEnum.MEDIUM, mediumGame);

            // Generate Hard (remove 25 cells)
            Game hardGame = GameFactory.createDifficultyGame(sourceGame, DifficultyEnum.HARD, randomPairs);
            storage.saveGame(DifficultyEnum.HARD, hardGame);

        } catch (IOException e) {
            throw new SolutionInvalidException("Error saving games: " + e.getMessage(), e);
        }
    }

    /**
     * Save current game state
     */
    public void saveCurrentGame(Game game) throws IOException {
        storage.saveCurrentGame(game);
    }

    /**
     * Check if game is complete and valid, then delete it
     */
    public boolean checkAndDeleteIfComplete(Game game, DifficultyEnum difficulty) throws IOException {
        GameState state = verifier.verify(game.getBoard());

        if (state == GameState.VALID && game.isComplete()) {
            // Game is complete and valid - delete it
            storage.deleteGame(difficulty, game);
            storage.deleteCurrentGame();
            return true;
        }

        return false;
    }

    /**
     * Log user action
     */
    public void logUserAction(String userAction) throws IOException {
        storage.appendToLog(userAction);
    }

    /**
     * Undo last move
     */
    public UserAction undoLastMove() throws IOException, NotFoundException {
        List<String> logEntries = storage.readLogEntries();
        if (logEntries.isEmpty()) {
            throw new NotFoundException("No moves to undo");
        }

        // Get last entry
        String lastEntry = logEntries.get(logEntries.size() - 1);
        UserAction action = UserAction.fromLogString(lastEntry);

        // Remove from log
        storage.removeLastLogEntry();

        return action;
    }
}
