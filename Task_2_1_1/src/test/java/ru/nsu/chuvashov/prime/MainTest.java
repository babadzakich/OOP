package ru.nsu.chuvashov.prime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void mainTest() {
        try {
            Main.main(new String[]{});
        } catch (Exception e) {
            fail();
        }
        assertTrue(true);
    }
}