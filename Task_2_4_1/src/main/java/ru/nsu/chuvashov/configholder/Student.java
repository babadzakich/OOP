package ru.nsu.chuvashov.configholder;

public record Student(String repo, String name, String username) {

    @Override
    public String toString() {
        return "Student{name='" + name + "', username='" + username + "', Github=" + repo + "}";
    }
}
