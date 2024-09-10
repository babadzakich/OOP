package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }


    public enum Mast {
        SPADES("Пики"),
        HEARTS("Черви"),
        BUBNA("Буби"),
        KRESTY("Крести");

        String name;

        Mast(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

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

        String name;
        int value;

        Kards(String name,int value) {
            this.value = value;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}

