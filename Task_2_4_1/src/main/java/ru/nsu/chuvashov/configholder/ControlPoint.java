package ru.nsu.chuvashov.configholder;

import lombok.Getter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ControlPoint {
    private final String name;
    private final LocalDate date;
    private final Map<Integer,Integer> marks = new HashMap<>();

    public ControlPoint(String name, LocalDate date, int[] marks) {
        this.name = name;
        this.date = date;
        this.marks.put(3, marks[0]);
        this.marks.put(4, marks[1]);
        this.marks.put(5, marks[2]);
    }
}
