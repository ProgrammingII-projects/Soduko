package model;


public interface GameStateObserver {
    
    void onGameStateChanged(Game game, String state);
}
