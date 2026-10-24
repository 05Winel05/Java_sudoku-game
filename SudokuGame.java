import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;

public class SudokuGame extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private int[][] puzzle = new int[SIZE][SIZE];
    private JTextField selectedCell = null;
    private boolean[][] isEditable = new boolean[SIZE][SIZE];

    public SudokuGame() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 255));
        setLayout(new BorderLayout(15, 15));

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        gridPanel.setBackground(new Color(210, 230, 255));
        Font font = new Font("SansSerif", Font.BOLD, 24);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);
                cell.setEditable(false);
                cell.setBackground(Color.WHITE);
                cell.setBorder(getCellBorder(row, col));
                restrictInputToOneDigit(cell);
                int finalRow = row;
                int finalCol = col;
                cell.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (puzzle[finalRow][finalCol] == 0) {
                            selectedCell = cell;
                            highlightSelectedCell();
                        }
                    }
                });
                cells[row][col] = cell;
                gridPanel.add(cell);
            }
        }

        JButton generateButton = createStyledButton("Generate New");
        generateButton.addActionListener(e -> generateNewPuzzle());

        JButton solveButton = createStyledButton("Solve");
        solveButton.addActionListener(e -> solvePuzzle());

        JButton checkButton = createStyledButton("Check Solution");
        checkButton.addActionListener(e -> checkSolution());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(230, 240, 255));
        controlPanel.add(generateButton);
        controlPanel.add(solveButton);
        controlPanel.add(checkButton);

        JPanel numberPanel = new JPanel(new GridLayout(1, 9, 8, 8));
        numberPanel.setBackground(new Color(230, 240, 255));
        for (int i = 1; i <= 9; i++) {
            JButton numberButton = createStyledNumberButton(String.valueOf(i));
            int number = i;
            numberButton.addActionListener(e -> fillSelectedCell(number));
            numberPanel.add(numberButton);
        }

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(230, 240, 255));
        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(numberPanel, BorderLayout.SOUTH);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        generateNewPuzzle();
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 120, 200), 2));
        return button;
    }

    private JButton createStyledNumberButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(180, 220, 250));
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBorder(new LineBorder(new Color(120, 160, 220), 2));
        return button;
    }

    private void restrictInputToOneDigit(JTextField cell) {
        ((AbstractDocument) cell.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs)
                    throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null || text.isEmpty()) {
                    super.replace(fb, offset, length, text, attrs);
                } else if (text.matches("[1-9]")) {
                    // Typing a new number replaces the previous one.
                    super.replace(fb, 0, fb.getDocument().getLength(), text, attrs);
                }
            }
        });
    }

    private Border getCellBorder(int row, int col) {
        int top = (row % 3 == 0) ? 3 : 1;
        int left = (col % 3 == 0) ? 3 : 1;
        int bottom = 1;
        int right = 1;
        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.GRAY);
    }

    private void highlightSelectedCell() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField cell = cells[row][col];
                if (cell == selectedCell) {
                    cell.setBackground(new Color(200, 220, 255));
                } else if (puzzle[row][col] == 0) {
                    cell.setBackground(Color.WHITE);
                } else {
                    cell.setBackground(new Color(230, 230, 230));
                }
            }
        }
    }

    private void fillSelectedCell(int number) {
        if (selectedCell != null && selectedCell.isEditable()) {
            selectedCell.setText(String.valueOf(number));
            selectedCell.setBackground(Color.WHITE);
            selectedCell = null;
        }
    }

    private void generateNewPuzzle() {
        clearBoard();
        selectedCell = null;
        SudokuGenerator generator = new SudokuGenerator();
        puzzle = generator.generatePuzzle();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (puzzle[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(puzzle[row][col]));
                    cells[row][col].setForeground(Color.BLACK);
                    cells[row][col].setEditable(false);
                    isEditable[row][col] = false;
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true);
                    isEditable[row][col] = true;
                }
            }
        }
    }

    private void solvePuzzle() {
        int[][] tempBoard = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            tempBoard[row] = puzzle[row].clone();
        }
        if (SudokuSolver.solve(tempBoard)) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    cells[row][col].setText(String.valueOf(tempBoard[row][col]));
                    if (isEditable[row][col]) {
                        cells[row][col].setForeground(new Color(0, 128, 0));
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkSolution() {
        int[][] solvedBoard = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                solvedBoard[row][col] = puzzle[row][col];
            }
        }
        SudokuSolver.solve(solvedBoard);

        boolean correct = true;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText();
                if (isEditable[row][col]) { // Check only the cells the player can edit
                    if (!text.matches("[1-9]") || Integer.parseInt(text) != solvedBoard[row][col]) {
                        cells[row][col].setBackground(new Color(255, 180, 180)); // Incorrect answer
                        correct = false;
                    } else {
                        cells[row][col].setBackground(new Color(180, 255, 180)); // Correct answer
                    }
                }
            }
        }

        if (correct) {
            JOptionPane.showMessageDialog(this, "Congratulations! You solved all editable cells correctly!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Some entries are incorrect. Please check the red cells.", "Check", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText("");
                cells[row][col].setForeground(Color.BLACK);
                cells[row][col].setBackground(Color.WHITE);
            }
        }
    }

}
