package ru.nsu.chuvashov.FAZpizzeria;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String[] input = new String[]{"fourCheese", "margarita", "vegetable", "marsh", "hawaian"};
        Controller controller = Controller.getInstance(2, 4, 1);
        Random random = new Random();
        while (!controller.isClosingTime()) {
            controller.addOrder(OrderFactory.createPizza(input[random.nextInt(input.length)]));
        }
    }
}
