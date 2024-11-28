package ru.nsu.chuvashov.zachotka;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for describing student.
 */
public class Student {
    private final String name;
    private final String group;
    private final List<Grade> grades;
    private int semester;
    private boolean commercial;

    /**
     * Constructor.
     *
     * @param name - student name.
     * @param group - group.
     * @param semester - semester.
     * @param commercial - is on budget?
     */
    public Student(String name, String group, int semester, boolean commercial) {
        this.name = name;
        this.group = group;
        this.grades = new ArrayList<>();
        this.semester = semester;
        this.commercial = commercial;
    }

    /**
     * Method to add grade to Student.
     * If grade is not between 2 and 5, we don`t add.
     * We don`t add if we got 2 last semester or semester is greater than curr.
     * If we already have such a subject, we only change signature.
     *
     * @param grade which we want to add.
     */
    void addGrade(Grade grade) {
        if (grade.getGrade() < 2 || grade.getGrade() > 5) {
            throw new IllegalArgumentException("Оценка должна быть между 2 и 5");
        }

        if (grade.getSemester() > semester) {
            throw new IllegalArgumentException("Семестр должен быть ниже или равен текущему");
        }

        if (grades.stream().anyMatch(existingGrade
                -> existingGrade.getSemester() == grade.getSemester() - 1
                && existingGrade.getGrade() == 2)) {
            throw new IllegalArgumentException("Получил 2 в прошлом семестре");
        }

        if (grades.stream().anyMatch(existingGrade
                -> existingGrade.getSemester() == grade.getSemester()
                && existingGrade.getSubject().equals(grade.getSubject()))) {
            grades.stream().filter(existingGrade
                    -> existingGrade.getSubject().equals(grade.getSubject())
                && existingGrade.getSemester() == grade.getSemester()).forEach(existingGrade -> {
                    existingGrade.setGrade(grade.getGrade());
                    existingGrade.setPassedDate(grade.getPassedDate());
                    existingGrade.setTeacherName(grade.getTeacherName());
                });
        } else {
            grades.add(grade);
        }
    }

    public void setCurrentSemester(int semester) {
        this.semester = semester;
    }

    public void setCommercial(boolean commercial) {
        this.commercial = commercial;
    }

    public int getSemester() {
        return semester;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public boolean isCommercial() {
        return commercial;
    }
}
