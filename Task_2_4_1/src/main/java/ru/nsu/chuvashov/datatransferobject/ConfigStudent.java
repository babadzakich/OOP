package ru.nsu.chuvashov.datatransferobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConfigStudent {
    private String nickname;
    private String name;
    private final Map<String, TaskResult> results = new HashMap<>();
    private int mark;

    public ConfigStudent(String nickname, String name) {
        this.nickname = nickname;
        this.name = name;
    }
}
