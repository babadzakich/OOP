package ru.nsu.chuvashov.FAZpizzeria;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Pizza> pizzas = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pizzas.add(new Pizza(i, "Pepperoni", 1800, 1, "88005553535", false));
        }
        Controller controller = Controller.getInstance(1, 4, 1);
        for (Pizza pizza : pizzas) {
            controller.addOrder(pizza);
        }
    }
}
