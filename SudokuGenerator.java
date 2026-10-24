import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private Random random = new Random();

    public int[][] generatePuzzle() {
        int[][] board = new int[SIZE][SIZE];
        fillDiagonal(board);
        if (!SudokuSolver.solve(board)) {
            throw new IllegalStateException("Could not generate a valid Sudoku board.");
        }
        removeNumbers(board);
        return board;
    }

    private void fillDiagonal(int[][] board) {
        for (int i = 0; i < SIZE; i += 3) {
            fillBox(board, i, i);
        }
    }

    private void fillBox(int[][] board, int row, int col) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num;
                do {
                    num = random.nextInt(SIZE) + 1;
                } while (!isSafeInBox(board, row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }

    private boolean isSafeInBox(int[][] board, int boxStartRow, int boxStartCol, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxStartRow + i][boxStartCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(int[][] board) {
        // Remove cells in random order while keeping exactly one solution.
        int[] positions = new int[SIZE * SIZE];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = i;
        }
        for (int i = positions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        int target = 40 + random.nextInt(10);
        int removed = 0;
        for (int position : positions) {
            if (removed >= target) {
                break;
            }
            int row = position / SIZE;
            int col = position % SIZE;
            int original = board[row][col];
            board[row][col] = 0;

            // Put the number back if removing it allows multiple solutions.
            if (SudokuSolver.countSolutions(board, 2) != 1) {
                board[row][col] = original;
            } else {
                removed++;
            }
        }
    }
}
