package ru.nsu.chuvashov.bj;

import java.util.ArrayList;
import ru.nsu.chuvashov.bj.Card;

/**
 * Class for player and dealer decks.
 */
public class PlayerDeck {
    Deck bigDeck = Deck.getInstance();
    private int summary = 0;
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Integer> aces = new ArrayList<>();

    /**
     * We draw card,
     * save summary of cards in the first elem of deck,
     * and store in first element where aces located.
     */
    public void drawCards() {
        deck.add(bigDeck.getter());
        if (deck.get(deck.size() - 1).value == 11) {
            aces.add(deck.size() - 1);
        }
        summary += deck.get(deck.size() - 1).value;
    }

    /**
     * We create players deck.
     * We also store the summary and aces pos in 1 elem.
     */
    public PlayerDeck() {
        deck.add(bigDeck.getter());
        if (deck.get(deck.size() - 1).value == 11) {
            aces.add(deck.size() - 1);
        }
        summary += deck.get(deck.size() - 1).value;
        deck.add(bigDeck.getter());
        if (deck.get(deck.size() - 1).value == 11) {
            aces.add(deck.size() - 1);
        }
        summary += deck.get(deck.size() - 1).value;
    }

    /**
     * We return the number of player points.
     *
     * @return points of a player.
     */
    public int getPoints() {
        return summary;
    }

    /**
     * Deck is being cleared.
     */
    public void clear() {
        summary = 0;
        aces.clear();
        deck.clear();
    }

    /**
     * Getting last card in deck.
     *
     * @return last card in deck.
     */
    public Card getLast() {
        return deck.get(deck.size() - 1);
    }

    /**
     * We want to be able to show our deck.
     *
     * @return string implement of deck.
     */
    public String toString() {
        return deck.toString();
    }

    /**
     * We want to see first card in deck.
     *
     * @return first card in deck.
     */
    public Card getFirst() {
        return deck.get(0);
    }

    /**
     * We want to be able to check,
     * if we can stay in game by,
     * changing value of ace from 11 to 1.
     *
     * @return true - change ace, false - no aces.
     */
    public boolean aceChecker() {
        if (aces.isEmpty()) {
            return false;
        }
        deck.get(aces.get(0)).value = 1;
        aces.remove(0);
        summary -= 10;
        return true;
    }
}
