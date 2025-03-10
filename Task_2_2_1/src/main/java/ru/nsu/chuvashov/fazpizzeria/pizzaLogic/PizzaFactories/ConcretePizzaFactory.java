package ru.nsu.chuvashov.fazpizzeria.pizzaLogic.PizzaFactories;

import ru.nsu.chuvashov.fazpizzeria.pizzaLogic.Pizza.Pizza;
import ru.nsu.chuvashov.fazpizzeria.pizzaLogic.Pizza.PizzaType;

/**
 * Interface for different factories.
 */
public interface ConcretePizzaFactory {
    /**
     * Returns type of pizza, that one certain factory is producing.
     *
     * @return type of pizza.
     */
    PizzaType getPizzaType();


    /**
     * Creates fixated pizza order for testing purposes.
     *
     * @param id = id of pizza.
     * @return new pizza order.
     */
    Pizza createPizzaOrder(int id);

    /**
     * Create new pizza order from our customer.
     *
     * @param id - id of pizza.
     * @param quantity - amount of pizzas.
     * @param phone - phone number of customer.
     * @return new pizza order.
     */
    Pizza createPizzaOrder(int id, int quantity, String phone);
}
