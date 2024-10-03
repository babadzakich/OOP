package ru.nsu.chuvashov.expressionparser;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void mainTest() throws Exception {
        InputStream in = System.in;
        ByteArrayInputStream bais = new ByteArrayInputStream("(2 + 2) * 2\n".getBytes());
        System.setIn(bais);
        Main.main(new String[]{});
        assertTrue(true);
        System.setIn(in);
    }
}