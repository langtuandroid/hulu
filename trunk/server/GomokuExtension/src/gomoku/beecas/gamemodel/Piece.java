package gomoku.beecas.gamemodel;

public enum Piece {
    EMPTY(0),
    NOUGHT(1),
    CROSS(2);

    private int id;

    private Piece(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
