package ru.nsu.chuvashov.antlrconfig.configholder;

/**
 * Student data.
 *
 * @param repo - repository url.
 * @param name - name surname of person.
 * @param username - GitHub nickname.
 */
public record Student(String repo, String name, String username) {

    @Override
    public String toString() {
        return "Student{name='" + name + "', username='" + username + "', Github=" + repo + "}";
    }
}
