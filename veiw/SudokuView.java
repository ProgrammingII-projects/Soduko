package veiw;
import javax.swing.*;
import javax.swing.border.LineBorder;

import model.SudokuModel;

import java.awt.*;

public class SudokuView extends JFrame {
    private static final int SIZE = 9;
    private JButton[][] cells = new JButton[SIZE][SIZE];
    private JButton[] numberButtons = new JButton[9];
    private JLabel errorLabel;
    private javax.swing.border.Border[][] originalBorders = new javax.swing.border.Border[SIZE][SIZE];

    public SudokuView() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLayout(new BorderLayout());
        initializeUI();
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        // Error counter panel
        JPanel topPanel = new JPanel(new FlowLayout());
        errorLabel = new JLabel("Errors: 0");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
        errorLabel.setForeground(Color.RED);
        topPanel.add(errorLabel);
        add(topPanel, BorderLayout.NORTH);

        // Sudoku grid panel
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBorder(new LineBorder(Color.BLACK, 3));
        add(gridPanel, BorderLayout.CENTER);

        // Create cell buttons
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JButton cell = createCellButton(r, c);
                cells[r][c] = cell;
                gridPanel.add(cell);
            }
        }

        // Number buttons panel
        JPanel numberPanel = new JPanel(new GridLayout(1, 9, 5, 5));
        numberPanel.setBorder(BorderFactory.createTitledBorder("Select Number"));
        for (int i = 0; i < 9; i++) {
            JButton numBtn = createNumberButton(i + 1);
            numberButtons[i] = numBtn;
            numberPanel.add(numBtn);
        }
        add(numberPanel, BorderLayout.SOUTH);
    }

    private JButton createCellButton(int r, int c) {
        JButton cell = new JButton();
        cell.setFont(new Font("Arial", Font.BOLD, 24));
        cell.setPreferredSize(new Dimension(60, 60));
        cell.setFocusPainted(false);

        // Create border for 3x3 boxes
        int topBorder = (r % 3 == 0) ? 3 : 1;
        int bottomBorder = (r % 3 == 2) ? 3 : 1;
        int leftBorder = (c % 3 == 0) ? 3 : 1;
        int rightBorder = (c % 3 == 2) ? 3 : 1;

        javax.swing.border.Border border = new javax.swing.border.MatteBorder(
            topBorder, leftBorder, bottomBorder, rightBorder, Color.BLACK);
        cell.setBorder(border);
        originalBorders[r][c] = border;

        return cell;
    }

    private JButton createNumberButton(int number) {
        JButton numBtn = new JButton(String.valueOf(number));
        numBtn.setFont(new Font("Arial", Font.BOLD, 20));
        numBtn.setPreferredSize(new Dimension(60, 60));
        numBtn.setBackground(new Color(200, 220, 255));
        numBtn.setFocusPainted(false);
        return numBtn;
    }

    public void setCellListener(int row, int col, Runnable listener) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            cells[row][col].addActionListener(e -> listener.run());
        }
    }

    public void setNumberButtonListener(int number, Runnable listener) {
        if (number >= 1 && number <= 9) {
            numberButtons[number - 1].addActionListener(e -> listener.run());
        }
    }

    public void updateCell(int row, int col, int value, boolean isFixed, boolean isCorrect) {
        JButton cell = cells[row][col];
        if (value != 0) {
            cell.setText(String.valueOf(value));
        } else {
            cell.setText("");
        }

        if (isFixed) {
            cell.setBackground(new Color(240, 240, 240));
            cell.setEnabled(false);
        } else if (isCorrect) {
            cell.setBackground(new Color(144, 238, 144));
            cell.setEnabled(false);
        } else {
            cell.setBackground(Color.WHITE);
            cell.setEnabled(true);
        }
    }

    public void highlightCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            int topBorder = (row % 3 == 0) ? 3 : 1;
            int bottomBorder = (row % 3 == 2) ? 3 : 1;
            int leftBorder = (col % 3 == 0) ? 3 : 1;
            int rightBorder = (col % 3 == 2) ? 3 : 1;
            cells[row][col].setBorder(new javax.swing.border.MatteBorder(
                topBorder, leftBorder, bottomBorder, rightBorder, Color.BLUE));
        }
    }

    public void unhighlightCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            cells[row][col].setBorder(originalBorders[row][col]);
        }
    }

    public void updateErrorCount(int count) {
        errorLabel.setText("Errors: " + count);
    }

    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public void initializePuzzle(SudokuModel model) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int value = model.getCellValue(r, c);
                boolean isFixed = model.isFixed(r, c);
                boolean isCorrect = model.isCorrect(r, c);
                updateCell(r, c, value, isFixed, isCorrect);
            }
        }
    }
}

