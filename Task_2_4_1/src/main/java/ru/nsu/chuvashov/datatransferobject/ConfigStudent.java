package ru.nsu.chuvashov.datatransferobject;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Class for storing check results for tasks for student.
 */
@Getter
public class ConfigStudent {
    private final String nickname;
    private final String name;
    private final Map<String, TaskResult> results = new HashMap<>();
    @Setter private int mark;

    /**
     * Constructor.
     *
     * @param nickname GitHub nickname.
     * @param name name.
     */
    public ConfigStudent(String nickname, String name) {
        this.nickname = nickname;
        this.name = name;
    }
}
