package cardsgame.model;

public class HCard {
    private byte num;

    private byte suit;

    public HCard(int num, int suit) {
        this.num = (byte) num;
        this.suit = (byte) suit;
    }

    public HCard(int value) {
        this.num = (byte) (value / 4);
        this.suit = (byte) (value % 4);
    }

    public byte getValue() {
        return ((byte) (num * 4 + suit));
    }

    public byte getNum() {
        return num;
    }

    public void setNum(byte num) {
        this.num = num;
    }

    public byte getSuit() {
        return suit;
    }

    public void setSuit(byte suit) {
        this.suit = suit;
    }
}
