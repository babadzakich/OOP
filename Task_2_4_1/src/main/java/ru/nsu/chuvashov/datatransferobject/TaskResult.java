package ru.nsu.chuvashov.datatransferobject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResult {
    private String taskName;
    private boolean compilationSuccess;
    private boolean documentationSuccess;
    private boolean styleCheckPassed;
    private int testsPassed;
    private int testsFailed;
    private int testsError;
    private double score;

    public TaskResult(String taskName) {
        this.taskName = taskName;
    }
}
