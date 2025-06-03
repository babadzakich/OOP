package ru.nsu.chuvashov.antlrconfig.configholder;

import java.util.List;
import java.util.Map;

/**
 * Configuration data.
 *
 * @param tools tools need for checking.
 * @param groups to be checked.
 * @param tasks to be checked.
 * @param studentCheckers - additional info.
 * @param controlPoints - control points.
 * @param imports - imports.
 */
public record Configuration(Tools tools, List<Group> groups,
                            List<Task> tasks, Map<String, List<String>> studentCheckers,
                            List<ControlPoint> controlPoints, List<String> imports) {
}
