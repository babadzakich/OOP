package ru.nsu.chuvashov.configholder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ControlPoint {
    String name;
    Date date;
    Map<Integer,Integer> marks = new HashMap<>();

    public ControlPoint(String name, Date date, int[] marks) {
        this.name = name;
        this.date = date;
        this.marks.put(3, marks[0]);
        this.marks.put(4, marks[1]);
        this.marks.put(5, marks[2]);
    }
}
