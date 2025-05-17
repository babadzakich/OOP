package ru.nsu.chuvashov.configholder;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Group {
    private final List<Student> students = new ArrayList<>();
    private final int groupNumber;

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @Override
    public String toString() {
        return "Group{number=" + groupNumber + ", students=" + students + "}";
    }
}
