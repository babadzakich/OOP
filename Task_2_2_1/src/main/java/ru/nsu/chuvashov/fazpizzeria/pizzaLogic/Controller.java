package ru.nsu.chuvashov.fazpizzeria.pizzaLogic;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Controller which controls the flow of our pizzeria.
 */
public class Controller {
    @Getter private static volatile boolean closingTime = false;
    @Getter private final Bakers bakers;
    @Getter private final Couriers couriers;
    @Getter private final SyncQueues warehouse;

    public Controller(int bakersAmount, int couriersAmount, int warehouseLimit, long pizzeriaWorkingTime) {
        warehouse = new SyncQueues(warehouseLimit);
        bakers = new Bakers(bakersAmount, warehouse, this);
        couriers = new Couriers(couriersAmount, warehouse, this);
        new Thread(new Timer(pizzeriaWorkingTime)).start();
    }

    /**
     * Timer, counting how long pizzeria is working
     */
    @AllArgsConstructor
    private static class Timer implements Runnable {
        private final long startTime;
        @Override
        public synchronized void run() {
            try {
                wait(startTime);
            } catch (InterruptedException e) {
                System.err.println("Работу пиццерии невовремя прервали");
            } finally {
                closingTime = true;
            }
        }
    }
}
