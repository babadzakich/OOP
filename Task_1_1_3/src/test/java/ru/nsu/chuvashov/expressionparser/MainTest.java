package ru.nsu.chuvashov.expressionparser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void mainTest() throws Exception {
        final InputStream in = System.in;
        ByteArrayInputStream bais = new ByteArrayInputStream("(2 + 2) * 2\n".getBytes());
        System.setIn(bais);
        Main.main(new String[]{});
        assertTrue(true);
        System.setIn(in);
    }
}