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


public class ControllerA {
    private final GameStorage storage;
    private final RandomPairs randomPairs;
    private final SudokuVerifier verifier;
    private Controllable controllerView; 

    public ControllerA() {
        this.storage = GameStorage.getInstance();
        this.randomPairs = new RandomPairs();
        this.verifier = new SudokuVerifier();
    }

    
    public void setView(Controllable view) {
        this.controllerView = view;
    }

    
    public Catalog getCatalog() {
        boolean hasCurrent = storage.hasCurrentGame();
        boolean hasAllModes = storage.hasAllDifficultyGames();
        return new Catalog(hasCurrent, hasAllModes);
    }

    
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        try {
            Game game = storage.loadRandomGame(level);
            
            storage.saveCurrentGame(game);
            return game;
        } catch (IOException e) {
            throw new NotFoundException("Error loading game: " + e.getMessage(), e);
        }
    }

    
    public Game loadCurrentGame() throws NotFoundException {
        try {
            return storage.loadCurrentGame();
        } catch (IOException e) {
            throw new NotFoundException("Error loading current game: " + e.getMessage(), e);
        }
    }

    
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        
        GameState state = verifier.verify(sourceGame.getBoard());

        if (state != GameState.VALID) {
            throw new SolutionInvalidException(
                    "Source solution is " + state + ". Must be VALID to generate games.");
        }

        
        try {
            
            Game easyGame = GameFactory.createDifficultyGame(sourceGame, DifficultyEnum.EASY, randomPairs);
            storage.saveGame(DifficultyEnum.EASY, easyGame);

            
            Game mediumGame = GameFactory.createDifficultyGame(sourceGame, DifficultyEnum.MEDIUM, randomPairs);
            storage.saveGame(DifficultyEnum.MEDIUM, mediumGame);

            
            Game hardGame = GameFactory.createDifficultyGame(sourceGame, DifficultyEnum.HARD, randomPairs);
            storage.saveGame(DifficultyEnum.HARD, hardGame);

        } catch (IOException e) {
            throw new SolutionInvalidException("Error saving games: " + e.getMessage(), e);
        }
    }

    
    public void saveCurrentGame(Game game) throws IOException {
        storage.saveCurrentGame(game);
    }

    
    public boolean checkAndDeleteIfComplete(Game game, DifficultyEnum difficulty) throws IOException {
        GameState state = verifier.verify(game.getBoard());

        if (state == GameState.VALID && game.isComplete()) {
            
            storage.deleteGame(difficulty, game);
            storage.deleteCurrentGame();
            return true;
        }

        return false;
    }

    
    public void logUserAction(String userAction) throws IOException {
        storage.appendToLog(userAction);
    }

    
    public UserAction undoLastMove() throws IOException, NotFoundException {
        List<String> logEntries = storage.readLogEntries();
        if (logEntries.isEmpty()) {
            throw new NotFoundException("No moves to undo");
        }

        
        String lastEntry = logEntries.get(logEntries.size() - 1);
        UserAction action = UserAction.fromLogString(lastEntry);

        
        storage.removeLastLogEntry();

        return action;
    }
}
