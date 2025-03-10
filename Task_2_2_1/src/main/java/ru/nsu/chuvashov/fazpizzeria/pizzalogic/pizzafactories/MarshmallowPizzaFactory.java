package ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizzafactories;

import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.PizzaBuilder;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.PizzaType;

public class MarshmallowPizzaFactory implements ConcretePizzaFactory{
    private final PizzaType type = PizzaType.Marshmallow;
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
        return PizzaBuilder.createNewPizzaOrder().withId(id)
                .withName("Зефирная").inQuantity(1).whichCosts(134.76)
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
        return PizzaBuilder.createNewPizzaOrder().withId(id)
                .withName("Зефирная").inQuantity(quantity).whichCosts(288.1)
                .fromNumber(phone).makePizza();
    }
}
