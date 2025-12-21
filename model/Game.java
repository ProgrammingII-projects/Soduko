package model;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private int[][] board;
    private List<GameStateObserver> observers = new ArrayList<>();

    public Game(int[][] board) {
        
        
        this.board = board;
    }

    public void addObserver(GameStateObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(GameStateObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String state) {
        for (GameStateObserver observer : observers) {
            observer.onGameStateChanged(this, state);
        }
    }

    public void setValue(int row, int col, int value) {
        if (board != null && row >= 0 && row < board.length && col >= 0 && col < board[0].length) {
            board[row][col] = value;
            notifyObservers("UPDATE:" + row + "," + col + "," + value);
        }
    }

    
    public int[][] getBoard() {
        return board;
    }

    
    public void setBoard(int[][] board) {
        this.board = board;
    }

    
    public int getSize() {
        return board != null ? board.length : 0;
    }

    
    public boolean isComplete() {
        if (board == null)
            return false;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    
    public int countEmptyCells() {
        if (board == null)
            return 0;
        int count = 0;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    count++;
                }
            }
        }
        return count;
    }
}
