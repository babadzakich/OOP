package ru.nsu.chuvashov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.bj.Main;

class MainTest {

    @Test
    void checkMain() {
        Main.main(new String[] {});
        assertTrue(true);
    }
}