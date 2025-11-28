

public class Main {
    public static void main(String[] args) {
        int N = 9; // 9x9 board, change to 4, 16, etc, for other sizes
        SudokuGenerator sg = new SudokuGenerator(N);
        System.out.println("Generated Sudoku Board:");
        sg.printBoard();

        // Optionally, demonstrate puzzle generation as well:
        sg.generatePuzzle(40, 2); // Remove 40 cells, allow up to 2 solutions
        System.out.println("Generated Sudoku Puzzle (up to 2 solutions):");
        sg.printBoard();
    }
}

