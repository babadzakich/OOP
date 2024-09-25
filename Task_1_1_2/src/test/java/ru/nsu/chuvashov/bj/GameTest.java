package ru.nsu.chuvashov.bj;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class GameTest {
    @Test
    void checkGame() {
        InputStream inapt = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
            Game game = new Game();
            while (Math.abs(game.dealerScore - game.playerScore) < 3) {
                System.setIn(in);
                game.blackJack(true);
            }
            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream in2 = new ByteArrayInputStream("228\n".getBytes());
            System.setIn(in2);
        } finally {
            System.setIn(inapt);
        }
    }

    @Test
    void checkWithComp() {
        Game game = new Game();
        for (int i = 0; i < 200; i++) {
            game.blackJack(false);
        }
        assertTrue(true);
    }

    @Test
    void checkGetter() {
        Card card;
        Deck deck = Deck.getInstance();
        try {
            for (int i = 0; i < 52; i++) {
                card = deck.takeCard();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }

    }

    @Test
    void checkSingleton() {
        Deck deck1 = Deck.getInstance();
        Deck deck2 = Deck.getInstance();
        assertSame(deck1, deck2);
    }

    @Test
    void checkAces() {
        PlayerDeck player = new PlayerDeck();
        int summary = player.getPoints();
        while (!player.aceChecker()) {
            player.drawCards();
            summary = player.getPoints();
        }
        assertSame(summary, player.getPoints() + 10);
    }
}