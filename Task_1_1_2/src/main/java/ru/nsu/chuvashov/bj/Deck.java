package ru.nsu.chuvashov.bj;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Singleton class that represents deck.
 */
public class Deck {
    private static Deck instance;
    private ArrayList<Card> deck;
    private int deckCardIndex;

    /**
     * Constructor for our singleton.
     */
    private Deck() {
        createBigDeck();
    }

    /**
     * Method that overrides constructor to be able,
     * to leave only one instance of class.
     *
     * @return our singletone instance.
     */
    public static Deck getInstance() {
        if (instance == null) {
            instance = new Deck();
        }
        return instance;
    }

    /**
     * creating big deck from which we draw cards.
     */
    private void createBigDeck() {
        deck = new ArrayList<>();
        for (Mast mast : Mast.values()) {
            for (Kards kards : Kards.values()) {
                deck.add(new Card(mast, kards));
            }
        }
        deckCardIndex = 0;
    }

    /**
     * Shuffles deck.
     */
    private void shuffleDeck() {
        Collections.shuffle(deck);
        deckCardIndex = 0;
    }

    /**
     * We want to draw cards from deck.
     *
     * @return card from deck.
     */
    public Card takeCard() {
        if (deckCardIndex >= 52) {
            shuffleDeck();
        }
        return deck.get(deckCardIndex++);
    }
}