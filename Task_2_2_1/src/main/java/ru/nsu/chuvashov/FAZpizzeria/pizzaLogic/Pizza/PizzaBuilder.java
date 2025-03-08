package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza;

public class PizzaBuilder {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String phone;

    private PizzaBuilder() {
    }

    public static PizzaBuilder CreateNewPizzaOrder() {
        return new PizzaBuilder();
    }

    public PizzaBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PizzaBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PizzaBuilder whichCosts(double price) {
        this.price = price;
        return this;
    }

    public PizzaBuilder inQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public PizzaBuilder fromNumber(String phone) {
        this.phone = phone;
        return this;
    }

    public Pizza makePizza() {
        return new Pizza(id, name, price, quantity, phone, false);
    }
}
