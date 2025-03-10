package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import lombok.Getter;

/**
 * Controller which controls the flow of our pizzeria.
 */
public class Controller {
    @Getter private static volatile boolean closingTime = false;
    @Getter private final Bakers bakers;
    @Getter private final Couriers couriers;
    @Getter private final SyncQueues warehouse;

    /**
     * Constructor.
     *
     * @param bakersAmount - how many bakers are in pizzeria.
     * @param couriersAmount - how many couriers in pizzeria.
     * @param warehouseLimit - how many pizzas can be stored in warehouse.
     * @param pizzeriaWorkingTime - how long does pizzeria work.
     */
    public Controller(int bakersAmount, int couriersAmount,
                  int warehouseLimit, long pizzeriaWorkingTime) {
        warehouse = new SyncQueues(warehouseLimit);
        bakers = new Bakers(bakersAmount, warehouse, this);
        couriers = new Couriers(couriersAmount, warehouse, this);
        new Thread(new Timer(pizzeriaWorkingTime)).start();
    }

    /**
     * Timer, counting how long pizzeria is working.
     */
    private record Timer(long startTime) implements Runnable {
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
