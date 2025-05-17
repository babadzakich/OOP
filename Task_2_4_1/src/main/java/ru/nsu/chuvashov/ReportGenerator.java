package ru.nsu.chuvashov;

import ru.nsu.chuvashov.configholder.Configuration;
import ru.nsu.chuvashov.datatransferobject.ConfigStudent;
import ru.nsu.chuvashov.datatransferobject.TaskResult;
import ru.nsu.chuvashov.configholder.Task;

import java.util.*;

public class ReportGenerator {
    public String generateHtmlReport(Configuration config, Map<Integer, List<ConfigStudent>> groups, List<Task> tasks) {
        StringBuilder html = new StringBuilder();
        html.append("""
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
                    td, th { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    .group-section { margin-bottom: 40px; }
                </style>
            </head>
            <body>
            """);

        for (Map.Entry<Integer, List<ConfigStudent>> groupEntry : groups.entrySet()) {
            int groupNumber = groupEntry.getKey();
            List<ConfigStudent> students = groupEntry.getValue();

            html.append("<div class='group-section'>")
                .append("<h1>Группа ").append(groupNumber).append("</h1>");

            for (Task task : tasks) {
                String taskName = task.name();
                String taskId = task.task();

                List<ConfigStudent> studentsWithTask = groupEntry.getValue().stream()
                    .filter(student -> student.getResults().containsKey(taskId))
                    .toList();


                html.append("<h2>Задача ").append(taskName).append("</h2>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<th>Студент</th>")
                    .append("<th>Сборка</th>")
                    .append("<th>Документация</th>")
                    .append("<th>Style guide</th>")
                    .append("<th>Тесты (Passed/Failed/Error)</th>")
//                        .append("<th>Доп. балл</th>")
                    .append("<th>Итого</th>")
                    .append("</tr>");
                if (studentsWithTask.isEmpty()) {
                    html.append("<tr>")
                            .append("    <td colspan=\"6\">Никто не решил задачу</td>")
                            .append("</tr>");
                } else {
                    for (ConfigStudent student : studentsWithTask) {
                        TaskResult result = student.getResults().get(taskId);
                        html.append("<tr>")
                                .append("<td>").append(student.getName()).append("</td>")
                                .append("<td>").append(result.isCompilationSuccess() ? "+" : "-").append("</td>")
                                .append("<td>").append(result.isDocumentationSuccess() ? "+" : "-").append("</td>")
                                .append("<td>").append(result.isStyleCheckPassed() ? "+" : "-").append("</td>")
                                .append("<td>").append(result.getTestsPassed()).append("/")
                                .append(result.getTestsFailed()).append("/")
                                .append(result.getTestsError()).append("</td>")
                                //                            .append("<td>").append(result.getExtraPoints()).append("</td>")
                                .append("<td>").append(result.getScore()).append("</td>")
                                .append("</tr>");
                    }
                }
                html.append("</table>");

            }
            html.append("</div>");
        }



        return html.append("</body></html>").toString();
    }
}

