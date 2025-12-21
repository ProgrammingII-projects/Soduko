package model;

/**
 * Enumeration representing the state of a Sudoku game
 */
public enum GameState {
    VALID,      // Fully solved and valid
    INVALID,    // Contains duplicates or conflicts
    INCOMPLETE  // Contains empty cells (zeros) but no conflicts
}

