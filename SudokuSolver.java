public class SudokuSolver {
    private static final int SIZE = 9;

    public static boolean solve(int[][] board) {
        if (!isBoardValid(board)) {
            return false;
        }
        return solveRecursive(board);
    }

    // Counts solutions up to a limit. The board is restored before returning.
    public static int countSolutions(int[][] board, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be positive.");
        }
        if (!isBoardValid(board)) {
            return 0;
        }
        return countSolutionsRecursive(board, limit);
    }

    private static boolean solveRecursive(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveRecursive(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static int countSolutionsRecursive(int[][] board, int limit) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    int found = 0;
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            found += countSolutionsRecursive(board, limit - found);
                            board[row][col] = 0;
                            if (found >= limit) {
                                return found;
                            }
                        }
                    }
                    return found;
                }
            }
        }
        return 1;
    }

    private static boolean isBoardValid(int[][] board) {
        if (board == null || board.length != SIZE) {
            return false;
        }
        for (int row = 0; row < SIZE; row++) {
            if (board[row] == null || board[row].length != SIZE) {
                return false;
            }
            for (int col = 0; col < SIZE; col++) {
                int value = board[row][col];
                if (value < 0 || value > 9) {
                    return false;
                }
                if (value != 0) {
                    board[row][col] = 0;
                    boolean valid = isValid(board, row, col, value);
                    board[row][col] = value;
                    if (!valid) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }
}
