package Services;

import model.Sudoku;

public class solve0Thread {
    private Sudoku sudoku;
    private int[][] board;
    private int row;
    private int column;
    private int num;

    solve0Thread(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public boolean Check_Board(){
        boolean check = sudoku.isSafe(row, column, num);
    return check;
    }

    
}
