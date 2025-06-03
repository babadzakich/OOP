package ru.nsu.chuvashov.datatransferobject;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for check results of task.
 */
@Getter
@Setter
public class TaskResult {
    private final String taskName;
    private boolean compilationSuccess;
    private boolean documentationSuccess;
    private boolean styleCheckPassed;
    private int testsPassed;
    private int testsFailed;
    private int testsError;
    private double score;

    /**
     * Constructor.
     *
     * @param taskName task name.
     */
    public TaskResult(String taskName) {
        this.taskName = taskName;
    }
}
