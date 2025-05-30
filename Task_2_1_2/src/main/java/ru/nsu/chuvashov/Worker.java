package ru.nsu.chuvashov;

import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Calculation node class.
 */
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

    /**
     * Constructor.
     *
     * @throws UnknownHostException if cant find host.
     */
    public Worker() throws UnknownHostException {
        this.managerIp = (Inet4Address) Inet4Address.getByName(
                System.getenv().getOrDefault("MANAGER_HOST", "localhost")
        );
        this.managerPort = Integer.parseInt(
                System.getenv().getOrDefault("MANAGER_PORT", "8080")
        );
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        this.taskExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Logic to connect to manager.
     *
     * @return true if connected.
     */
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

    /**
     * Start heartbeat once in a 10 seconds.
     */
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
        }, 0, 10, TimeUnit.SECONDS);
    }

    /**
     * Creates json and sends heartbeat to manager.
     *
     * @throws IOException if can`t send heartbeat.
     */
    private void sendHeartBeat() throws IOException {
        JSONObject heartbeat = new JSONObject();
        heartbeat.put("type", TaskType.HEARTBEAT.toString());
        heartbeat.put("timestamp", System.currentTimeMillis());
        heartbeat.put("worker_id", System.getenv("WORKER_ID"));

        sendMessageToManager(heartbeat.toString());
        System.out.println("Heartbeat sent to manager");
    }

    /**
     * Sends message to manager.
     * First it sends length of message and secondly the message.
     *
     * @param message - what to send.
     * @throws IOException - if can`t send.
     */
    private void sendMessageToManager(String message) throws IOException {
        synchronized (managerOutput) {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            managerOutput.writeInt(messageBytes.length);
            managerOutput.write(messageBytes);
            managerOutput.flush();
        }
    }

    /**
     * Takes task from queue and starts processing it.
     */
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
                } else {
                    break;
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

    /**
     * Gets message from manager.
     *
     * @return message itself.
     * @throws IOException if can`t read from socket.
     */
    private String receiveMessageFromManager() throws IOException {
        try {
            int messageLength = managerInput.readInt();

            if (messageLength <= 0 || messageLength > 10 * 1024 * 1024) {
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

    /**
     * Parses string to json and handles it.
     *
     * @param data - string to parse.
     */
    private void handleReceivedData(String data) {
        try {
            JSONObject json = new JSONObject(data);
            TaskType type = TaskType.valueOf(json.optString("type", "unknown"));

            System.out.println("Received data from manager: " + type);

            switch (type) {
                case TASK:
                    taskQueue.offer(json);
                    break;
                case SHUTDOWN:
                    System.out.println("Received shutdown command from manager");
                    shutdown();
                    break;
                case PING:
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

    /**
     * Processes task.
     *
     * @param task - task json.
     */
    private void processTask(JSONObject task) {
        try {
            String method = task.optString("method", "unknown");
            String taskId = task.optString("task_id", "unknown");

            System.out.println("Processing task: " + taskId + " with method: " + method);

            JSONObject result = new JSONObject();
            result.put("type", TaskType.TASKRESULT.toString());
            result.put("task_id", taskId);

            if (method.equals("calculate")) {
                handleCalculateTask(task, result);
            } else {
                result.put("status", "error");
                result.put("message", "unknown method: " + method);
            }

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
                connectToManager();
            }
        }
    }

    /**
     * Finds nonprime for given data.
     *
     * @param task - task to process.
     * @param result - where to store result.
     */
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

    /**
     * Sends pong responce.
     *
     * @param ping - ping json.
     */
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

    /**
     * Finds nonprime.
     *
     * @param data - where to find.
     * @return true if found nonprime.
     */
    private boolean performCalculation(int[] data) {
        for (int number : data) {
            if (IsNotPrime.isNotPrime(number)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to reconnect if connection lost.
     */
    private void handleConnectionError() {
        System.err.println("Connection to manager lost. Attempting to reconnect...");

        closeConnection();

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

    /**
     * Closes connection.
     */
    private void closeConnection() {
        try {
            if (managerOutput != null) {
                managerOutput.close();
            }
            if (managerInput != null) {
                managerInput.close();
            }
            if (managerSocket != null && !managerSocket.isClosed()) {
                managerSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Shutdown logic to turn off worker.
     */
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

    /**
     * Class for prime calculation.
     */
    public static class IsNotPrime {
        /**
         * Checker for primeness.
         *
         * @param number - to check.
         * @return true if number is not prime.
         */
        public static boolean isNotPrime(int number) {
            if ((number % 2 == 0 && number != 2) || Math.abs(number) < 2) {
                return true;
            }
            for (int i = 3; i * i <= number; i += 2) {
                if (number % i == 0) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Starter of worker.
     *
     * @param args - nothing here.
     */
    public static void main(String[] args) {
        try {
            Worker worker = new Worker();

            if (worker.connectToManager()) {
                Thread workerThread = new Thread(worker);
                workerThread.start();

                Runtime.getRuntime().addShutdownHook(new Thread(worker::shutdown));

                try {
                    workerThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.err.println("Failed to connect to manager. Exiting...");
            }

        } catch (Exception e) {
            System.err.println("Failed to start worker: " + e.getMessage());
        }
    }
}
