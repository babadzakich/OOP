package ru.nsu.chuvashov.antlrconfig.configholder;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Group data from config.
 */
@Getter
public class Group {
    private final List<Student> students = new ArrayList<>();
    private final int groupNumber;

    /**
     * Constructor.
     *
     * @param groupNumber group number.
     */
    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @Override
    public String toString() {
        return "Group{number=" + groupNumber + ", students=" + students + "}";
    }
}
