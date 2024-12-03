package ru.nsu.chuvashov.zachotka;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZachotkaTest {
    Student student = new Student("Artyom Chuvashov", "23213", 3, false);

    @BeforeEach
    void initStudent() {
        student.addGrade(new Grade("Введение в дискретную математику и логику",
                "Кудинов О В", "25.12.23",
                1, 4, "Экзамен"));
        student.addGrade(new Grade("Введение в алгебру и анализ",
                "Васкевич В Л", "11.01.24",
                1, 5, "Экзамен"));
        student.addGrade(new Grade("Императивное программирование",
                "Плюснин А А", "26.12.23",
                1, 4, "ДиффЗачёт"));
        student.addGrade(new Grade("Введение в дискретную математику и логику",
                "Апанович З В", "11.06.24",
                2, 5, "Экзамен"));
        student.addGrade(new Grade("Введение в алгебру и анализ",
                "Васкевич В Л", "18.06.24",
                2, 5, "Экзамен"));
        student.addGrade(new Grade("Императивное программирование",
                "Стененко А А", "06.06.24",
                2, 5, "Экзамен"));
    }

    @Test
    void getMark() {
        Zachotka zachotka = new Zachotka(student);
        assertEquals(4.7, zachotka.getMark());
    }

    @Test
    void canTransfer() {
        student.setCommercial(true);
        Zachotka zachotka = new Zachotka(student);
        try {
            assertTrue(zachotka.canTransfer());
        } catch (Exception e) {
            fail();
        }
        student.setCommercial(false);
    }

    @Test
    void alreadyTransfered() {
        Zachotka zachotka = new Zachotka(student);
        assertThrows(Exception.class, zachotka::canTransfer);
    }

    @Test
    void earlyTransfer() {
        student.setCurrentSemester(2);
        student.setCommercial(true);
        Zachotka zachotka = new Zachotka(student);
        assertThrows(Exception.class, zachotka::canTransfer);
        student.setCurrentSemester(3);
        student.setCommercial(false);
    }

    @Test
    void cantTransfer() {
        student.setCommercial(true);
        Zachotka zachotka = new Zachotka(student);
        try {
            assert (zachotka.canTransfer());
        } catch (Exception e) {
            fail();
        }
        student.setCommercial(false);
    }

    @Test
    void cantraiseScholarship() {
        Zachotka zachotka = new Zachotka(student);
        try {
            assertFalse(zachotka.raiseScholarship());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void raiseCommercial() {
        student.setCommercial(true);
        Zachotka zachotka = new Zachotka(student);
        assertThrows(Exception.class, zachotka::raiseScholarship);
        student.setCommercial(false);
    }

    @Test
    void canRaise() {
        Zachotka zachotka = new Zachotka(student);
        student.addGrade(new Grade("Императивное программирование",
                "Плюснин А А", "26.12.23",
                1, 5, "ДиффЗачёт"));
        student.addGrade(new Grade("Введение в дискретную математику и логику",
                "Кудинов О В", "25.12.23",
                1, 5, "Экзамен"));
        try {
            assertFalse(zachotka.raiseScholarship());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void diplomaProgress() {
        Zachotka zachotka = new Zachotka(student);
        assertThrows(Exception.class, zachotka::redDiploma);
    }

    @Test
    void redDiploma() {
        student.setCurrentSemester(8);
        student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "25.12.24",
                3, 5, "ДиффЗачёт"));
        student.addGrade(new Grade("Диффуры",
                "Васкевич В Л", "11.01.25",
                3, 5, "Экзамен"));
        student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "26.12.25",
                4, 4, "Экзамен"));
        student.addGrade(new Grade("Модели вычислений",
                "Пузаренко В Г", "11.06.25",
                4, 5, "Экзамен"));
        student.addGrade(new Grade("Хранение и обработка данных",
                "Мигинский Д С", "18.12.25",
                5, 5, "Экзамен"));
        student.addGrade(new Grade("Плюханье",
                "Стененко А А", "06.12.25",
                5, 5, "Экзамен"));
        student.addGrade(new Grade("Введение в алкогольную математику и логику",
                "Чепеньков А И", "25.06.26",
                6, 5, "Экзамен"));
        student.addGrade(new Grade("МЛ",
                "Яковлева В В", "11.01.26",
                6, 5, "Экзамен"));
        student.addGrade(new Grade("Кальянная прога",
                "Плюснин А А", "26.12.26",
                7, 4, "ДиффЗачёт"));
        student.addGrade(new Grade("Теория графов",
                "Апанович З В", "11.06.26",
                7, 5, "Экзамен"));
        student.addGrade(new Grade("Вычи",
                "Васкевич В Л", "18.06.27",
                8, 5, "Экзамен"));
        student.addGrade(new Grade("Геома",
                "Стененко А А", "06.06.27",
                8, 5, "Диплом"));
        Zachotka zachotka = new Zachotka(student);
        try {
            assertTrue(zachotka.redDiploma());
        } catch (Exception e) {
            fail();
        }
        student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "25.12.24",
                3, 4, "ДиффЗачёт"));
        zachotka = new Zachotka(student);
        try {
            assertFalse(zachotka.redDiploma());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void wrongMark() {
        assertThrows(Exception.class, ()
                -> student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "25.12.24",
                3, 0, "ДиффЗачёт")));
    }

    @Test
    void wrongSemester() {
        assertThrows(Exception.class, ()
                -> student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "25.12.24",
                5, 4, "ДиффЗачёт")));
    }

    @Test
    void gotTwo() {
        student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "25.12.24",
                2, 2, "ДиффЗачёт"));
        assertThrows(Exception.class, ()
                -> student.addGrade(new Grade("Операционные системы",
                "Иртегов Д В", "25.12.24",
                3, 4, "ДиффЗачёт")));
    }

    @Test
    void testFile() throws IOException {
        Zachotka zachotka = new Zachotka("Student1.txt");
        assertTrue(zachotka.getStudent().getStudent().equals("Артём Чувашов")
                && zachotka.getStudent().getGrades().size() == 1
                && zachotka.getStudent().getGroup().equals("23213"));
    }
}