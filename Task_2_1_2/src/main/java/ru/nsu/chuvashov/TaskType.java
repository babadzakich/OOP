package ru.nsu.chuvashov;

public enum TaskType {
    HEARTBEAT("heartbeat"),
    TASKRESULT("task_result"),
    PING("ping"),
    PONG("pong"),
    TASK("task"),
    SHUTDOWN("shutdown"),
    UNKNOWN("unknown");


    TaskType(String heartbeat) {
    }
}
