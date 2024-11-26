package ru.nsu.chuvashov.zachotka;

public class Zachotka {
    private final Student student;
    Zachotka(Student student) {
        this.student = student;
    }

    public double getMark() {
        return (double) (Integer) student.getGrades().stream().mapToInt(Grade::getGrade).sum()
                / (double) student.getGrades().size();
    }

    private static int comparator(Grade grade1, Grade grade2) {
        return Math.max(grade1.getGrade(), grade2.getGrade());
    }

    public boolean canTransfer() throws Exception{
        if (!student.isCommercial()) {
            throw new Exception("Уже на бюджете");
        }

        if (student.getSemester() <= 2) {
            throw new Exception("Перевестись можно только с 3 семестра");
        }

        return student.getGrades().stream().noneMatch(grade
                -> ((grade.getSemester() == student.getSemester() - 1 || grade.getSemester() == student.getSemester() - 2)
                && grade.getGrade() <= 3 && grade.getTypeOfPass().equals("Экзамен")) || (grade.getGrade() == 2));
    }

    public boolean raiseScholarship() throws Exception {
        if (student.isCommercial()) {
            throw new Exception("Не на бюджетной основе");
        }

        return student.getGrades().stream().anyMatch(grade
                -> grade.getSemester() == student.getSemester() - 1 && grade.getGrade() > 4);
    }

    public boolean redDiploma() {
        if (student.getGrades().stream().anyMatch(grade -> grade.getGrade() <= 3)) {
            return false;
        }

        return true;
    }
}
