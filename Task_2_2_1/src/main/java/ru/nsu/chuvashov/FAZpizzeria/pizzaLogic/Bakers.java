package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic;

import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.Pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bakers {
    private static Bakers instance = null;
    private final int[] times = new int[] {2_000, 4_000, 5_000, 6_000, 1_000, 8_000, 12_000, 3_000, 7_000, 1_000};
    private final Controller controller = Controller.getAlreadyInitInstance();
    private static final List<PizzaMaker> PizzaMakers = new ArrayList<>();


    public static Bakers getBakerinstance(int N) {
        if (instance == null) {
            instance = new Bakers(N);
        } else {
            throw new IllegalStateException("Bakers instance has already been created");
        }
        return instance;
    }

    private Bakers(int amountOfPizzaMakers) {
        Random rand = new Random();
        for (int i = 1; i <= amountOfPizzaMakers; i++) {
            PizzaMaker pizzaMaker = new PizzaMaker(times[rand.nextInt(times.length)], i);
            new Thread(pizzaMaker).start();
            PizzaMakers.add(pizzaMaker);
        }
    }

    public class PizzaMaker implements Runnable {
        private final int timeToMakePizza;
        private final int index;

        PizzaMaker(int timeToMakePizza, int index) {
            this.timeToMakePizza = timeToMakePizza;
            this.index = index;
        }

        @Override
        public void run() {
            while (!controller.isClosingTime()) {
                Pizza pizza;

                System.out.println("Повар номер " + index + " берёт заказ");

                try {
                    pizza = controller.takeOrder();
                } catch (InterruptedException e) {
                    return;
                }

                System.out.println("Повар номер " + index + " получил заказ номер " + pizza.getId() + " и начал его готовить");

                int cookingTime = timeToMakePizza * pizza.getQuantity();
                try {
                    Thread.sleep(cookingTime);
                    pizza.setCooked(true);
                    System.out.println("Повар номер " + index + " приготовил заказ номер " + pizza.getId() + " и пытается отправить его на склад");
                    controller.addReady(pizza);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
