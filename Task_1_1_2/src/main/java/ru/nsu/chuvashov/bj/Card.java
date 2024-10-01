package ru.nsu.chuvashov.bj;

import ru.nsu.chuvashov.bj.Kards;
import ru.nsu.chuvashov.bj.Mast;

/**
 * Class for Card representation,
 * has Name, value and Mast`,
 * also we have fields for summary and aces list,
 * where we save them in other methods.
 */
class Card {

    private final String mast;
    private final String name;
    private int value;

    /**
     * Constructor for class.
     *
     * @param mast mast` of card.
     * @param kards - card where we have name and value.
     */
    public Card(Mast mast, Kards kards) {
        this.mast = mast.name;
        this.name = kards.toString();
        this.value = kards.getValue();
    }

    /**
     * String convertor.
     *
     * @return string card representation.
     */
    @Override
    public String toString() {
        return this.mast + " " + this.name + " (" + this.value + ")";
    }

    /**
     * Returns value of var.
     *
     * @return value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Setter.
     *
     * @param value - value we want our card to have
     */
    public void setValue(int value) {
        this.value = value;
    }
}