package ru.nsu.chuvashov.bj;

import java.util.ArrayList;
import ru.nsu.chuvashov.bj.Kards;
import ru.nsu.chuvashov.bj.Mast;

/**
 * Class for Card representation,
 * has Name, value and Mast`,
 * also we have fields for summary and aces list,
 * where we save them in other methods.
 */
class Card {

    final String mast;
    final String name;
    int value;

    /**
     * Constructor for class.
     *
     * @param mast mast` of card.
     * @param kards - card where we have name and value.
     */
    Card(Mast mast, Kards kards) {
        this.mast = mast.name;
        this.name = kards.name;
        this.value = kards.value;
    }

    /**
     * String convertor.
     *
     * @return string card representation.
     */
    public String toString() {
        return this.mast + " " + this.name + " (" + this.value + ")";
    }
}