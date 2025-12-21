package view;

import facades.Controllable;
import facades.Viewable;
import controller.ControllerFacade;
import model.Game;
import model.Catalog;
import model.DifficultyEnum;
import model.GameStateObserver;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Stack;


public class SudokuGameGUI extends JFrame implements Controllable, GameStateObserver {
    private static final int SIZE = 9;
    private JButton[][] cells = new JButton[SIZE][SIZE];
    private JButton[] numberButtons = new JButton[9];
    private JButton selectedCell = null;
    private int[][] currentBoard;
    private Game activeGame; 
    private char currentDifficulty;
    private Viewable controllerFacade; 

    private JLabel statusLabel;
    private JButton verifyButton;
    private JButton solveButton;
    private JButton undoButton;
    private boolean[][] isFixed = new boolean[SIZE][SIZE];
    private boolean[][] isCorrect = new boolean[SIZE][SIZE];
    private javax.swing.border.Border[][] originalBorders = new javax.swing.border.Border[SIZE][SIZE];

    
    private Stack<int[][]> boardHistory = new Stack<>();

    public SudokuGameGUI(Viewable controllerFacade) {
        this.controllerFacade = controllerFacade;
        initializeGUI();
        loadGame();
    }

    private void initializeGUI() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 900);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new FlowLayout());
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);

        
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBorder(new LineBorder(Color.BLACK, 3));
        add(gridPanel, BorderLayout.CENTER);

        
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JButton cell = new JButton();
                cell.setFont(new Font("Arial", Font.BOLD, 24));
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setFocusPainted(false);

                int topBorder = (r % 3 == 0) ? 3 : 1;
                int bottomBorder = (r % 3 == 2) ? 3 : 1;
                int leftBorder = (c % 3 == 0) ? 3 : 1;
                int rightBorder = (c % 3 == 2) ? 3 : 1;

                javax.swing.border.Border border = new javax.swing.border.MatteBorder(
                        topBorder, leftBorder, bottomBorder, rightBorder, Color.BLACK);
                cell.setBorder(border);
                originalBorders[r][c] = border;

                final int row = r;
                final int col = c;
                cell.addActionListener(e -> selectCell(cell, row, col));

                cells[r][c] = cell;
                gridPanel.add(cell);
            }
        }

        
        JPanel numberPanel = new JPanel(new GridLayout(1, 9, 5, 5));
        numberPanel.setBorder(BorderFactory.createTitledBorder("Select Number"));
        for (int i = 0; i < 9; i++) {
            JButton numBtn = new JButton(String.valueOf(i + 1));
            numBtn.setFont(new Font("Arial", Font.BOLD, 20));
            numBtn.setPreferredSize(new Dimension(60, 60));
            numBtn.setBackground(new Color(200, 220, 255));
            numBtn.setFocusPainted(false);
            final int number = i + 1;
            numBtn.addActionListener(e -> placeNumber(number));
            numberButtons[i] = numBtn;
            numberPanel.add(numBtn);
        }
        add(numberPanel, BorderLayout.SOUTH);

        
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        verifyButton = new JButton("Verify");
        verifyButton.addActionListener(e -> verifyGame());

        solveButton = new JButton("Solve");
        solveButton.setEnabled(false); 
        solveButton.addActionListener(e -> solveGame());

        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undoMove());

        controlPanel.add(verifyButton);
        controlPanel.add(solveButton);
        controlPanel.add(undoButton);
        add(controlPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
    }

    
    private void loadGame() {
        Catalog catalog = controllerFacade.getCatalog();

        if (catalog.hasCurrent()) {
            
            try {
                if (controllerFacade instanceof ControllerFacade) {
                    ControllerFacade facade = (ControllerFacade) controllerFacade;
                    Game currentGame = facade.getControllerA().loadCurrentGame();
                    
                    this.activeGame = currentGame;
                    this.activeGame.addObserver(this);

                    currentBoard = currentGame.getBoard();
                    loadGameIntoGUI();
                    statusLabel.setText("Status: Resumed game");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading current game: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (catalog.hasAllModesExist()) {
            
            String[] options = { "Easy", "Medium", "Hard" };
            int choice = JOptionPane.showOptionDialog(this,
                    "Select difficulty level:",
                    "Choose Difficulty",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice >= 0) {
                char[] levels = { 'e', 'm', 'h' };
                currentDifficulty = levels[choice];
                DifficultyEnum difficulty = DifficultyEnum.fromChar(currentDifficulty);
                try {
                    Game game = controllerFacade.getGame(difficulty);
                    
                    this.activeGame = game;
                    this.activeGame.addObserver(this);

                    currentBoard = game.getBoard();
                    loadGameIntoGUI();
                    statusLabel.setText("Status: " + options[choice] + " game loaded");
                } catch (NotFoundException e) {
                    JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            
            askForSolvedSudokuFile();
        }
    }

    
    private void askForSolvedSudokuFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Solved Sudoku File");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                driveGames(file.getAbsolutePath());

                
                String[] options = { "Easy", "Medium", "Hard" };
                int choice = JOptionPane.showOptionDialog(this,
                        "Games generated! Select difficulty level:",
                        "Choose Difficulty",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice >= 0) {
                    char[] levels = { 'e', 'm', 'h' };
                    currentDifficulty = levels[choice];
                    DifficultyEnum difficulty = DifficultyEnum.fromChar(currentDifficulty);
                    try {
                        Game game = controllerFacade.getGame(difficulty);
                        
                        this.activeGame = game;
                        this.activeGame.addObserver(this);

                        currentBoard = game.getBoard();
                        loadGameIntoGUI();
                        statusLabel.setText("Status: " + options[choice] + " game loaded");
                    } catch (NotFoundException e) {
                        JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private void loadGameIntoGUI() {
        if (currentBoard == null)
            return;

        if (currentBoard == null)
            return;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                isFixed[r][c] = (currentBoard[r][c] != 0);
                isCorrect[r][c] = false;

                if (currentBoard[r][c] != 0) {
                    cells[r][c].setText(String.valueOf(currentBoard[r][c]));
                    cells[r][c].setBackground(new Color(240, 240, 240));
                    cells[r][c].setEnabled(false);
                } else {
                    cells[r][c].setText("");
                    cells[r][c].setBackground(Color.WHITE);
                    cells[r][c].setEnabled(true);
                }
            }
        }

        updateSolveButtonState();
    }

    
    private void updateSolveButtonState() {
        int emptyCount = countEmptyCells();
        solveButton.setEnabled(emptyCount == 5);
    }

    
    private int countEmptyCells() {
        if (currentBoard == null)
            return 0;
        int count = 0;
        for (int[] row : currentBoard) {
            for (int cell : row) {
                if (cell == 0)
                    count++;
            }
        }
        return count;
    }

    
    private void saveBoardState() {
        if (currentBoard == null)
            return;
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(currentBoard[i], 0, copy[i], 0, SIZE);
        }
        boardHistory.push(copy);
    }

    

    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controllerFacade.getCatalog();
        return new boolean[] { catalog.hasCurrent(), catalog.hasAllModesExist() };
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyEnum difficulty = DifficultyEnum.fromChar(level);
        Game game = controllerFacade.getGame(difficulty);
        return game.getBoard();
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        if (controllerFacade instanceof ControllerFacade) {
            ControllerFacade facade = (ControllerFacade) controllerFacade;
            facade.driveGamesFromPath(sourcePath);
        }
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        Game gameObj = new Game(game);
        String result = controllerFacade.verifyGame(gameObj);

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

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        Game gameObj = new Game(game);
        int[] solution = controllerFacade.solveGame(gameObj);

        
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int row = 0; row < game.length; row++) {
            for (int col = 0; col < game[row].length; col++) {
                if (game[row][col] == 0) {
                    emptyCells.add(new int[] { row, col });
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

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controllerFacade.logUserAction(userAction.toLogString());
    }

    private void selectCell(JButton cell, int row, int col) {
        if (isFixed[row][col] || isCorrect[row][col]) {
            return;
        }

        if (selectedCell != null && selectedCell != cell) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (cells[r][c] == selectedCell) {
                        selectedCell.setBorder(originalBorders[r][c]);
                        break;
                    }
                }
            }
        }

        selectedCell = cell;

        int topBorder = (row % 3 == 0) ? 3 : 1;
        int bottomBorder = (row % 3 == 2) ? 3 : 1;
        int leftBorder = (col % 3 == 0) ? 3 : 1;
        int rightBorder = (col % 3 == 2) ? 3 : 1;
        cell.setBorder(new javax.swing.border.MatteBorder(
                topBorder, leftBorder, bottomBorder, rightBorder, Color.BLUE));
    }

    private void placeNumber(int number) {
        if (selectedCell == null) {
            JOptionPane.showMessageDialog(this, "Please select a cell first!",
                    "No Cell Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = -1, col = -1;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c] == selectedCell) {
                    row = r;
                    col = c;
                    break;
                }
            }
            if (row != -1)
                break;
        }

        if (row == -1 || col == -1 || isFixed[row][col]) {
            return;
        }

        
        int previousValue = currentBoard[row][col];
        saveBoardState();

        
        
        

        if (activeGame != null) {
            activeGame.setValue(row, col, number); 
        } else {
            
            currentBoard[row][col] = number;
            cells[row][col].setText(String.valueOf(number));
        }

        
        try {
            UserAction action = new UserAction(row, col, number, previousValue);
            logUserAction(action);
        } catch (IOException e) {
            System.err.println("Error logging action: " + e.getMessage());
        }

        
        try {
            if (controllerFacade instanceof ControllerFacade) {
                ControllerFacade facade = (ControllerFacade) controllerFacade;
                Game game = new Game(currentBoard);
                facade.getControllerA().saveCurrentGame(game);
            }
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }

        updateSolveButtonState();
        selectedCell.setBorder(originalBorders[row][col]);
        selectedCell = null;
    }

    private void verifyGame() {
        if (currentBoard == null)
            return;

        boolean[][] cellValidity = verifyGame(currentBoard);

        
        boolean hasInvalid = false;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (!cellValidity[r][c]) {
                    hasInvalid = true;
                    cells[r][c].setBackground(Color.RED);
                }
            }
        }

        if (hasInvalid) {
            JOptionPane.showMessageDialog(this,
                    "Board contains conflicts or duplicates!",
                    "Invalid",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int emptyCount = countEmptyCells();
            if (emptyCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "Congratulations! Puzzle solved correctly!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                
                try {
                    if (controllerFacade instanceof ControllerFacade) {
                        ControllerFacade facade = (ControllerFacade) controllerFacade;
                        DifficultyEnum difficulty = DifficultyEnum.fromChar(currentDifficulty);
                        Game game = new Game(currentBoard);
                        facade.getControllerA().checkAndDeleteIfComplete(game, difficulty);
                    }
                } catch (Exception e) {
                    System.err.println("Error deleting game: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Board is valid so far, but incomplete (" + emptyCount + " cells remaining).",
                        "Valid",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void solveGame() {
        if (currentBoard == null)
            return;

        int emptyCount = countEmptyCells();
        if (emptyCount != 5) {
            JOptionPane.showMessageDialog(this,
                    "Solver only works with exactly 5 empty cells. Currently: " + emptyCount,
                    "Cannot Solve",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int[][] solution = solveGame(currentBoard);

            
            for (int[] sol : solution) {
                int row = sol[0];
                int col = sol[1];
                int value = sol[2];

                if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                    currentBoard[row][col] = value;
                    cells[row][col].setText(String.valueOf(value));
                    cells[row][col].setBackground(new Color(144, 238, 144)); 
                    isCorrect[row][col] = true;
                }
            }

            
            verifyGame();

        } catch (InvalidGameException e) {
            JOptionPane.showMessageDialog(this,
                    "Cannot solve this puzzle: " + e.getMessage(),
                    "Solve Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void undoMove() {
        if (boardHistory.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No moves to undo.",
                    "Undo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            if (controllerFacade instanceof ControllerFacade) {
                ControllerFacade facade = (ControllerFacade) controllerFacade;
                facade.getControllerA().undoLastMove();
            }

            
            currentBoard = boardHistory.pop();

            
            if (activeGame != null) {
                activeGame.setBoard(currentBoard);
            }

            
            loadGameIntoGUI();

            statusLabel.setText("Status: Undo performed");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error undoing move: " + e.getMessage(),
                    "Undo Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onGameStateChanged(Game game, String state) {
        if (state != null && state.startsWith("UPDATE:")) {
            try {
                String[] parts = state.substring(7).split(",");
                if (parts.length == 3) {
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    int value = Integer.parseInt(parts[2]);

                    if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                        cells[row][col].setText(value == 0 ? "" : String.valueOf(value));
                        
                        if (!cells[row][col].getBackground().equals(Color.RED) &&
                                !cells[row][col].getBackground().equals(new Color(144, 238, 144))) {
                            cells[row][col].setBackground(new Color(240, 248, 255)); 
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing game state update: " + state);
            }
        }
    }
}
