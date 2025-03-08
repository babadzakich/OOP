package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic;

import ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza.Pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Couriers {
    private final Controller controller = Controller.getAlreadyInitInstance();
    private static Couriers instance = null;
    private final List<Courier> couriers = new ArrayList<>();
    private final int amountOfCouriers;
    private final int[] times = new int[] {2_000, 4_000, 5_000, 6_000, 1_000, 8_000, 12_000, 3_000, 7_000, 1_000};

    public static Couriers getCourierInstance(int M) {
        if (instance == null) {
            instance = new Couriers(M);
        }
        return instance;
    }

    private Couriers(int M) {
        amountOfCouriers = M;
        Random rand = new Random();
        for (int i = 1; i <= amountOfCouriers; i++) {
            Courier courier = new Courier(times[rand.nextInt(times.length)], i);
            new Thread(courier).start();
            couriers.add(courier);
        }
    }

    private class Courier implements Runnable {
        private final int timeToDeliver;
        private final int index;

        Courier(int timeToMakePizza, int index) {
            this.timeToDeliver = timeToMakePizza;
            this.index = index;
        }

        @Override
        public void run() {
            while (!controller.isClosingTime()) {
                System.out.println("Курьер номер " + index + " пытается взять доставку");
                Pizza pizza;
                try {
                    pizza = controller.takeReady();
                } catch (InterruptedException e) {
                    return;
                }

                System.out.println("Курьер номер " + index + " забрал заказ номер " + pizza.getId() + " и начал его перевозку");

                try {
                    Thread.sleep(timeToDeliver);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Курьер номер " + index + " закночил перевозку заказа номер " + pizza.getId());
            }
        }
    }
}
