package ru.nsu.chuvashov;

/**
 * An enum for Masti of cards.
 */
enum Mast {

    SPADES("Пики"),
    HEARTS("Черви"),
    BUBNA("Буби"),
    KRESTY("Крести");

    final String name;

    /**
     * Constructor
     *
     * @param name - mast` of card.
     */
    Mast(String name) {
        this.name = name;
    }

    /**
     * @return string representation of mast`.
     */
    public String toString() {
        return name;
    }
}