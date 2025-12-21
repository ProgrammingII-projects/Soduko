package utils;

import model.Game;

/**
 * Observer Pattern: Interface for observing game state changes
 * Additional design pattern beyond required ones
 * Encapsulation: Encapsulates observer contract
 */
public interface GameStateObserver {
    /**
     * Called when game state changes
     * @param game The game that changed
     * @param state The new state
     */
    void onGameStateChanged(Game game, String state);
}

