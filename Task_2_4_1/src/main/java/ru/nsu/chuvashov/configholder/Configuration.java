package ru.nsu.chuvashov.configholder;

import java.util.List;
import java.util.Map;

public record Configuration(Tools tools, List<Group> groups, List<Task> tasks, Map<String, List<String>> studentCheckers,
                            List<ControlPoint> controlPoints, List<String> imports) {
}
