package ru.nsu.chuvashov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void checkMain() {
        Main.main(new String[] {});
        assertTrue(true);
    }
}