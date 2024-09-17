package ru.nsu.chuvashov;

import java.util.ArrayList;
import java.util.Collections;

/**
 * class that represents deck.
 */
class Deck {

    static ArrayList<Card> deck;
    static int deck_card_index;

    /**
     * creating big deck from which we draw cards.
     */
    public static void createBigDeck() {
        deck = new ArrayList<>();
        for (Mast mast : Mast.values()) {
            for (Kards kards : Kards.values()) {
                deck.add(new Card(mast, kards));
            }
        }
        deck_card_index = 0;
    }

    /**
     * Shuffles deck.
     */
    public static void shuffleDeck() {
        Collections.shuffle(deck);
        deck_card_index = 0;
    }

    /**
     * We draw card,
     * save summary of cards in the first elem of deck,
     * and store in first element where aces located.
     *
     * @param player deck where we draw cards.
     */
    public static void draw_cards(ArrayList<Card> player) {
        player.add(deck.get(deck_card_index++));
        if (player.get(player.size() - 1).value == 11) {
            player.get(0).aces.add(player.size() - 1);
        }
        player.get(0).summary += player.get(player.size() - 1).value;
    }

    /**
     * We create players deck.
     * We also store the summary and aces pos in 1 elem.
     *
     * @param player deck which we create.
     */
    public static void createSmallDeck(ArrayList<Card> player) {
        player.add(deck.get(deck_card_index++));
        if (player.get(player.size() - 1).value == 11) {
            player.get(0).aces.add(player.size() - 1);
        }
        player.add(deck.get(deck_card_index++));
        if (player.get(player.size() - 1).value == 11) {
            player.get(0).aces.add(player.size() - 1);
        }
        player.get(0).summary += player.get(player.size() - 1).value;
    }

    /**
     * We return the number of player points.
     *
     * @param player deck which points we get.
     * @return points of a player.
     */
    public static int getPoints(ArrayList<Card> player) {
        return player.get(0).summary;
    }
}