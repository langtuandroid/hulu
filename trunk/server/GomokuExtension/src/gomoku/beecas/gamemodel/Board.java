package gomoku.beecas.gamemodel;

public class Board {
    private static final int BOARD_SIZE = 4;

    private final Piece[][] board;

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        reset();
    }

    private void reset() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            Piece[] boardRow = board[y];
            for (int x = 0; x < BOARD_SIZE; x++) {
                boardRow[x] = Piece.EMPTY;
            }
        }
    }

    public boolean makeMove(int x, int y, Piece piece) {
        if (board[x][y] == Piece.EMPTY) {
            checkCoords(x, y);
            board[x][y] = piece;
            return true;
        }
        return false;
    }

    private void checkCoords(int x, int y) {
        if ((x < 1) || (x > BOARD_SIZE)) {
            throw new IllegalArgumentException("Piece X position out of range: " + x);
        }
        if ((y < 1) || (y > BOARD_SIZE))
            throw new IllegalArgumentException("Piece Y position out of range: " + y);
    }
}
