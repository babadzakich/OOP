package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import java.util.*;

import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.PizzaType;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizzafactories.*;

/**
 * Abstract factory pattern for implementing adding orders.
 */
public class PizzaFactory {
    private int id = 0;
    private final Map<PizzaType, ConcretePizzaFactory> factories = new HashMap<>(){{
        put(PizzaType.Margarita, new MargaritaPizzaFactory());
        put(PizzaType.Hawaiian, new HawaiianPizzaFactory());
        put(PizzaType.Marshmallow, new MarshmallowPizzaFactory());
        put(PizzaType.FourCheese, new FourCheesePizzaFactory());
        put(PizzaType.Vegetarian, new VegetarianPizzaFactory());
    }};
    private final int maxCapacity;
    private final Controller controller;
    private final SyncQueues warehouse;

    /**
     * Constructor.
     *
     * @param controller - our pizzeria controller.
     * @param maxCapacity - how many pizzas can be in one order.
     */
    public PizzaFactory(Controller controller, int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.controller = controller;
        warehouse = controller.getWarehouse();
    }

    /**
     * Method for client to place an order for pizza in queue.
     * If there are more pizzas in order than limit,
     * than order is separated.
     *
     * @param pizzaType - what pizza customer wants.
     * @param phone - phone number of customer.
     * @param quantity - amount of pizzas
     */
    public void placeOrder(PizzaType pizzaType, String phone, int quantity) {
        var pizzaFactory = factories.get(pizzaType);
        while (quantity > maxCapacity) {
            warehouse.addOrder(pizzaFactory
                    .createPizzaOrder(id++, quantity, phone));
            quantity -= maxCapacity;
        }
        if (quantity > 0) {
            warehouse.addOrder(pizzaFactory.createPizzaOrder(id++, quantity, phone));
        }
    }

    /**
     * Method to create fixated pizza from given type.
     *
     * @param pizzaType type we want.
     * @return new pizza.
     */
    private Pizza createPizza(PizzaType pizzaType) {
        return factories.get(pizzaType).createPizzaOrder(id++);
    }

    /**
     * Special method to simulate generation of orders.
     */
    public void otladkaStart() {
        PizzaType[] input = new PizzaType[]{
            PizzaType.Margarita, PizzaType.FourCheese, PizzaType.Marshmallow,
            PizzaType.Vegetarian, PizzaType.Hawaiian};
        Random random = new Random();
        for (int i = 0; i < 100 && !Controller.isClosingTime(); i++) {
            warehouse.addOrder(createPizza(input[random.nextInt(input.length)]));
        }
    }

}
