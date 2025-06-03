package ru.nsu.chuvashov.tools;

import java.util.*;
import ru.nsu.chuvashov.antlrconfig.configholder.Configuration;
import ru.nsu.chuvashov.antlrconfig.configholder.Task;
import ru.nsu.chuvashov.datatransferobject.ConfigStudent;
import ru.nsu.chuvashov.datatransferobject.TaskResult;

/**
 * HTML report generator.
 */
public class ReportGenerator {
    /**
     * HTML page generator.
     *
     * @param config where all data from configuration lies.
     * @param groups - check results for every group.
     * @return html page.
     */
    public String generateHtmlReport(Configuration config, Map<Integer,
            List<ConfigStudent>> groups) {
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
            List<Task> tasks = config.tasks();
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
                                .append("<td>").append(student.getName())
                                .append("</td>")
                                .append("<td>").append(result.isCompilationSuccess() ? "+" : "-")
                                .append("</td>")
                                .append("<td>").append(result.isDocumentationSuccess() ? "+" : "-")
                                .append("</td>")
                                .append("<td>").append(result.isStyleCheckPassed() ? "+" : "-")
                                .append("</td>")
                                .append("<td>").append(result.getTestsPassed())
                                .append("/")
                                .append(result.getTestsFailed())
                                .append("/")
                                .append(result.getTestsError())
                                .append("</td>")
                                .append("<td>").append(result.getScore())
                                .append("</td>")
                                .append("</tr>");
                    }
                }
                html.append("</table>");
            }
            html.append("<h2>Общая статистика по студентам</h2>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<th>Студент</th>");

            // Столбцы для всех задач (лаб)
            for (Task task : tasks) {
                html.append("<th>").append(task.task()).append("</th>");
            }
            html.append("<th>Сумма</th>")
                    .append("<th>Оценка за 1 семестр</th>")
                    .append("<th>Оценка за 2 семестр</th>")
                    .append("<th>Итоговая оценка</th>")
                    .append("</tr>");

            List<ConfigStudent> students = groupEntry.getValue();

            // Строки для каждого студента
            for (ConfigStudent student : students) {
                Map<String, TaskResult> results = student.getResults();
                double totalScore = 0.0;

                // Суммы баллов для 1-го и 2-го семестров
                double firstSemScore = 0.0;
                double secondSemScore = 0.0;

                html.append("<tr>")
                        .append("<td>").append(student.getName()).append("</td>");

                // Баллы за каждую лабу
                for (Task task : tasks) {
                    String taskId = task.task();
                    if (results.containsKey(taskId)) {
                        TaskResult result = results.get(taskId);
                        double score = result.getScore();
                        totalScore += score;
                        if (taskId.startsWith("1")) {
                            firstSemScore += score;
                        } else if (taskId.startsWith("2")) {
                            secondSemScore += score;
                        }
                        html.append("<td>").append(score).append("</td>");
                    } else {
                        html.append("<td>-</td>");
                    }
                }

                // Вычисление оценки
                int firstSemGrade = calculateGrade(firstSemScore,
                        config.controlPoints().getFirst().getMarks());
                int secondSemGrade = calculateGrade(secondSemScore,
                        config.controlPoints().getLast().getMarks());
                int finalGrade = Math.floorDiv(firstSemGrade
                        + secondSemGrade, 2) - 1;

                html.append("<td>").append(totalScore).append("</td>")
                        .append("<td>").append(Math.max(firstSemGrade, 2)).append("</td>")
                        .append("<td>").append(Math.max(secondSemGrade, 2)).append("</td>")
                        .append("<td>").append(Math.max(finalGrade, 2)).append("</td>")
                        .append("</tr>");
            }
            html.append("</table>");
            html.append("</div>");
        }
        return html.append("</body></html>").toString();
    }

    private int calculateGrade(double score, Map<Integer,
                Integer> gradeCriteria) {
        int grade = 2; // Минимальная оценка (незачёт)
        for (Map.Entry<Integer, Integer> entry : gradeCriteria.entrySet()) {
            int requiredPoints = entry.getValue();
            int possibleGrade = entry.getKey();
            if (score >= requiredPoints && possibleGrade > grade) {
                grade = possibleGrade;
            }
        }
        return grade;
    }
}

