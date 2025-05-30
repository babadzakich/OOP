package ru.nsu.chuvashov;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manager class.
 */
public class Manager {
    private final int port;
    private final Map<String, WorkerConnection> workers = new ConcurrentHashMap<>();
    private final Map<String, TaskInfo> pendingTasks = new ConcurrentHashMap<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final AtomicLong taskIdCounter = new AtomicLong(0);

    private ServerSocket serverSocket;
    private final ExecutorService workerHandlerExecutor = Executors.newCachedThreadPool();
    private final ScheduledExecutorService maintenanceExecutor
            = Executors.newScheduledThreadPool(2);

    /**
     * Constructor.
     */
    public Manager() {
        this.port = Integer.parseInt(System.getenv().getOrDefault("MANAGER_PORT", "8080"));
    }

    /**
     * Initializing work and get new workers.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Manager started on port " + port);

            startMaintenanceTasks();

            while (isRunning.get()) {
                try {
                    Socket workerSocket = serverSocket.accept();
                    System.out.println("New worker connection from: "
                            + workerSocket.getInetAddress());

                    workerHandlerExecutor.submit(
                            () -> handleWorkerConnection(workerSocket)
                    );

                } catch (IOException e) {
                    if (isRunning.get()) {
                        System.err.println("Error accepting worker connection: "
                                + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to start manager: " + e.getMessage());
        }
    }

    /**
     * Initializes executors to check for some stuff.
     */
    private void startMaintenanceTasks() {
        maintenanceExecutor.scheduleAtFixedRate(this::checkWorkerHealth, 30, 30, TimeUnit.SECONDS);

        maintenanceExecutor.scheduleAtFixedRate(this::checkPendingTasks, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * Handles new worker connection.
     *
     * @param workerSocket - new worker socket
     */
    private void handleWorkerConnection(Socket workerSocket) {
        String workerId = null;
        WorkerConnection workerConn = null;

        try {
            DataInputStream input = new DataInputStream(workerSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(workerSocket.getOutputStream());

            while (isRunning.get() && !workerSocket.isClosed()) {
                String message = receiveMessage(input);
                if (message == null) {
                    break;
                }

                JSONObject json = new JSONObject(message);
                TaskType messageType = TaskType.valueOf(
                        json.optString("type", "unknown")
                );

                switch (messageType) {
                    case HEARTBEAT:
                        workerId = json.optString("worker_id", "unknown");
                        if (workerConn == null) {
                            workerConn = new WorkerConnection(workerId, workerSocket, input, output);
                            workers.put(workerId, workerConn);
                            System.out.println("Worker registered: " + workerId);
                        }
                        workerConn.updateLastHeartbeat();
                        System.out.println("Heartbeat received from worker: " + workerId);
                        break;

                    case TASKRESULT:
                        handleTaskResult(json);
                        break;

                    case PONG:
                        if (workerConn != null) {
                            workerConn.updateLastHeartbeat();
                        }
                        System.out.println("Pong received from worker: " + workerId);
                        break;

                    default:
                        System.out.println("Unknown message type from worker: " + messageType);
                }
            }

        } catch (Exception e) {
            System.err.println("Error handling worker connection: " + e.getMessage());
        } finally {
            if (workerId != null && workerConn != null) {
                workers.remove(workerId);
                System.out.println("Worker disconnected: " + workerId);

                redistributeWorkerTasks(workerId);
            }

            try {
                workerSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing worker socket: " + e.getMessage());
            }
        }
    }

    /**
     * Receives message from worker.
     *
     * @param input - from where to receive.
     * @return message.
     * @throws IOException if can`t read from socket.
     */
    private String receiveMessage(DataInputStream input) throws IOException {
        try {
            int messageLength = input.readInt();

            if (messageLength <= 0 || messageLength > 10 * 1024 * 1024) {
                throw new IOException("Invalid message length: " + messageLength);
            }

            byte[] messageBytes = new byte[messageLength];
            input.readFully(messageBytes);

            return new String(messageBytes, StandardCharsets.UTF_8);
        } catch (EOFException e) {
            return null;
        }
    }

    /**
     * First sends length of message and then message.
     *
     * @param output - where to send.
     * @param message - what to end.
     * @throws IOException if can`t send.
     */
    private void sendMessage(DataOutputStream output, String message) throws IOException {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        output.writeInt(messageBytes.length);
        output.write(messageBytes);
        output.flush();
    }

    /**
     * Creates new task and distributes it.
     *
     * @param data - what to send.
     * @return task id.
     */
    public String submitTask(int[] data) {
        if (workers.isEmpty()) {
            throw new RuntimeException("No workers available");
        }
        String taskId = "task_" + taskIdCounter.get();
        for (int i = 0; i < workers.size(); i++) {

            JSONObject task = new JSONObject();
            task.put("type", TaskType.TASK.toString());
            task.put("task_id", taskId);
            task.put("method", "calculate");

            JSONArray dataArray = new JSONArray();
            for (int j = i; j < data.length; j += workers.size()) {
                dataArray.put(data[j]);
            }
            task.put("data", dataArray);

            WorkerConnection selectedWorker = selectWorker();
            if (selectedWorker == null) {
                throw new RuntimeException("No available workers");
            }

            try {
                sendMessage(selectedWorker.output, task.toString());

                TaskInfo taskInfo = new TaskInfo(taskId,
                        selectedWorker.workerId, System.currentTimeMillis());
                pendingTasks.put(taskId, taskInfo);
                selectedWorker.incrementActiveTasks();

                System.out.println("Task " + taskId
                        + " sent to worker " + selectedWorker.workerId);

            } catch (IOException e) {
                System.err.println("Failed to send task to worker: " + e.getMessage());
                throw new RuntimeException("Failed to send task to worker", e);
            }
        }
        return taskId;
    }

    /**
     * Chooses a worker with minimal payload.
     *
     * @return worker.
     */
    private WorkerConnection selectWorker() {
        return workers.values().stream()
                .filter(w -> w.isHealthy())
                .min(Comparator.comparingInt(w -> w.activeTasks))
                .orElse(null);
    }

    /**
     * Parses result of calculation.
     *
     * @param result what to parse.
     */
    private void handleTaskResult(JSONObject result) {
        String taskId = result.optString("task_id", "unknown");
        TaskInfo taskInfo = pendingTasks.remove(taskId);

        if (taskInfo != null) {
            WorkerConnection worker = workers.get(taskInfo.workerId);
            if (worker != null) {
                worker.decrementActiveTasks();
            }

            String status = result.optString("status", "unknown");
            System.out.println("Task " + taskId + " completed with status: " + status);

            if (status.equals("success")) {
                boolean hasNonPrime = result.optBoolean("result", false);
                System.out.println("Task result: "
                        + (hasNonPrime
                        ? "Contains non-prime numbers"
                        : "All numbers are prime"));
            } else {
                String message = result.optString("message", "No message");
                System.out.println("Task error: " + message);
            }
        } else {
            System.out.println("Received result for unknown task: " + taskId);
        }
    }

    /**
     * Pings worker if lacks of heartbeat.
     */
    private void checkWorkerHealth() {
        long currentTime = System.currentTimeMillis();
        List<String> unhealthyWorkers = new ArrayList<>();

        for (Map.Entry<String, WorkerConnection> entry : workers.entrySet()) {
            WorkerConnection worker = entry.getValue();
            if (currentTime - worker.lastHeartbeat > 60000) {
                unhealthyWorkers.add(entry.getKey());
                System.out.println("Worker " + entry.getKey() + " appears to be unhealthy");

                pingWorker(worker);
            }
        }

        for (String workerId : unhealthyWorkers) {
            WorkerConnection worker = workers.get(workerId);
            if (worker != null && currentTime - worker.lastHeartbeat > 120000) {
                workers.remove(workerId);
                redistributeWorkerTasks(workerId);
                System.out.println("Removed dead worker: " + workerId);
            }
        }
    }

    /**
     * Creates ping json.
     *
     * @param worker for who to send.
     */
    private void pingWorker(WorkerConnection worker) {
        try {
            JSONObject ping = new JSONObject();
            ping.put("type", TaskType.PING.toString());
            ping.put("ping_id", "ping_" + System.currentTimeMillis());
            ping.put("timestamp", System.currentTimeMillis());

            sendMessage(worker.output, ping.toString());
            System.out.println("Ping sent to worker: " + worker.workerId);
        } catch (IOException e) {
            System.err.println("Failed to ping worker "
                    + worker.workerId + ": " + e.getMessage());
        }
    }

    /**
     * Checks tasks and removes expired.
     */
    private void checkPendingTasks() {
        long currentTime = System.currentTimeMillis();
        List<String> expiredTasks = new ArrayList<>();

        for (Map.Entry<String, TaskInfo> entry : pendingTasks.entrySet()) {
            TaskInfo taskInfo = entry.getValue();
            if (currentTime - taskInfo.submittedTime > 300000) {
                expiredTasks.add(entry.getKey());
            }
        }

        for (String taskId : expiredTasks) {
            TaskInfo taskInfo = pendingTasks.remove(taskId);
            if (taskInfo != null) {
                WorkerConnection worker = workers.get(taskInfo.workerId);
                if (worker != null) {
                    worker.decrementActiveTasks();
                }
                System.out.println("Task " + taskId
                        + " expired and removed from pending tasks");
            }
        }
    }

    /**
     * If we lose worker we need to redistribute tasks.
     *
     * @param workerId from who to redistribute.
     */
    private void redistributeWorkerTasks(String workerId) {
        List<String> tasksToRedistribute = new ArrayList<>();

        for (Map.Entry<String, TaskInfo> entry : pendingTasks.entrySet()) {
            if (entry.getValue().workerId.equals(workerId)) {
                tasksToRedistribute.add(entry.getKey());
            }
        }

        System.out.println("Redistributing "
                + tasksToRedistribute.size() + " tasks from worker " + workerId);

        for (String taskId : tasksToRedistribute) {
            pendingTasks.remove(taskId);
            System.out.println("Task "
                    + taskId + " lost due to worker failure");
        }
    }

    /**
     * Shutdown logic.
     */
    public void shutdown() {
        System.out.println("Shutting down manager...");
        isRunning.set(false);

        JSONObject shutdownMessage = new JSONObject();
        shutdownMessage.put("type", TaskType.SHUTDOWN.toString());

        for (WorkerConnection worker : workers.values()) {
            try {
                sendMessage(worker.output, shutdownMessage.toString());
                System.out.println("Shutdown command sent to worker: "
                        + worker.workerId);
            } catch (IOException e) {
                System.err.println("Failed to send shutdown to worker "
                        + worker.workerId + ": " + e.getMessage());
            }
        }

        workerHandlerExecutor.shutdown();
        maintenanceExecutor.shutdown();

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }

        System.out.println("Manager shutdown complete");
    }

    /**
     * Class to store worker`s data.
     */
    private static class WorkerConnection {
        final String workerId;
        final Socket socket;
        final DataInputStream input;
        final DataOutputStream output;
        volatile long lastHeartbeat;
        volatile int activeTasks;

        /**
         * Constructor.
         *
         * @param workerId id of worker.
         * @param socket - worker socket.
         * @param input - input stream of worker.
         * @param output - output stream of worker.
         */
        WorkerConnection(String workerId, Socket socket,
                         DataInputStream input, DataOutputStream output) {
            this.workerId = workerId;
            this.socket = socket;
            this.input = input;
            this.output = output;
            this.lastHeartbeat = System.currentTimeMillis();
            this.activeTasks = 0;
        }

        /**
         * Updates last heartbeat.
         */
        void updateLastHeartbeat() {
            this.lastHeartbeat = System.currentTimeMillis();
        }

        /**
         * Adds one to active tasks.
         */
        void incrementActiveTasks() {
            this.activeTasks++;
        }

        /**
         * Removes one from active tasks.
         */
        void decrementActiveTasks() {
            this.activeTasks = Math.max(0, this.activeTasks - 1);
        }

        /**
         * Check healthiness.
         *
         * @return true if healthy.
         */
        boolean isHealthy() {
            return System.currentTimeMillis() - lastHeartbeat < 60000;
        }
    }

    /**
     * Information about task.
     *
     * @param taskId - id of task.
     * @param workerId - id of worker who took it.
     * @param submittedTime - when it was submitted.
     */
    private record TaskInfo(String taskId,
                            String workerId, long submittedTime) {
    }


    /**
     * Starter of manager.
     *
     * @param args - nothing interesting.
     */
    public static void main(String[] args) {
        Manager manager = new Manager();

        Runtime.getRuntime().addShutdownHook(new Thread(manager::shutdown));

        Thread managerThread = new Thread(manager::start);
        managerThread.start();

        ServerSocket httpServerSocket;
        try {
            httpServerSocket = new ServerSocket(Integer.parseInt(
                    System.getenv().getOrDefault("OUTSIDE_HOST", "8081")
            ));
            System.out.println("HTTP server started on port " + httpServerSocket.getLocalPort());
        } catch (IOException e) {
            System.err.println("Failed to create HTTP server socket: " + e.getMessage());
            return;
        }

        Thread httpServerThread = new Thread(() -> {
            while (!httpServerSocket.isClosed()) {
                try (Socket connection = httpServerSocket.accept()) {
                    handleHttpRequest(connection, manager);
                } catch (IOException e) {
                    if (!httpServerSocket.isClosed()) {
                        System.err.println("Failed to accept HTTP connection: " + e.getMessage());
                    }
                }
            }
        });
        httpServerThread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Manager started. "
                + "Available commands: 'task <numbers>', 'status', 'quit'");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            if (command.equals("quit")) {
                try {
                    httpServerSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing HTTP server socket: "
                            + e.getMessage());
                }
                manager.shutdown();
                break;
            } else if (command.equals("status")) {
                printConsoleStatus(manager);
            } else if (command.startsWith("task ")) {
                try {
                    String[] parts = command.substring(5).split("\\s+");
                    int[] data = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        data[i] = Integer.parseInt(parts[i]);
                    }

                    String taskId = manager.submitTask(data);
                    System.out.println("Task submitted: " + taskId);
                } catch (Exception e) {
                    System.err.println("Error submitting task: " + e.getMessage());
                }
            } else {
                System.out.println("Unknown command. "
                        + "Use: 'task <numbers>', 'status', 'quit'");
            }
        }

        scanner.close();
    }

    /**
     * Handles data input using http.
     *
     * @param connection - http input socket.
     * @param manager - where to send data.
     */
    private static void handleHttpRequest(Socket connection, Manager manager) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

            String requestLine = reader.readLine();
            if (requestLine == null) return;

            String line;
            int contentLength = 0;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.substring(15).trim());
                }
            }

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendHttpResponse(writer, 400,
                        "Bad Request", "Invalid request format");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];

            switch (method) {
                case "GET":
                    handleGetRequest(path, writer, manager);
                    break;
                case "POST":
                    handlePostRequest(path, reader, writer, manager, contentLength);
                    break;
                default:
                    sendHttpResponse(writer, 405,
                            "Method Not Allowed", "Method not supported");
            }

        } catch (Exception e) {
            System.err.println("Error handling HTTP request: " + e.getMessage());
        }
    }

    /**
     * Handles GET requests.
     * If we have status or quit, do smth else 404.
     *
     * @param path case of request.
     * @param writer http creator.
     * @param manager where to send request.
     */
    private static void handleGetRequest(String path, PrintWriter writer, Manager manager) {
        switch (path) {
            case "/status":
                JSONObject status = getManagerStatus(manager);
                sendHttpResponse(writer, 200,
                        "OK", status.toString(), "application/json");
                break;
            case "/quit":
                sendHttpResponse(writer, 200,
                        "OK", "{\"message\":\"Shutdown initiated\"}");
                new Thread(manager::shutdown).start();
                break;
            default:
                sendHttpResponse(writer, 404,
                        "Not Found", "Endpoint not found");
        }
    }

    /**
     * Handles POST requests.
     *
     * @param path case of post.
     * @param reader - reader of body.
     * @param writer - creator of http.
     * @param manager - where to send data.
     * @param contentLength - length of body.
     * @throws IOException - if cant read from socket.
     */
    private static void handlePostRequest(String path,
                                          BufferedReader reader, PrintWriter writer,
                                          Manager manager, int contentLength) throws IOException {
        if (!path.equals("/task")) {
            sendHttpResponse(writer, 404,
                    "Not Found", "Endpoint not found");
            return;
        }

        if (contentLength <= 0) {
            sendHttpResponse(writer, 400,
                    "Bad Request", "Content-Length required");
            return;
        }

        char[] bodyChars = new char[contentLength];
        int totalRead = 0;
        while (totalRead < contentLength) {
            int read = reader.read(bodyChars, totalRead, contentLength - totalRead);
            if (read == -1) break;
            totalRead += read;
        }

        String requestBody = new String(bodyChars, 0, totalRead);

        try {
            JSONObject requestJson = new JSONObject(requestBody);
            JSONArray dataArray = requestJson.getJSONArray("data");

            int[] data = new int[dataArray.length()];
            for (int i = 0; i < dataArray.length(); i++) {
                data[i] = dataArray.getInt(i);
            }

            String taskId = manager.submitTask(data);

            JSONObject response = new JSONObject();
            response.put("task_id", taskId);
            response.put("status", "submitted");

            sendHttpResponse(writer, 200,
                    "OK", response.toString(), "application/json");

        } catch (JSONException e) {
            sendHttpResponse(writer, 400,
                    "Bad Request", "Invalid JSON format");
        } catch (Exception e) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", e.getMessage());
            sendHttpResponse(writer, 500,
                    "Internal Server Error", errorResponse.toString(), "application/json");
        }
    }

    /**
     * Overload of creator of http.
     *
     * @param writer thingy to create http.
     * @param statusCode - status code.
     * @param statusText - text of message.
     * @param body - body.
     */
    private static void sendHttpResponse(PrintWriter writer,
                                         int statusCode, String statusText, String body) {
        sendHttpResponse(writer, statusCode, statusText, body, "text/plain");
    }

    /**
     * Creates and sends http.
     *
     * @param writer - creates http.
     * @param statusCode - status code.
     * @param statusText - status text.
     * @param body - body.
     * @param contentType - type of content.
     */
    private static void sendHttpResponse(PrintWriter writer,
                                         int statusCode, String statusText,
                                         String body, String contentType) {
        writer.println("HTTP/1.1 " + statusCode + " " + statusText);
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + body.length());
        writer.println("Connection: close");
        writer.println();
        writer.println(body);
        writer.flush();
    }

    /**
     * Get manager status.
     *
     * @param manager which status to find.
     * @return status json.
     */
    private static JSONObject getManagerStatus(Manager manager) {
        JSONObject status = new JSONObject();
        status.put("active_workers", manager.workers.size());
        status.put("pending_tasks", manager.pendingTasks.size());

        JSONArray workersArray = new JSONArray();
        for (WorkerConnection worker : manager.workers.values()) {
            JSONObject workerInfo = new JSONObject();
            workerInfo.put("worker_id", worker.workerId);
            workerInfo.put("active_tasks", worker.activeTasks);
            workerInfo.put("last_heartbeat_ms_ago",
                    System.currentTimeMillis() - worker.lastHeartbeat);
            workerInfo.put("is_healthy", worker.isHealthy());
            workersArray.put(workerInfo);
        }
        status.put("workers", workersArray);

        return status;
    }

    /**
     * Prints manager status in console.
     *
     * @param manager which status to print.
     */
    private static void printConsoleStatus(Manager manager) {
        System.out.println("=== Manager Status ===");
        System.out.println("Active workers: " + manager.workers.size());
        System.out.println("Pending tasks: " + manager.pendingTasks.size());

        for (WorkerConnection worker : manager.workers.values()) {
            long timeSinceLastHeartbeat = System.currentTimeMillis() - worker.lastHeartbeat;
            System.out.println("Worker " + worker.workerId
                    + ": " + worker.activeTasks + " active tasks, "
                    + "last heartbeat " + (timeSinceLastHeartbeat / 1000)
                    + "s ago");
        }
        System.out.println("======================");
    }
}