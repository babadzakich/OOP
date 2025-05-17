package ru.nsu.chuvashov.configholder;

import java.time.LocalDate;

public record Task(String task, String name, LocalDate first_commit, LocalDate last_commit, int points) {
    @Override
    public String toString() {
        return task + ": {\nName=" + name + "firstCommit=" + first_commit + "\nlastCommit=" + last_commit + "\npoints=" + points + "%\n}";
    }
}
