package ru.nsu.chuvashov.bj;

/**
 * An enum for Cards representation.
 */
public enum Kards {

    ACE("Туз", 11),
    KING("Король", 10),
    QUEEN("Королева", 10),
    VALET("Валет", 10),
    TEN("Десять", 10),
    NINE("Девять", 9),
    EIGHT("Восемь", 8),
    SEVEN("Семь", 7),
    SIX("Шесть", 6),
    FIVE("Пять", 5),
    FOUR("Четыре", 4),
    THREE("Три", 3),
    TWO("Два", 2);

    private final String name;
    private final int value;

    /**
     * Constructor for Cards.
     *
     * @param name - name of card.
     * @param value - score of a card.
     */
    Kards(String name, int value) {
        this.value = value;
        this.name = name;
    }

    /**
     * Converting enum to string.
     *
     * @return string representation of cards.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * gets value.
     *
     * @return value of kard.
     */
    public int getValue() {
        return value;
    }
}