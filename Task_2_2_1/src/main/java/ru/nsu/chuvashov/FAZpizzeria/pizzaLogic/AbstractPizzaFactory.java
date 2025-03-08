package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic;

import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.PizzaType;
import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.PizzaFactories.*;
import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.Pizza;

import java.util.Arrays;
import java.util.Random;

public class AbstractPizzaFactory
{
    private int id = 0;
    private final ConcretePizzaFactory[] _factories = new ConcretePizzaFactory[]{
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

    public void placeOrder(PizzaType pizzaType, String phone, int quantity) {
        var pizzaFactory = Arrays.stream(_factories).filter(x -> x.getPizzaType().equals(pizzaType)).findFirst().get();
        while (quantity > maxCapacity) {
            warehouse.addOrder(pizzaFactory.createPizzaOrder(id++, quantity, phone));
            quantity -= maxCapacity;
        }
        if (quantity > 0) {
            warehouse.addOrder(pizzaFactory.createPizzaOrder(id++, quantity, phone));
        }
    }

    private Pizza CreatePizza(PizzaType pizzaType)
    {
        var pizzaFactory = Arrays.stream(_factories).filter(x -> x.getPizzaType() == pizzaType).findFirst().get();
        return pizzaFactory.createPizzaOrder(id++);
    }

    public void otladkaStart() {
        PizzaType[] input = new PizzaType[]{
                PizzaType.Margarita, PizzaType.FourCheese, PizzaType.Marshmallow,
                PizzaType.Vegetarian, PizzaType.Hawaiian};
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            warehouse.addOrder(CreatePizza(input[random.nextInt(input.length)]));
        }
    }

}
