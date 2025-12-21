package utils;

import model.DifficultyEnum;
import model.Game;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Storage system for Sudoku games
 * Manages folders: easy, medium, hard, and current
 * Singleton Pattern: Single storage instance
 * Encapsulation: Encapsulates file system operations
 */
public class GameStorage {
    private static GameStorage instance;
    private static final String EASY_FOLDER = "easy";
    private static final String MEDIUM_FOLDER = "medium";
    private static final String HARD_FOLDER = "hard";
    private static final String CURRENT_FOLDER = "current";
    private static final String INCOMPLETE_FOLDER = "incomplete";
    private static final String GAME_FILE_PREFIX = "game_";
    private static final String GAME_FILE_EXTENSION = ".txt";
    private static final String LOG_FILE_NAME = "game_log.txt";
    
    private final String basePath;
    
    private GameStorage() {
        this.basePath = "games";
        initializeFolders();
    }
    
    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }
    
    private void initializeFolders() {
        try {
            Files.createDirectories(Paths.get(basePath, EASY_FOLDER));
            Files.createDirectories(Paths.get(basePath, MEDIUM_FOLDER));
            Files.createDirectories(Paths.get(basePath, HARD_FOLDER));
            Files.createDirectories(Paths.get(basePath, CURRENT_FOLDER));
            Files.createDirectories(Paths.get(basePath, INCOMPLETE_FOLDER));
        } catch (IOException e) {
            System.err.println("Error creating folders: " + e.getMessage());
        }
    }
    
    public void saveGame(DifficultyEnum difficulty, Game game) throws IOException {
        String folder = getFolderName(difficulty);
        String filename = GAME_FILE_PREFIX + System.currentTimeMillis() + GAME_FILE_EXTENSION;
        Path filePath = Paths.get(basePath, folder, filename);
        saveGameToFile(filePath, game);
    }
    
    public void saveCurrentGame(Game game) throws IOException {
        Path filePath = Paths.get(basePath, CURRENT_FOLDER, "current_game" + GAME_FILE_EXTENSION);
        saveGameToFile(filePath, game);
        Path incompleteGamePath = Paths.get(basePath, INCOMPLETE_FOLDER, "current_game" + GAME_FILE_EXTENSION);
        saveGameToFile(incompleteGamePath, game);
    }
    
    public Game loadRandomGame(DifficultyEnum difficulty) throws IOException, exceptions.NotFoundException {
        String folder = getFolderName(difficulty);
        Path folderPath = Paths.get(basePath, folder);
        if (!Files.exists(folderPath)) {
            throw new exceptions.NotFoundException("Folder does not exist: " + folder);
        }
        List<Path> gameFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, 
                path -> path.toString().endsWith(GAME_FILE_EXTENSION))) {
            for (Path entry : stream) {
                gameFiles.add(entry);
            }
        }
        if (gameFiles.isEmpty()) {
            throw new exceptions.NotFoundException("No games found for difficulty: " + difficulty);
        }
        Random random = new Random();
        Path selectedFile = gameFiles.get(random.nextInt(gameFiles.size()));
        return loadGameFromFile(selectedFile);
    }
    
    public Game loadCurrentGame() throws IOException, exceptions.NotFoundException {
        Path filePath = Paths.get(basePath, INCOMPLETE_FOLDER, "current_game" + GAME_FILE_EXTENSION);
        if (!Files.exists(filePath)) {
            throw new exceptions.NotFoundException("No current game found");
        }
        return loadGameFromFile(filePath);
    }
    
    public boolean hasCurrentGame() {
        Path filePath = Paths.get(basePath, INCOMPLETE_FOLDER, "current_game" + GAME_FILE_EXTENSION);
        return Files.exists(filePath);
    }
    
    public boolean hasAllDifficultyGames() {
        return hasGamesInFolder(EASY_FOLDER) && 
               hasGamesInFolder(MEDIUM_FOLDER) && 
               hasGamesInFolder(HARD_FOLDER);
    }
    
    public void deleteGame(DifficultyEnum difficulty, Game game) throws IOException {
        String folder = getFolderName(difficulty);
        Path folderPath = Paths.get(basePath, folder);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, 
                path -> path.toString().endsWith(GAME_FILE_EXTENSION))) {
            for (Path file : stream) {
                Game fileGame = loadGameFromFile(file);
                if (Arrays.deepEquals(fileGame.getBoard(), game.getBoard())) {
                    Files.delete(file);
                    return;
                }
            }
        }
    }
    
    public void deleteCurrentGame() throws IOException {
        Path gamePath = Paths.get(basePath, INCOMPLETE_FOLDER, "current_game" + GAME_FILE_EXTENSION);
        Path logPath = Paths.get(basePath, INCOMPLETE_FOLDER, LOG_FILE_NAME);
        if (Files.exists(gamePath)) {
            Files.delete(gamePath);
        }
        if (Files.exists(logPath)) {
            Files.delete(logPath);
        }
        Path currentPath = Paths.get(basePath, CURRENT_FOLDER, "current_game" + GAME_FILE_EXTENSION);
        if (Files.exists(currentPath)) {
            Files.delete(currentPath);
        }
    }
    
    public void appendToLog(String logEntry) throws IOException {
        Path logPath = Paths.get(basePath, INCOMPLETE_FOLDER, LOG_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(logPath, 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(logEntry);
            writer.newLine();
        }
    }
    
    public List<String> readLogEntries() throws IOException {
        Path logPath = Paths.get(basePath, INCOMPLETE_FOLDER, LOG_FILE_NAME);
        if (!Files.exists(logPath)) {
            return new ArrayList<>();
        }
        List<String> entries = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                entries.add(line.trim());
            }
        }
        return entries;
    }
    
    public void removeLastLogEntry() throws IOException {
        List<String> entries = readLogEntries();
        if (entries.isEmpty()) {
            return;
        }
        entries.remove(entries.size() - 1);
        Path logPath = Paths.get(basePath, INCOMPLETE_FOLDER, LOG_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(logPath, 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String entry : entries) {
                writer.write(entry);
                writer.newLine();
            }
        }
    }
    
    private void saveGameToFile(Path filePath, Game game) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            int[][] board = game.getBoard();
            for (int[] row : board) {
                for (int i = 0; i < row.length; i++) {
                    writer.print(row[i]);
                    if (i < row.length - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        }
    }
    
    private Game loadGameFromFile(Path filePath) throws IOException {
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
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
    
    private boolean hasGamesInFolder(String folder) {
        Path folderPath = Paths.get(basePath, folder);
        if (!Files.exists(folderPath)) {
            return false;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, 
                path -> path.toString().endsWith(GAME_FILE_EXTENSION))) {
            return stream.iterator().hasNext();
        } catch (IOException e) {
            return false;
        }
    }
    
    private String getFolderName(DifficultyEnum difficulty) {
        switch (difficulty) {
            case EASY: return EASY_FOLDER;
            case MEDIUM: return MEDIUM_FOLDER;
            case HARD: return HARD_FOLDER;
            default: throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }
    }
}

