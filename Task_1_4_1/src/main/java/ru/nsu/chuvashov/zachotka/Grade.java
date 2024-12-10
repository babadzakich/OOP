package ru.nsu.chuvashov.zachotka;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for implementing grades.
 */
@Getter
public class Grade {
    private final String subject;
    @Setter private String teacherName;
    @Setter private String passedDate;
    private final String typeOfPass;
    private final int semester;
    @Setter private int grade;

    Grade(String subject, String teacherName, String passedDate,
          int semester, int grade, String typeOfPass) {
        if (grade < 2 || grade > 5) {
            throw new IllegalArgumentException("Grade must be between 2 and 5");
        }
        this.subject = subject;
        this.teacherName = teacherName;
        this.passedDate = passedDate;
        this.semester = semester;
        this.grade = grade;
        this.typeOfPass = typeOfPass;
    }

}
