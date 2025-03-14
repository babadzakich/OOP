package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;

/**
 * Couriers container.
 */
public class Couriers {
    private final List<Courier> couriers = new ArrayList<>();
    private final int amountOfCouriers;
    private final int[] times = new int[] {
        2_000, 4_000, 5_000, 6_000, 1_000,
        8_000, 12_000, 3_000, 7_000, 1_000
    };
    private final SyncQueues warehouse;
    private final Controller controller;

    protected Couriers(int m, SyncQueues warehouse, Controller controller) {
        this.warehouse = warehouse;
        this.controller = controller;
        amountOfCouriers = m;
        Random rand = new Random();
        for (int i = 1; i <= amountOfCouriers; i++) {
            Courier courier = new Courier(times[rand.nextInt(times.length)], i);
            new Thread(courier).start();
            couriers.add(courier);
        }
    }

    /**
     * Thread that simulates one courier.
     */
    private class Courier implements Runnable {
        private final int timeToDeliver;
        private final int index;

        Courier(int timeToMakePizza, int index) {
            this.timeToDeliver = timeToMakePizza;
            this.index = index;
        }

        @Override
        public void run() {
            while (!Controller.isClosingTime() || warehouse.getCapacity() > 0) {
                System.out.println("Курьер номер " + index + " пытается взять доставку");
                Pizza pizza;
                try {
                    pizza = warehouse.takeReady();
                } catch (InterruptedException e) {
                    if (e.getMessage().equals("Время работы окончено")) {
                        break;
                    }
                    System.err.println("Работу " + index
                            + "-го курьера прервали при попытке взять готовую пиццу!");
                    return;
                }

                System.out.println("Курьер номер " + index
                        + " забрал заказ номер " + pizza.getId() + " и начал его перевозку");

                try {
                    Thread.sleep(timeToDeliver);
                } catch (InterruptedException e) {
                    System.err.println("Работу " + index + "-го курьера прервали при доставке!");
                    return;
                }
                System.out.println("Курьер номер " + index
                        + " закночил перевозку заказа номер " + pizza.getId());
            }
            System.out.println("Курьер номер " + index
                    + " завершает работу, так как наступило время закрытия.");
        }
    }
}
