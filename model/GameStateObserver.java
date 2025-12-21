package model;

/**
 * Observer Pattern: Interface for observing game state changes
 * Encapsulation: Encapsulates observer contract
 */
public interface GameStateObserver {
    /**
     * Called when game state changes
     * 
     * @param game  The game that changed
     * @param state The new state
     */
    void onGameStateChanged(Game game, String state);
}
