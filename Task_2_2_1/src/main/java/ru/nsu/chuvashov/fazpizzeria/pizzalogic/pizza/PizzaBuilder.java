package ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza;

/**
 * Builder pattern for better pizza creation.
 */
public class PizzaBuilder {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String phone;

    private PizzaBuilder() {}

    /**
     * Initializing builder.
     *
     * @return builder class.
     */
    public static PizzaBuilder createNewPizzaOrder() {
        return new PizzaBuilder();
    }

    /**
     * Id setter.
     *
     * @param id - id we want to set.
     * @return builder with our id.
     */
    public PizzaBuilder withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Name setter.
     *
     * @param name - name of pizza.
     * @return pizza with name.
     */
    public PizzaBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Price setter.
     *
     * @param price - price of pizza.
     * @return builder with new price.
     */
    public PizzaBuilder whichCosts(double price) {
        this.price = price;
        return this;
    }

    /**
     * Quantity setter.
     *
     * @param quantity - amount of pizza.
     * @return builder with certain amount.
     */
    public PizzaBuilder inQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Phone number setter.
     *
     * @param phone - number of customer.
     * @return builder with customer phone number.
     */
    public PizzaBuilder fromNumber(String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Create pizza from builder.
     *
     * @return our Pizza.
     */
    public Pizza makePizza() {
        return new Pizza(id, name, price, quantity, phone, false);
    }
}
