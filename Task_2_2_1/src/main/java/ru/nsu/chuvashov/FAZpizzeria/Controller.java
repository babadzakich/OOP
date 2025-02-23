package ru.nsu.chuvashov.FAZpizzeria;

import java.util.LinkedList;
import java.util.Queue;
import lombok.Getter;
import lombok.Setter;

public class Controller {
    private final Queue<Pizza> queue = new LinkedList<>();
    private final Queue<Pizza> ready = new LinkedList<>();
    private final int readyLimit;
    @Getter @Setter
    private volatile boolean closingTime = false;

    private static Controller instance = null;
    public static Controller getInstance(int readyLimit, int bakersLimit, int couriersLimit) {
        if (instance == null) {
            instance = new Controller(readyLimit);
            Bakers.getBakerinstance(bakersLimit);
            Couriers.getCourierInstance(couriersLimit);
        }
        return instance;
    }

    public static Controller getAlreadyInitInstance() {
        if (instance == null) {
            throw new IllegalStateException("Controller not initialized");
        }
        return instance;
    }

    private Controller(int readyLimits) {
        this.readyLimit = readyLimits;
        new Thread(new Timer()).start();
    }

    public synchronized void addOrder(Pizza pizza) {
        queue.add(pizza);
        notifyAll();
    }

    public synchronized Pizza takeOrder() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        Pizza pizza = queue.poll();
        notifyAll();
        return pizza;
    }

    public synchronized void addReady(Pizza pizza) throws InterruptedException {
        while (ready.size() == readyLimit) {
            wait();
        }
        ready.add(pizza);
        notifyAll();
    }

    public synchronized Pizza takeReady() throws InterruptedException {
        while (ready.isEmpty()) {
            wait();
        }
        Pizza pizza = ready.poll();
        notifyAll();
        return pizza;
    }

    private class Timer implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            closingTime = true;
        }
    }
}
