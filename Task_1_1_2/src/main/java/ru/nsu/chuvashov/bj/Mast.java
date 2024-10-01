package ru.nsu.chuvashov.bj;

/**
 * An enum for Masti of cards.
 */
public enum Mast {

    SPADES("Пики"),
    HEARTS("Черви"),
    BUBNA("Буби"),
    KRESTY("Крести");

    public final String name;

    /**
     * Constructor.
     *
     * @param name - mast` of card.
     */
    Mast(String name) {
        this.name = name;
    }

    /**
     * String Convertor.
     *
     * @return string representation of mast`.
     */
    @Override
    public String toString() {
        return name;
    }
}