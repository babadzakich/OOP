package ru.nsu.chuvashov.antlrconfig.configholder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Control point data.
 */
@Getter
public class ControlPoint {
    private final String name;
    private final LocalDate date;
    private final Map<Integer, Integer> marks = new HashMap<>();

    /**
     * Constructor.
     *
     * @param name - name of control point.
     * @param date - date of control point.
     * @param marks - points for receiving marks(3,4,5).
     */
    public ControlPoint(String name, LocalDate date, int[] marks) {
        this.name = name;
        this.date = date;
        this.marks.put(3, marks[0]);
        this.marks.put(4, marks[1]);
        this.marks.put(5, marks[2]);
    }
}
