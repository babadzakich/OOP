package ru.nsu.chuvashov.configholder;

import java.util.Date;

public record Task(String task, String name, Date first_commit, Date last_commit, int points) {
    @Override
    public String toString() {
        return task + ": {\nName=" + name + "firstCommit=" + first_commit + "\nlastCommit=" + last_commit + "\npoints=" + points + "%\n}";
    }
}
