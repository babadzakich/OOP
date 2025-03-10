package ru.nsu.chuvashov.fazpizzeria;

import ru.nsu.chuvashov.fazpizzeria.pizzalogic.AbstractPizzaFactory;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.Controller;

/**
 * Main.
 */
public class Main {
    /**
     * Main.
     *
     * @param args nothing here.
     */
    public static void main(String[] args) {
        Controller controller = new Controller(4, 2, 2, 20_000);
        AbstractPizzaFactory factory = new AbstractPizzaFactory(controller, 2);
        factory.otladkaStart();
        while (!Controller.isClosingTime()) {
            Thread.onSpinWait();
        }
    }
}
