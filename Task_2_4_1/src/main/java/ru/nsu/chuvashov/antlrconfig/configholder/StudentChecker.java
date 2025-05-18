package ru.nsu.chuvashov.antlrconfig.configholder;

import java.util.List;

/**
 * Extra information class.
 *
 * @param name name of student.
 * @param tasks which task give extra point for student.
 */
public record StudentChecker(String name, List<String> tasks) {
}
