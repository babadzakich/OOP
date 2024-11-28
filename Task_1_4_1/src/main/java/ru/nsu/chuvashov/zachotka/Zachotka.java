package ru.nsu.chuvashov.zachotka;

/**
 * Class for record book implementation.
 */
public class Zachotka {
    private final int examsPasses = 18;
    private final Student student;

    Zachotka(Student student) {
        this.student = student;
    }

    /**
     * Method for extracting student mark rounded to 10th digit.
     *
     * @return average mark.
     */
    public double getMark() {
        return (double) Math.round(
                (double) (Integer) student.getGrades().stream()
                        .mapToInt(Grade::getGrade).sum()
                / (double) student.getGrades().size() * 10) / 10;
    }

    /**
     * If student is on commercial learning, and have >3 for exams,
     * for 2 last semesters, he can be transfered to budget.
     *
     * @return true if student can be transfered.
     * @throws Exception if student on budget or on 2 or less course.
     */
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

    /**
     * Check, if student got all fives and is on budget,
     * he can get a raise in scholarship.
     *
     * @return true if student can get raise.
     * @throws Exception if student isn`t on budget.
     */
    public boolean raiseScholarship() throws Exception {
        if (student.isCommercial()) {
            throw new Exception("Не на бюджетной основе");
        }

        return student.getGrades().stream().allMatch(grade
                -> grade.getSemester() == (student.getSemester() - 1)
                && grade.getGrade() > 4);
    }

    /**
     * Calculation of possibility of receiving red diploma.
     * If student got 3 or less, he cant get it.
     * If we can`t certainly determine if he can get diploma, raise an exception.
     * Student can get red diploma if he has less than 25% of 4 all time.
     * And diploma work is 5.
     *
     * @return true if student can get red diploma.
     * @throws Exception if we can`t determine achieving of diploma.
     */
    public boolean redDiploma() throws Exception {
        if (student.getGrades().stream().anyMatch(grade
                -> (grade.getTypeOfPass().equals("Экзамен")
                || grade.getTypeOfPass().equals("ДиффЗачёт"))
                && grade.getGrade() <= 3)) {
            return false;
        }

        if (student.getSemester() < 8) {
            if ((student.getGrades().stream().filter(grade
                    -> (grade.getTypeOfPass().equals("Экзамен")
                    || grade.getTypeOfPass().equals("ДиффЗачёт"))
                    && grade.getGrade() == 4).count() * 100 / examsPasses ) >= 25) {
                return false;
            } else {
                throw new Exception("Красный диплом ещё может быть получен, а может и не быть получен!");
            }
        } else {
            return (student.getGrades().stream().filter(grade
                    -> (grade.getTypeOfPass().equals("Экзамен")
                    || grade.getTypeOfPass().equals("ДиффЗачёт"))
                    && grade.getGrade() == 4).count() * 100 / examsPasses) < 25
                    && student.getGrades().stream().anyMatch(grade
                    -> grade.getTypeOfPass().equals("Диплом")
                    && grade.getGrade() == 5);
        }
    }
}
