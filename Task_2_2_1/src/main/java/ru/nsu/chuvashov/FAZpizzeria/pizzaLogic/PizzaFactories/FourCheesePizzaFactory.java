package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.PizzaFactories;

import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.Pizza;
import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.PizzaBuilder;
import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.PizzaType;

public class FourCheesePizzaFactory implements ConcretePizzaFactory{
    private final PizzaType type = PizzaType.FourCheese;
    /**
     * Method to determine whether it`s suitable type.
     *
     * @return type of my pizza.
     */
    @Override
    public PizzaType getPizzaType() {
        return type;
    }

    /**
     * Builds new Hawaiian pizza with fixated price(in fpi coins).
     *
     * @return new Margarita pizza.
     */
    @Override
    public Pizza createPizzaOrder(int id) {
        return PizzaBuilder.CreateNewPizzaOrder().withId(id)
                .withName("Четыре сыра").inQuantity(1).whichCosts(122.8)
                .fromNumber("89835514973").makePizza();
    }

    /**
     * Create new pizza order from our customer.
     *
     * @param id       - id of pizza.
     * @param quantity - amount of pizzas.
     * @param phone    - phone number of customer.
     * @return new pizza order.
     */
    @Override
    public Pizza createPizzaOrder(int id, int quantity, String phone) {
        return PizzaBuilder.CreateNewPizzaOrder().withId(id)
                .withName("Четыре сыра").inQuantity(quantity).whichCosts(134.76)
                .fromNumber(phone).makePizza();
    }
}
