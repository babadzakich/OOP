package ru.nsu.chuvashov.zachotka;

public class Grade {
    private final String subject;
    private String teacherName;
    private String passedDate;
    private final String typeOfPass;
    private final int semester;
    private int grade;

    Grade(String subject, String teacherName, String passedDate, int semester, int grade, String typeOfPass) {
        this.subject = subject;
        this.teacherName = teacherName;
        this.passedDate = passedDate;
        this.semester = semester;
        this.grade = grade;
        this.typeOfPass = typeOfPass;
    }
    public String getSubject() {
        return subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getPassedDate() {
        return passedDate;
    }

    public int getSemester() {
        return semester;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getTypeOfPass() {
        return typeOfPass;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setPassedDate(String passedDate) {
        this.passedDate = passedDate;
    }
}
