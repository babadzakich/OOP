package ru.nsu.chuvashov.prime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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