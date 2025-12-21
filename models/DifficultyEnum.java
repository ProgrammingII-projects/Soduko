package model;


public enum DifficultyEnum {
    EASY(10),      
    MEDIUM(20),    
    HARD(25);      
    
    private final int cellsToRemove;
    
    DifficultyEnum(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }
    
    public int getCellsToRemove() {
        return cellsToRemove;
    }
    
    
    public static DifficultyEnum fromChar(char c) {
        switch (Character.toLowerCase(c)) {
            case 'e': return EASY;
            case 'm': return MEDIUM;
            case 'h': return HARD;
            default: throw new IllegalArgumentException("Invalid difficulty: " + c);
        }
    }
    
    
    public char toChar() {
        switch (this) {
            case EASY: return 'e';
            case MEDIUM: return 'm';
            case HARD: return 'h';
            default: throw new IllegalStateException("Unknown difficulty");
        }
    }
}

