import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SudokuGUI extends JFrame {
    private static final int SIZE = 9;
    private JButton[][] cells = new JButton[SIZE][SIZE];
    private JButton[] numberButtons = new JButton[9];
    private JButton selectedCell = null;
    private int[][] solution;
    private int[][] puzzle;
    private int errorCount = 0;
    private JLabel errorLabel;
    private boolean[][] isFixed = new boolean[SIZE][SIZE];
    private boolean[][] isCorrect = new boolean[SIZE][SIZE];
    private javax.swing.border.Border[][] originalBorders = new javax.swing.border.Border[SIZE][SIZE];

    public SudokuGUI() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLayout(new BorderLayout());

       
        SudokuGenerator sg = new SudokuGenerator(SIZE);
        solution = sg.getBoard(); 
        sg.generatePuzzle(20, 1); 
        puzzle = sg.getBoard(); 

   
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                isFixed[r][c] = (puzzle[r][c] != 0);
            }
        }

     
        JPanel topPanel = new JPanel(new FlowLayout());
        errorLabel = new JLabel("Errors: 0");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
        errorLabel.setForeground(Color.RED);
        topPanel.add(errorLabel);
        add(topPanel, BorderLayout.NORTH);

     
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBorder(new LineBorder(Color.BLACK, 3));
        add(gridPanel, BorderLayout.CENTER);

      
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JButton cell = new JButton();
                cell.setFont(new Font("Arial", Font.BOLD, 24));
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setFocusPainted(false);

              
                int topBorder = (r % 3 == 0) ? 3 : 1;
                int bottomBorder = (r % 3 == 2) ? 3 : 1;
                int leftBorder = (c % 3 == 0) ? 3 : 1;
                int rightBorder = (c % 3 == 2) ? 3 : 1;
                
                
                javax.swing.border.Border border = new javax.swing.border.MatteBorder(
                    topBorder, leftBorder, bottomBorder, rightBorder, Color.BLACK);
                cell.setBorder(border);
                originalBorders[r][c] = border;

               
                final int row = r;
                final int col = c;
                if (puzzle[r][c] != 0) {
                    cell.setText(String.valueOf(puzzle[r][c]));
                    cell.setBackground(new Color(240, 240, 240)); // Light gray 
                    cell.setEnabled(false);
                } else {
                    cell.setBackground(Color.WHITE);
                }
               
                if (!isFixed[row][col]) {
                    cell.addActionListener(e -> selectCell(cell, row, col));
                }

                cells[r][c] = cell;
                gridPanel.add(cell);
            }
        }

        
        JPanel numberPanel = new JPanel(new GridLayout(1, 9, 5, 5));
        numberPanel.setBorder(BorderFactory.createTitledBorder("Select Number"));
        for (int i = 0; i < 9; i++) {
            JButton numBtn = new JButton(String.valueOf(i + 1));
            numBtn.setFont(new Font("Arial", Font.BOLD, 20));
            numBtn.setPreferredSize(new Dimension(60, 60));
            numBtn.setBackground(new Color(200, 220, 255));
            numBtn.setFocusPainted(false);
            final int number = i + 1;
            numBtn.addActionListener(e -> placeNumber(number));
            numberButtons[i] = numBtn;
            numberPanel.add(numBtn);
        }
        add(numberPanel, BorderLayout.SOUTH);

        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void selectCell(JButton cell, int row, int col) {
        
        if (isFixed[row][col] || isCorrect[row][col]) {
            return;
        }

       
        if (selectedCell != null && selectedCell != cell) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (cells[r][c] == selectedCell) {
                        selectedCell.setBorder(originalBorders[r][c]);
                        break;
                    }
                }
            }
        }

        
        selectedCell = cell;

        
        int topBorder = (row % 3 == 0) ? 3 : 1;
        int bottomBorder = (row % 3 == 2) ? 3 : 1;
        int leftBorder = (col % 3 == 0) ? 3 : 1;
        int rightBorder = (col % 3 == 2) ? 3 : 1;
        cell.setBorder(new javax.swing.border.MatteBorder(
            topBorder, leftBorder, bottomBorder, rightBorder, Color.BLUE));
    }

    private void placeNumber(int number) {
        if (selectedCell == null) {
            JOptionPane.showMessageDialog(this, "Please select a cell first!", 
                "No Cell Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

       
        int row = -1, col = -1;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c] == selectedCell) {
                    row = r;
                    col = c;
                    break;
                }
            }
            if (row != -1) break;
        }

        if (row == -1 || col == -1 || isFixed[row][col]) {
            return;
        }

       
        selectedCell.setText(String.valueOf(number));
        puzzle[row][col] = number;

       
        boolean correct = (number == solution[row][col]);
        
        if (correct) {
            selectedCell.setBackground(new Color(144, 238, 144)); // Light green
         
            isCorrect[row][col] = true;
            selectedCell.setEnabled(false);
            
            selectedCell.setBorder(originalBorders[row][col]);
            selectedCell = null;
        } else {
            selectedCell.setBackground(new Color(255, 150, 150)); // Light red 
         
            errorCount++;
            errorLabel.setText("Errors: " + errorCount);
         
        }
    }
}

