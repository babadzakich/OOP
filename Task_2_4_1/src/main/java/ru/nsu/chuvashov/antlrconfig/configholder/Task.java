package ru.nsu.chuvashov.antlrconfig.configholder;

import java.time.LocalDate;

/**
 * Task data class.
 *
 * @param task - id (for example 1_1_1).
 * @param name - name of lab(Пирамидальная сортировка).
 * @param firstCommit - date of soft deadline.
 * @param lastCommit - date of hard deadline.
 * @param points - points for lab.
 */
public record Task(String task, String name,
                   LocalDate firstCommit, LocalDate lastCommit,
                   int points) {
    @Override
    public String toString() {
        return task + ": {\nName=" + name
                + "\nfirstCommit=" + firstCommit
                + "\nlastCommit=" + lastCommit
                + "\npoints=" + points + "%\n}";
    }
}
