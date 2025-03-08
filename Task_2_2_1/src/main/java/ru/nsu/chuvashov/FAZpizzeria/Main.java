package ru.nsu.chuvashov.FAZpizzeria;

import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.AbstractPizzaFactory;
import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Controller;

public class Main {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance(2, 4, 1);
        AbstractPizzaFactory factory = new AbstractPizzaFactory(controller, 2);
        factory.otladkaStart();
    }
}
