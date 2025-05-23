package ru.nsu.chuvashov;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Worker implements Runnable {
    private final Inet4Address managerIp;
    private final int managerPort;
    private Socket managerSocket;
    private DataOutputStream managerOutput;
    private DataInputStream managerInput;

    private final BlockingQueue<JSONObject> taskQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final ScheduledExecutorService heartbeatExecutor;
    private final ExecutorService taskExecutor;

    public Worker(Inet4Address managerIp) {
        this.managerIp = managerIp;
        this.managerPort = Integer.parseInt(System.getenv("MANAGER_PORT"));
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        this.taskExecutor = Executors.newSingleThreadExecutor();
    }

    public boolean connectToManager() {
        try {
            managerSocket = new Socket(managerIp, managerPort);
            managerOutput = new DataOutputStream(managerSocket.getOutputStream());
            managerInput = new DataInputStream(managerSocket.getInputStream());
            System.out.println("Connected to manager at " + managerIp + ":" + managerPort);

            startHeartbeat();

            startTaskProcessor();

            return true;
        } catch (IOException e) {
            System.err.println("Failed to connect to manager: " + e.getMessage());
            return false;
        }
    }

    private void startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            if (isRunning.get()) {
                try {
                    sendHeartBeat();
                } catch (IOException e) {
                    System.err.println("Failed to send heartbeat: " + e.getMessage());
                    handleConnectionError();
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void sendHeartBeat() throws IOException {
        JSONObject heartbeat = new JSONObject();
        heartbeat.put("type", "heartbeat");
        heartbeat.put("timestamp", System.currentTimeMillis());
        heartbeat.put("worker_id", System.getenv("WORKER_ID"));

        sendMessageToManager(heartbeat.toString());
        System.out.println("Heartbeat sent to manager");
    }

    private void sendMessageToManager(String message) throws IOException {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        managerOutput.writeInt(messageBytes.length);
        managerOutput.write(messageBytes);
        managerOutput.flush();
    }

    private void startTaskProcessor() {
        taskExecutor.submit(() -> {
           while (isRunning.get()) {
               try {
                   JSONObject task = taskQueue.take();
                   processTask(task);
               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
                   break;
               } catch (Exception e) {
                   System.err.println("Error processing task: " + e.getMessage());
               }
           }
        });
    }

    @Override
    public void run() {
        System.out.println("Worker started, listening for data from manager...");

        while (isRunning.get()) {
            try {
                String receivedData = receiveMessageFromManager();
                if (receivedData != null) {
                    handleReceivedData(receivedData);
                }
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("Connection error: " + e.getMessage());
                    handleConnectionError();
                }
                break;
            }
        }
    }

    private String receiveMessageFromManager() throws IOException {
        try {
            int messageLength = managerInput.readInt();

            if (messageLength <= 0 || messageLength > 10 * 1024 * 1024) { // Max 10MB
                throw new IOException("Invalid message length: " + messageLength);
            }

            byte[] messageBytes = new byte[messageLength];
            managerInput.readFully(messageBytes);

            return new String(messageBytes, StandardCharsets.UTF_8);
        } catch (EOFException e) {
            System.out.println("Manager closed connection");
            return null;
        }
    }

    private void handleReceivedData(String data) {
        try {
            JSONObject json = new JSONObject(data);
            String type = json.optString("type", "unknown");

            System.out.println("Received data from manager: " + type);

            switch (type) {
                case "task":
                    // Add task to queue for processing
                    taskQueue.offer(json);
                    break;
                case "shutdown":
                    System.out.println("Received shutdown command from manager");
                    shutdown();
                    break;
                case "ping":
                    handlePing(json);
                    break;
                default:
                    System.out.println("Unknown message type: " + type);
            }

        } catch (JSONException e) {
            System.err.println("Invalid JSON received: " + e.getMessage());
            System.err.println("Raw data: " + data);
        }
    }

    private void processTask(JSONObject task) {
        try {
            String method = task.optString("method", "unknown");
            String taskId = task.optString("task_id", "unknown");

            System.out.println("Processing task: " + taskId + " with method: " + method);

            JSONObject result = new JSONObject();
            result.put("type", "task_result");
            result.put("task_id", taskId);

            if (method.equals("calculate")) {
                handleCalculateTask(task, result);
            } else {
                result.put("status", "error");
                result.put("message", "unknown method: " + method);
            }

            // Send result back to manager
            sendMessageToManager(result.toString());

        } catch (Exception e) {
            System.err.println("Error processing task: " + e.getMessage());
            try {
                JSONObject errorResult = new JSONObject();
                errorResult.put("type", "task_result");
                errorResult.put("task_id", task.optString("task_id", "unknown"));
                errorResult.put("status", "error");
                errorResult.put("message", "Processing error: " + e.getMessage());
                sendMessageToManager(errorResult.toString());
            } catch (IOException ioException) {
                System.err.println("Failed to send error result: " + ioException.getMessage());
            }
        }
    }

    private void handleCalculateTask(JSONObject task, JSONObject result) {
        try {
            JSONArray dataArray = task.getJSONArray("data");
            int[] data = new int[dataArray.length()];

            for (int i = 0; i < dataArray.length(); i++) {
                data[i] = dataArray.getInt(i);
            }

            boolean hasNonPrime = performCalculation(data);

            result.put("status", "success");
            result.put("result", hasNonPrime);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Calculation error: " + e.getMessage());
        }
    }

    private void handlePing(JSONObject ping) {
        try {
            JSONObject pong = new JSONObject();
            pong.put("type", "pong");
            pong.put("timestamp", System.currentTimeMillis());
            pong.put("ping_id", ping.optString("ping_id", "unknown"));
            sendMessageToManager(pong.toString());
        } catch (IOException e) {
            System.err.println("Failed to send pong: " + e.getMessage());
        }
    }

    private boolean performCalculation(int[] data) {
        for (int number : data) {
            if (IsNotPrime.isNotPrime(number)) {
                return true;
            }
        }
        return false;
    }

    private void handleConnectionError() {
        System.err.println("Connection to manager lost. Attempting to reconnect...");

        closeConnection();

        // Try to reconnect
        int attempts = 0;
        while (isRunning.get() && attempts < 5) {
            try {
                Thread.sleep(5000); // Wait 5 seconds before retry
                if (connectToManager()) {
                    System.out.println("Reconnected to manager successfully");
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            attempts++;
        }

        System.err.println("Failed to reconnect to manager after " + attempts + " attempts");
        shutdown();
    }

    private void closeConnection() {
        try {
            if (managerOutput != null) managerOutput.close();
            if (managerInput != null) managerInput.close();
            if (managerSocket != null && !managerSocket.isClosed()) managerSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public void shutdown() {
        System.out.println("Shutting down worker...");
        isRunning.set(false);

        if (heartbeatExecutor != null && !heartbeatExecutor.isShutdown()) {
            heartbeatExecutor.shutdown();
        }

        if (taskExecutor != null && !taskExecutor.isShutdown()) {
            taskExecutor.shutdown();
        }

        closeConnection();

        System.out.println("Worker shutdown complete");
    }

    public static class IsNotPrime {
        /**
         * Checker for primeness.
         *
         * @param number - to check.
         * @return true if number is not prime.
         */
        public static boolean isNotPrime(int number) {
            if ((number % 2 == 0 && number != 2) || Math.abs(number) < 2) {
                return false;
            }
            for (int i = 3; i * i <= number; i += 2) {
                if (number % i == 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
