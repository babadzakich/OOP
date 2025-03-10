package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import java.util.Arrays;
import java.util.Random;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.PizzaType;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizzafactories.*;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;

/**
 * Abstract factory pattern for implementing adding orders.
 */
public class AbstractPizzaFactory {
    private int id = 0;
    private final ConcretePizzaFactory[] factories = new ConcretePizzaFactory[] {
        new MargaritaPizzaFactory(), new HawaiianPizzaFactory(), new MarshmallowPizzaFactory(),
        new FourCheesePizzaFactory(), new VegetarianPizzaFactory()
    };
    private final int maxCapacity;
    private final Controller controller;
    private final SyncQueues warehouse;

    public AbstractPizzaFactory(Controller controller, int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.controller = controller;
        warehouse = controller.getWarehouse();
    }

    /**
     * Method for client to place an order for pizza in queue.
     * If there are more pizzas in order than limit, than order is separated.
     *
     * @param pizzaType - what pizza customer wants.
     * @param phone - phone number of customer.
     * @param quantity - amount of pizzas
     */
    public void placeOrder(PizzaType pizzaType, String phone, int quantity) {
        var pizzaFactory = Arrays.stream(factories).filter(x -> x.getPizzaType().equals(pizzaType)).findFirst().get();
        while (quantity > maxCapacity) {
            warehouse.addOrder(pizzaFactory.createPizzaOrder(id++, quantity, phone));
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
    private Pizza CreatePizza(PizzaType pizzaType)
    {
        var pizzaFactory = Arrays.stream(factories)
                .filter(x -> x.getPizzaType() == pizzaType)
                .findFirst().get();
        return pizzaFactory.createPizzaOrder(id++);
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
            warehouse.addOrder(CreatePizza(input[random.nextInt(input.length)]));
        }
    }

}
