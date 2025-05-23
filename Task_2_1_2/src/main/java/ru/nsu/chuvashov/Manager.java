package ru.nsu.chuvashov;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private final List<Socket> workers;
    public Manager(int workersCount) {
        workers = new ArrayList<>(workersCount);
        String host = System.getenv("WORKERHOSTNAME");
        for (int i = 0; i < workersCount; i++) {
            try {
                workers.add(new Socket(InetAddress.getByName(host + i), 8008));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host + i);
            } catch (IOException e) {
                System.err.println("IO exception: " + e);
            }
        }
    }

    public sendRequest(int[] array) {
        
    }
}
