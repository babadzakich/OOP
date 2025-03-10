package ru.nsu.chuvashov.fazpizzeria.pizzalogic;

import ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza.Pizza;

import java.util.LinkedList;
import java.util.Queue;

public class SyncQueues {
    private final Queue<Pizza> orderList = new LinkedList<>();
    private final Queue<Pizza> warehouse = new LinkedList<>();
    private final int warehouseSize;

    public SyncQueues(int warehouseSize) {
        this.warehouseSize = warehouseSize;
    }

    protected synchronized void addOrder(Pizza pizza) {
        orderList.add(pizza);
        System.out.println("Заказ номер " + pizza.getId() + " добавили в очередь");
        notifyAll();
    }

    protected synchronized Pizza takeOrder() throws InterruptedException {
        while (orderList.isEmpty()) {
            if (Controller.isClosingTime()) {
                throw new InterruptedException("Время работы окончено");
            }
            System.out.println("Заказов нет, брать нечего");
            wait();
        }
        Pizza pizza = orderList.poll();
        System.out.println("Заказ номер " + pizza.getId() + " был достан из очереди");
        notifyAll();
        return pizza;
    }

    protected synchronized void addReady(Pizza pizza) throws InterruptedException {
        while (warehouse.size() == warehouseSize) {
            if (Controller.isClosingTime()) {
                throw new InterruptedException("Время работы окончено");
            }
            System.out.println("Склад полный, нечего складывать");
            wait();
        }
        warehouse.add(pizza);
        System.out.println("Пиццу номер " + pizza.getId() + " доставили на склад");
        notifyAll();
    }

    protected synchronized Pizza takeReady() throws InterruptedException {
        while (warehouse.isEmpty()) {
            if (Controller.isClosingTime()) {
                throw new InterruptedException("Время работы окончено");
            }
            System.out.println("Склад пуст, нечего брать");
            wait();
        }
        Pizza pizza = warehouse.poll();
        System.out.println("Пиццу номер " + pizza.getId() + " благополучно взяли со склада");
        notifyAll();
        return pizza;
    }
}
