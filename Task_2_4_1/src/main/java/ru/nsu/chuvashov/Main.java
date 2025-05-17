package ru.nsu.chuvashov;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.chuvashov.antlrconfig.*;
import ru.nsu.chuvashov.antlrconfig.configholder.Configuration;
import ru.nsu.chuvashov.antlrconfig.configholder.Group;
import ru.nsu.chuvashov.antlrconfig.configholder.Student;
import ru.nsu.chuvashov.antlrconfig.configholder.Task;
import ru.nsu.chuvashov.datatransferobject.ConfigStudent;
import ru.nsu.chuvashov.tools.CriteriaChecker;
import ru.nsu.chuvashov.tools.ReportGenerator;
import ru.nsu.chuvashov.tools.RepositoryManager;

/**
 * Main.
 */
public class Main {

    /**
     * Main.
     *
     * @param args nothing useful in here.
     */
    public static void main(String[] args) {
        try {
            Configuration config = MyParser.parseConfig();
            CriteriaChecker checker = new CriteriaChecker(config);

            Map<Integer, List<ConfigStudent>> groups = new HashMap<>();

            for (Group group : config.groups()) {
                List<ConfigStudent> results = new ArrayList<>();
                for (Student student : group.getStudents()) {
                    results.add(new ConfigStudent(student.username(), student.name()));
                }
                groups.put(group.getGroupNumber(), results);
            }
            RepositoryManager repoManager = new RepositoryManager(config);
            for (Task task : config.tasks()) {
                checker.processTask(groups, task);
            }
            repoManager.stopWork();
            // Generate and save report
            ReportGenerator generator = new ReportGenerator();
            String report = generator.generateHtmlReport(config, groups);
            
            Files.writeString(Path.of("report.html"), report);
            System.out.println("Report generated: report.html");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}