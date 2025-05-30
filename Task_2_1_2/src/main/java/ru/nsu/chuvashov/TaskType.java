package ru.nsu.chuvashov;

/**
 * Enum for types of tasks.
 */
public enum TaskType {
    HEARTBEAT("heartbeat"),
    TASKRESULT("task_result"),
    PING("ping"),
    PONG("pong"),
    TASK("task"),
    SHUTDOWN("shutdown"),
    UNKNOWN("unknown");


    /**
     * Constructor.
     *
     * @param heartbeat - to construct.
     */
    TaskType(String heartbeat) {
    }
}
