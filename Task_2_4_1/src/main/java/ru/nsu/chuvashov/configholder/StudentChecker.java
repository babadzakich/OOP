package ru.nsu.chuvashov.configholder;

import lombok.Getter;
import java.util.List;

@Getter
public class StudentChecker {
    String name;
    List<String> tasks;

    public StudentChecker(String name, List<String> tasks) {
        this.name = name;
        this.tasks = tasks;
    }
}
