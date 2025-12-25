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


public class ControllerFacade implements Viewable {
    private final ControllerA controllerA; 
    private final ControllerB controllerB; 
    private Controllable view; 
    
    public ControllerFacade() {
        this.controllerA = new ControllerA();
        this.controllerB = new ControllerB();
    }
    
    
    public void setView(Controllable view) {
        this.view = view;
        this.controllerA.setView(view);
        this.controllerB.setView(view);
    }
    
    
    public ControllerA getControllerA() {
        return controllerA;
    }
    
    
    public ControllerB getControllerB() {
        return controllerB;
    }
    
    
    
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
    
    
    
    
    public boolean[] getCatalogAsBooleanArray() {
        Catalog catalog = getCatalog();
        return new boolean[]{catalog.hasCurrent(), catalog.hasAllModesExist()};
    }
    
    
    public int[][] getGameAsIntArray(char level) throws NotFoundException {
        DifficultyEnum difficulty = DifficultyEnum.fromChar(level);
        Game game = getGame(difficulty);
        return game.getBoard();
    }
    
    
    public void driveGamesFromPath(String sourcePath) throws SolutionInvalidException {
        try {
            
            Game sourceGame = loadGameFromFile(sourcePath);
            driveGames(sourceGame);
        } catch (IOException e) {
            throw new SolutionInvalidException("Error loading source file: " + e.getMessage(), e);
        }
    }
    
    
    public boolean[][] verifyGameAsBooleanArray(int[][] game) {
        Game gameObj = new Game(game);
        String result = verifyGame(gameObj);
        
        int size = game.length;
        boolean[][] cellValidity = new boolean[size][size];
        
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellValidity[i][j] = true;
            }
        }
        
        if (result.startsWith("invalid")) {
            
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
    
    
    public int[][] solveGameAsIntArray(int[][] game) throws InvalidGameException {
        Game gameObj = new Game(game);
        int[] solution = solveGame(gameObj);
        
        
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int row = 0; row < game.length; row++) {
            for (int col = 0; col < game[row].length; col++) {
                if (game[row][col] == 0) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }
        
        
        int[][] result = new int[emptyCells.size()][3];
        for (int i = 0; i < emptyCells.size(); i++) {
            result[i][0] = emptyCells.get(i)[0]; 
            result[i][1] = emptyCells.get(i)[1]; 
            result[i][2] = solution[i];          
        }
        
        return result;
    }
    
    
    public void logUserActionFromView(UserAction userAction) throws IOException {
        logUserAction(userAction.toLogString());
    }
    
    
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

