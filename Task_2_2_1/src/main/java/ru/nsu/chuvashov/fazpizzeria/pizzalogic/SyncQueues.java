package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import java.util.LinkedList;
import java.util.Queue;
import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;

/**
 * Synchronized queues to prevent race conditions.
 */
public class SyncQueues {
    private final Queue<Pizza> orderList = new LinkedList<>();
    private final Queue<Pizza> warehouse = new LinkedList<>();
    private final int warehouseSize;

    public SyncQueues(int warehouseSize) {
        this.warehouseSize = warehouseSize;
    }

    /**
     * Add order on pizza.
     *
     * @param pizza - order.
     */
    protected synchronized void addOrder(Pizza pizza) {
        orderList.add(pizza);
        System.out.println("Заказ номер " + pizza.getId() + " добавили в очередь");
        notifyAll();
    }

    /**
     * Take order to cook pizza.
     *
     * @return order.
     * @throws InterruptedException if time is up.
     */
    protected synchronized Pizza takeOrder() throws InterruptedException {
        while (orderList.isEmpty()) {
            if (Controller.isClosingTime()) {
                throw new InterruptedException("Время работы окончено");
            }
            System.out.println("Заказов нет, брать нечего");
            wait(1_000);
        }
        Pizza pizza = orderList.poll();
        System.out.println("Заказ номер " + pizza.getId() + " был достан из очереди");
        notifyAll();
        return pizza;
    }

    /**
     * Add ready pizza to warehouse if able.
     *
     * @param pizza - to add.
     * @throws InterruptedException if time is up.
     */
    protected synchronized void addReady(Pizza pizza) throws InterruptedException {
        while (warehouse.size() == warehouseSize) {
            if (Controller.isClosingTime()) {
                throw new InterruptedException("Время работы окончено");
            }
            System.out.println("Склад полный, нечего складывать");
            wait(1_000);
        }
        warehouse.add(pizza);
        System.out.println("Пиццу номер " + pizza.getId() + " доставили на склад");
        notifyAll();
    }

    /**
     * Couriers take pizza from warehouse to deliver.
     *
     * @return pizza to deliver.
     * @throws InterruptedException if time is up.
     */
    protected synchronized Pizza takeReady() throws InterruptedException {
        while (warehouse.isEmpty()) {
            if (Controller.isClosingTime()) {
                throw new InterruptedException("Время работы окончено");
            }
            System.out.println("Склад пуст, нечего брать");
            wait(1_000);
        }
        Pizza pizza = warehouse.poll();
        System.out.println("Пиццу номер " + pizza.getId() + " благополучно взяли со склада");
        notifyAll();
        return pizza;
    }
}
