package utils;

import model.DifficultyEnum;
import model.Game;


public class GameFactory {

    public static Game createDifficultyGame(Game source, DifficultyEnum diff, RandomPairs randomPairs) {
        return switch (diff) {
            case EASY -> createGame(source, 10, randomPairs);
            case MEDIUM -> createGame(source, 20, randomPairs);
            case HARD -> createGame(source, 25, randomPairs);
        };
    }

    private static Game createGame(Game source, int cells, RandomPairs randomPairs) {
        int[][] board = source.getBoard();
        int size = board.length;

        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, size);
        }

        for (int[] p : randomPairs.generateDistinctPairs(cells)) {
            newBoard[p[0]][p[1]] = 0;
        }

        return new Game(newBoard);
    }
}