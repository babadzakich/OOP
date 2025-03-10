package ru.nsu.chuvashov.fazpizzeria;

import ru.nsu.chuvashov.fazpizzeria.pizzaLogic.AbstractPizzaFactory;
import ru.nsu.chuvashov.fazpizzeria.pizzaLogic.Controller;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(4, 2, 2, 20_000);
        AbstractPizzaFactory factory = new AbstractPizzaFactory(controller, 2);
        factory.otladkaStart();
        while (!Controller.isClosingTime()) {
            Thread.onSpinWait();
        }
    }
}
