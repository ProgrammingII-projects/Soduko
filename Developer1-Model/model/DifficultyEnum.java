package model;

/**
 * Enumeration representing difficulty levels for Sudoku games
 */
public enum DifficultyEnum {
    EASY(10),      // Remove 10 cells
    MEDIUM(20),    // Remove 20 cells
    HARD(25);      // Remove 25 cells
    
    private final int cellsToRemove;
    
    DifficultyEnum(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }
    
    public int getCellsToRemove() {
        return cellsToRemove;
    }
    
    /**
     * Convert char to DifficultyEnum
     */
    public static DifficultyEnum fromChar(char c) {
        switch (Character.toLowerCase(c)) {
            case 'e': return EASY;
            case 'm': return MEDIUM;
            case 'h': return HARD;
            default: throw new IllegalArgumentException("Invalid difficulty: " + c);
        }
    }
    
    /**
     * Convert DifficultyEnum to char
     */
    public char toChar() {
        switch (this) {
            case EASY: return 'e';
            case MEDIUM: return 'm';
            case HARD: return 'h';
            default: throw new IllegalStateException("Unknown difficulty");
        }
    }
}

