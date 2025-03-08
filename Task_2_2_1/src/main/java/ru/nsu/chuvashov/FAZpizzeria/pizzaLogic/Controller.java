package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic;

import lombok.Getter;


public class Controller {
    @Getter
    private static volatile boolean closingTime = false;
    @Getter private final Bakers bakers;
    @Getter private final Couriers couriers;
    @Getter private final SyncQueues warehouse;

    public Controller(int BakersAmount, int CouriersAmount, int WarehouseLimit) {
        warehouse = new SyncQueues(WarehouseLimit);
        bakers = new Bakers(BakersAmount, warehouse, this);
        couriers = new Couriers(CouriersAmount, warehouse, this);
        new Thread(new Timer()).start();
    }

    private static class Timer implements Runnable {
        @Override
        public synchronized void run() {
            try {
                wait(60_000);
            } catch (InterruptedException e) {
                System.err.println("Работу пиццерии невовремя прервали");
            } finally {
                closingTime = true;
            }
        }
    }
}
