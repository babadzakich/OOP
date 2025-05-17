package ru.nsu.chuvashov;

import ru.nsu.chuvashov.configholder.Configuration;
import ru.nsu.chuvashov.configholder.Group;
import ru.nsu.chuvashov.configholder.Student;
import ru.nsu.chuvashov.configholder.Task;
import ru.nsu.chuvashov.datatransferobject.ConfigStudent;
import ru.nsu.chuvashov.tools.CriteriaChecker;
import ru.nsu.chuvashov.tools.RepositoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        try {
            Configuration config = MyParser.parseConfig();
            RepositoryManager repoManager = new RepositoryManager(config);
//            repoManager.validateGitConfig();
            CriteriaChecker checker = new CriteriaChecker(config);

            Map<Integer,List<ConfigStudent>> groups = new HashMap<>();

            for (Group group : config.groups()) {
                List<ConfigStudent> results = new ArrayList<>();
                for (Student student : group.getStudents()) {
                    results.add(new ConfigStudent(student.username(), student.name()));
                }
                groups.put(group.getGroupNumber(), results);
            }

            for (Task task : config.tasks()) {
                checker.processTask(groups, task);
            }

            // Generate and save report
            ReportGenerator generator = new ReportGenerator();
            String report = generator.generateHtmlReport(config, groups, config.tasks());
            
            Files.writeString(Path.of("report.html"), report);
            System.out.println("Report generated: report.html");
            repoManager.stopWork();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}