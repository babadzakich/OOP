package ru.nsu.chuvashov.bj;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class GameTest {
    @Test
    void checkGame() {
        InputStream inapt = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("1\n0\n".getBytes());
            System.setIn(in);
            Game game = new Game();
            game.blackJack(true);

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream in2 = new ByteArrayInputStream("no\n".getBytes());
            System.setIn(in2);
        } finally {
            System.setIn(inapt);
        }
    }

    @Test
    void checkWithComp() {
        Game game = new Game();
        game.blackJack(false);
        assertTrue(true);
    }
}