package utils;

import model.Game;
import exceptions.InvalidGameException;

import java.util.concurrent.atomic.AtomicBoolean;

public class SudokuSolver {

    private static final int REQUIRED_EMPTY_CELLS = 5;

    public int[] solve(Game game) throws InvalidGameException {
        int[][] board = game.getBoard();

        int[][] emptyCells = findEmptyCells(board);

        if (emptyCells.length != REQUIRED_EMPTY_CELLS) {
            throw new InvalidGameException(
                "Solver only works with exactly " + REQUIRED_EMPTY_CELLS +
                " empty cells. Found: " + emptyCells.length);
        }

        PermutationIterator iterator =
                new PermutationIterator(REQUIRED_EMPTY_CELLS, 9);

        while (iterator.hasNext()) {
            int[] permutation = iterator.next();

            // 1️⃣ انسخ البورد
            int[][] testBoard = copyBoard(board);

            // 2️⃣ طبّق الـ permutation
            for (int i = 0; i < emptyCells.length; i++) {
                int row = emptyCells[i][0];
                int col = emptyCells[i][1];
                testBoard[row][col] = permutation[i];
            }

            // 3️⃣ شغّل Thread
            AtomicBoolean isValid = new AtomicBoolean(true);
            solve0Thread validator = new solve0Thread(testBoard, isValid);
            validator.start();

            try {
                validator.join(); // نستنى النتيجة
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InvalidGameException("Solver interrupted");
            }

            // 4️⃣ لو valid → حل صحيح
            if (isValid.get()) {
                return permutation;
            }
        }

        throw new InvalidGameException("No valid solution found");
    }

    // ================= helpers =================

    private int[][] findEmptyCells(int[][] board) {
        java.util.List<int[]> emptyList = new java.util.ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 0) {
                    emptyList.add(new int[]{r, c});
                }
            }
        }
        return emptyList.toArray(new int[0][]);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board.length);
        }
        return copy;
    }
}
