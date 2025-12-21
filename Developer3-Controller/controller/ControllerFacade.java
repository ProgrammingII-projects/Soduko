package controller;

import facades.Viewable;
import facades.Controllable;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import view.UserAction;
import java.io.IOException;

/**
 * ControllerFacade: Adapter/Facade between Viewable and Controllable interfaces
 * MVC Pattern: Controller Facade - implements Viewable, delegates to ControllerA and ControllerB
 * Facade Pattern: Provides simplified interface to complex subsystems
 * Adapter Pattern: Adapts Viewable interface to Controllable interface
 * Uses Controllable interface to update view (ControllerA and ControllerB can update view)
 * Encapsulation: Encapsulates coordination between controllers
 */
public class ControllerFacade implements Viewable {
    private final ControllerA controllerA; // Game management
    private final ControllerB controllerB; // Verification and solving
    private Controllable view; // View reference for updates
    
    public ControllerFacade() {
        this.controllerA = new ControllerA();
        this.controllerB = new ControllerB();
    }
    
    /**
     * Set the view reference
     * Observer Pattern: Allows controllers to update view
     */
    public void setView(Controllable view) {
        this.view = view;
        this.controllerA.setView(view);
        this.controllerB.setView(view);
    }
    
    /**
     * Get ControllerA instance (for direct access if needed)
     */
    public ControllerA getControllerA() {
        return controllerA;
    }
    
    /**
     * Get ControllerB instance (for direct access if needed)
     */
    public ControllerB getControllerB() {
        return controllerB;
    }
    
    // Viewable interface implementation - delegates to ControllerA and ControllerB
    
    @Override
    public Catalog getCatalog() {
        return controllerA.getCatalog();
    }
    
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        return controllerA.getGame(level);
    }
    
    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        controllerA.driveGames(sourceGame);
    }
    
    @Override
    public String verifyGame(Game game) {
        return controllerB.verifyGame(game);
    }
    
    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        return controllerB.solveGame(game);
    }
    
    @Override
    public void logUserAction(String userAction) throws IOException {
        controllerA.logUserAction(userAction);
    }
    
    // Additional methods for Controllable interface adaptation
    
    /**
     * Adapt getCatalog() to boolean array format
     */
    public boolean[] getCatalogAsBooleanArray() {
        Catalog catalog = getCatalog();
        return new boolean[]{catalog.hasCurrent(), catalog.hasAllModesExist()};
    }
    
    /**
     * Adapt getGame() to int[][] format
     */
    public int[][] getGameAsIntArray(char level) throws NotFoundException {
        DifficultyEnum difficulty = DifficultyEnum.fromChar(level);
        Game game = getGame(difficulty);
        return game.getBoard();
    }
    
    /**
     * Adapt driveGames() to accept file path
     */
    public void driveGamesFromPath(String sourcePath) throws SolutionInvalidException {
        try {
            // Load game from file
            Game sourceGame = loadGameFromFile(sourcePath);
            driveGames(sourceGame);
        } catch (IOException e) {
            throw new SolutionInvalidException("Error loading source file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Adapt verifyGame() to return boolean[][]
     */
    public boolean[][] verifyGameAsBooleanArray(int[][] game) {
        Game gameObj = new Game(game);
        String result = verifyGame(gameObj);
        
        int size = game.length;
        boolean[][] cellValidity = new boolean[size][size];
        
        // Initialize all to true (assuming valid)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellValidity[i][j] = true;
            }
        }
        
        if (result.startsWith("invalid")) {
            // Parse invalid positions
            String[] parts = result.split(" ");
            for (int i = 1; i < parts.length; i++) {
                String[] coords = parts[i].split(",");
                if (coords.length == 2) {
                    int row = Integer.parseInt(coords[0]);
                    int col = Integer.parseInt(coords[1]);
                    if (row >= 0 && row < size && col >= 0 && col < size) {
                        cellValidity[row][col] = false;
                    }
                }
            }
        }
        
        return cellValidity;
    }
    
    /**
     * Adapt solveGame() to return int[][]
     */
    public int[][] solveGameAsIntArray(int[][] game) throws InvalidGameException {
        Game gameObj = new Game(game);
        int[] solution = solveGame(gameObj);
        
        // Find empty cell positions
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int row = 0; row < game.length; row++) {
            for (int col = 0; col < game[row].length; col++) {
                if (game[row][col] == 0) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }
        
        // Return [x, y, solution] for each missing cell
        int[][] result = new int[emptyCells.size()][3];
        for (int i = 0; i < emptyCells.size(); i++) {
            result[i][0] = emptyCells.get(i)[0]; // x
            result[i][1] = emptyCells.get(i)[1]; // y
            result[i][2] = solution[i];          // solution
        }
        
        return result;
    }
    
    /**
     * Adapt logUserAction() to accept UserAction
     */
    public void logUserActionFromView(UserAction userAction) throws IOException {
        logUserAction(userAction.toLogString());
    }
    
    /**
     * Load game from file path
     */
    private Game loadGameFromFile(String filePath) throws IOException {
        java.util.List<int[]> rows = new java.util.ArrayList<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int[] row = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Integer.parseInt(values[i].trim());
                }
                rows.add(row);
            }
        }
        
        int[][] board = rows.toArray(new int[rows.size()][]);
        return new Game(board);
    }
}

