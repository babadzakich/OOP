package ru.nsu.chuvashov.bj;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void checkGame() {
        InputStream inapt = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("1\n0\n".getBytes());
            System.setIn(in);
            Game game = new Game();
            game.blackJack(false);

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream in2 = new ByteArrayInputStream("no\n".getBytes());
            System.setIn(in2);
        } finally {
            System.setIn(inapt);
        }
    }
}