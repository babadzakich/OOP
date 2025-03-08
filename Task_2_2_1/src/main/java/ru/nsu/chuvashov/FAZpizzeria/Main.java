package ru.nsu.chuvashov.FAZpizzeria;

import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.AbstractPizzaFactory;
import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Controller;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(4, 2, 2);
        AbstractPizzaFactory factory = new AbstractPizzaFactory(controller, 2);
        factory.otladkaStart();
        while (!Controller.isClosingTime()) {
            Thread.onSpinWait();
        }
    }
}
