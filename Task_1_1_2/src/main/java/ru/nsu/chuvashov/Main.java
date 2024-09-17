package ru.nsu.chuvashov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.Math.abs;

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
        game.blackJack();
    }
}