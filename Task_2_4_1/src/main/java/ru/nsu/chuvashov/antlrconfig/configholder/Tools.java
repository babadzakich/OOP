package ru.nsu.chuvashov.antlrconfig.configholder;

/**
 * Build tools data class.
 *
 * @param criteria - path to class to be used as checker.
 * @param buildTool - used buildtool.
 */
public record Tools(String criteria, String buildTool) {

}
