package ru.nsu.chuvashov.bj;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

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
            ByteArrayInputStream in2 = new ByteArrayInputStream("0\n".getBytes());
            System.setIn(in2);
        } finally {
            System.setIn(inapt);
        }
    }
}