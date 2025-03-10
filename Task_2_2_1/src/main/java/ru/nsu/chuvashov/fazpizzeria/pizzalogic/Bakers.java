package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bakers {
    private final int[] times = new int[] {2_000, 4_000, 5_000, 6_000, 1_000, 8_000, 12_000, 3_000, 7_000, 1_000};
    private final SyncQueues warehouse;
    private final Controller controller;
    private static final List<PizzaMaker> PizzaMakers = new ArrayList<>();

    protected Bakers(int amountOfPizzaMakers, SyncQueues warehouse, Controller controller) {
        this.warehouse = warehouse;
        this.controller = controller;
        Random rand = new Random();
        for (int i = 1; i <= amountOfPizzaMakers; i++) {
            PizzaMaker pizzaMaker = new PizzaMaker(times[rand.nextInt(times.length)], i);
            new Thread(pizzaMaker).start();
            PizzaMakers.add(pizzaMaker);
        }
    }

    private class PizzaMaker implements Runnable {
        private final int timeToMakePizza;
        private final int index;

        PizzaMaker(int timeToMakePizza, int index) {
            this.timeToMakePizza = timeToMakePizza;
            this.index = index;
        }

        @Override
        public void run() {
            while (!Controller.isClosingTime()) {
                Pizza pizza;

                System.out.println("Повар номер " + index + " берёт заказ");

                try {
                    pizza = warehouse.takeOrder();
                } catch (InterruptedException e) {
                    if (e.getMessage().equals("Время работы окончено")) {
                        break;
                    }
                    System.err.println("Работу " + index + "-го повара прервали при попытке взять заказ!");
                    return;
                }

                System.out.println("Повар номер " + index + " получил заказ номер " + pizza.getId() + " и начал его готовить");

                int cookingTime = timeToMakePizza * pizza.getQuantity();
                try {
                    Thread.sleep(cookingTime);
                    pizza.setCooked(true);
                    System.out.println("Повар номер " + index + " приготовил заказ номер " + pizza.getId() + " и пытается отправить его на склад");
                    warehouse.addReady(pizza);
                } catch (InterruptedException e) {
                    if (e.getMessage().equals("Время работы окончено")) {
                        break;
                    }
                    System.err.println("Работу " + index + "-го повара прервали при попытке отдать готовую пиццу!");
                    return;
                }
            }
            System.out.println("Повар номер " + index + " завершает работу, так как наступило время закрытия.");
        }
    }
}
