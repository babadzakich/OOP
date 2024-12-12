package ru.nsu.chuvashov.zachotka;

import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.Getter;

/**
 * Class for record book implementation.
 */
public class Zachotka {
    private final int examsPasses = 18;
    @Getter
    private final Student student;

    Zachotka(Student student) {
        this.student = student;
    }

    /**
     * Zachotka constructor from file in resources.
     *
     * @param filename - file of zachotka.
     * @throws IOException if no file found.
     */
    public Zachotka(String filename) throws IOException {
        InputStream inputStream = Zachotka.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new FileNotFoundException(filename);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String[] start = reader.readLine().split(";");
        if (start.length != 4) {
            throw new IllegalArgumentException("Wrong format");
        }
        Student st = new Student(start[0].trim(), start[1].trim(),
                Integer.parseInt(start[2].trim()), start[3].trim().equals("Платка"));
        while (reader.ready()) {
            String line = reader.readLine();
            String[] parts = line.split(";");
            if (parts.length != 6) {
                throw new IllegalArgumentException("Wrong format");
            }
            Grade grade = new Grade(parts[0].trim(), parts[1].trim(),
                    parts[2].trim(),
                    Integer.parseInt(parts[3].trim()),
                            Integer.parseInt(parts[4].trim()),
                            parts[5].trim());
            st.addGrade(grade);
        }
        this.student = st;
        reader.close();
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
    public boolean canTransfer() throws Exception {
        if (!student.isCommercial()) {
            throw new Exception("Уже на бюджете");
        }

        if (student.getSemester() <= 2) {
            throw new Exception("Перевестись можно только с 3 семестра");
        }

        return student.getGrades().stream().noneMatch(grade
                -> ((grade.getSemester() == student.getSemester() - 1
                || grade.getSemester() == student.getSemester() - 2)
                && grade.getGrade() <= 3
                && grade.getTypeOfPass().equals("Экзамен"))
                || (grade.getGrade() == 2));
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
                    && grade.getGrade() == 4).count() * 100 / examsPasses) >= 25) {
                return false;
            } else {
                throw new Exception("Красный диплом ещё может быть получен, "
                        + "а может и не быть получен!");
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

    /**
     * Serialization method.
     *
     * @param filename - where to save information.
     * @throws IOException - if we have troubles with filename.
     */
    public void toFile(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(student.getName()).append("; ");
        stringBuilder.append(student.getGroup()).append("; ");
        stringBuilder.append(student.getSemester()).append("; ");
        if (student.isCommercial()) {
            stringBuilder.append("Платка\n");
        } else {
            stringBuilder.append("Бюджет\n");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        for (Grade grade : student.getGrades()) {
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(grade.getSubject()).append("; ");
            stringBuilder.append(grade.getTeacherName()).append("; ");
            stringBuilder.append(grade.getPassedDate()).append("; ");
            stringBuilder.append(grade.getGrade()).append("; ");
            stringBuilder.append(grade.getTypeOfPass()).append('\n');
            fileOutputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        }
        fileOutputStream.close();
    }
}
