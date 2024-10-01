package ru.nsu.chuvashov.bj;

import ru.nsu.chuvashov.bj.Game;

/**
 * Blackjack implementation.
 *
 * @author artyom.
 */
public class Main {
    /**
     * starting point for program.
     *
     * @param args empty.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.blackJack(true);
    }
}