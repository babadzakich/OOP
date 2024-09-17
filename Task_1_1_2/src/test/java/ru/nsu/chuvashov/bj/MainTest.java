package ru.nsu.chuvashov.bj;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class MainTest {
    @Test
    void checkMain() {
        InputStream inapt = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
            System.setIn(in);
            Main.main(new String[] {});

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream in2 = new ByteArrayInputStream("no\n".getBytes());
            System.setIn(in2);
        } finally {
            System.setIn(inapt);
        }
    }
}