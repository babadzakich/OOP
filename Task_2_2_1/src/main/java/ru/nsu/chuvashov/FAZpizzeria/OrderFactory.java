package ru.nsu.chuvashov.FAZpizzeria;

public class OrderFactory {
    private static int id = 1;

    public static Pizza createPizza(String type) {
        return switch (type) {
            case "fourCheese" -> new Pizza(id++, "Four cheese", 228, 1, "88005553535", false);
            case "margarita" -> new Pizza(id++, "Margarita", 1488, 1, "88005553535", false);
            case "vegetable" -> new Pizza(id++, "Vegetable", 322, 1, "88005553535", false);
            case "marsh" -> new Pizza(id++, "Marshmallow", 555, 1, "88005553535", false);
            case "hawaian" -> new Pizza(id++, "Hawaiian", 228, 1, "88005553535", false);
            default -> null;
        };
    }
}
