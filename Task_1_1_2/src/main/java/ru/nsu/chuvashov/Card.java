package ru.nsu.chuvashov;

import java.util.ArrayList;

/**
 * Class for Card representation,
 * has Name, value and Mast`,
 * also we have fields for summary and aces list,
 * where we save them in other methods.
 */
class Card {

    String mast;
    String name;
    int value;
    int summary;
    ArrayList<Integer> aces;

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
        this.summary = this.value;
        this.aces = new ArrayList<>();
    }

    /**
     * String convertor
     *
     * @return string card representation.
     */
    public String toString() {
        return this.mast + " " + this.name + " (" + this.value + ")";
    }
}