package cardsgame.model;

import java.util.Vector;

public class HPlayer {
    private String username;

    private Vector<HCard> cards;

    public HPlayer(String username) {
        this.username = username;
        cards = new Vector<HCard>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Vector<HCard> getCards() {
        return cards;
    }

    public void setCards(Vector<HCard> cards) {
        this.cards = cards;
    }

}
