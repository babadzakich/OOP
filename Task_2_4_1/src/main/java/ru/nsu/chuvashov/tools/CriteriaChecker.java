package ru.nsu.chuvashov.tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ru.nsu.chuvashov.configholder.Configuration;
import ru.nsu.chuvashov.configholder.Group;
import ru.nsu.chuvashov.configholder.Task;
import ru.nsu.chuvashov.datatransferobject.ConfigStudent;
import ru.nsu.chuvashov.datatransferobject.TaskResult;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class CriteriaChecker {
    private static final String TESTER_WORKSPACE = "/tmp/tester_workspace";
    private final Configuration config;
    public CriteriaChecker(Configuration config) {
        this.config = config;
    }

    public void processTask(Map<Integer, List<ConfigStudent>> groups, Task task) {
        for (Group group : config.groups()) {
            List<ConfigStudent> students = groups.get(group.getGroupNumber());
            for (ConfigStudent student : students) {
                try {
                    Path studentLabPath = findLabDirectory(group.getGroupNumber(), student, task);
                    if (studentLabPath != null) {
                        TaskResult result = new TaskResult(task.name());
                        checkLab(studentLabPath, result, task);
                        student.getResults().put(task.task(), result);
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    private Path findLabDirectory(Integer groupNumber, ConfigStudent student, Task task) throws IOException {
        Path studentDir = Paths.get(TESTER_WORKSPACE, groupNumber.toString(), student.getNickname());
        if (!Files.exists(studentDir)) {
            return null;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(studentDir)) {
            for (Path path : stream) {
                if (path.getFileName().toString().contains("Task_" + task.task())) {
                    return path;
                }
            }
        }
        return null;
    }

    private void checkLab(Path labPath, TaskResult result, Task task) {
        try {
            boolean buildSuccess = runGradleCommand(labPath, "clean", "compileJava");
            result.setCompilationSuccess(buildSuccess);

            if (buildSuccess) {
                try {
                    runGradleCommand(labPath, "test");
                } catch (Exception e) {
                    System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
                }
                parseTestResults(labPath, result);
            }

            checkStyle(labPath, result);

            boolean documentation = runGradleCommand(labPath, "javadoc");
            result.setDocumentationSuccess(documentation);
            checkLabScore(labPath, result, task);

        } catch (Exception e) {
            System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void checkStyle(Path labPath, TaskResult result) {
        try {
            Process p = new ProcessBuilder("java", "-jar", "checkstyle.jar", "-c", "google_checks.xml", labPath.toString()).start();
            String data;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                 data = reader.lines().collect(Collectors.joining("\n"));
            }
            System.out.println(data);
            result.setStyleCheckPassed(p.waitFor() == 0);
        } catch (Exception e) {
            System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
            result.setStyleCheckPassed(false);
        }
    }

    private void checkLabScore(Path labPath, TaskResult result, Task task) {
        String branch = "task-" + task.task().replace('_', '-');
        double score = task.points();
        try {
            ProgramUtil.runCommand(labPath, "git", "checkout", branch);

            String firstCommitDate = getCommitDate(labPath, branch, "first");
            String lastCommitDate = getCommitDate(labPath, branch, "last");

            if (isBeforeOrEqual(firstCommitDate, task.first_commit())) {
                score -= 0.5;
            }

            if (!isBeforeOrEqual(lastCommitDate, task.last_commit())) {
                score -= 0.5;
            }

            result.setScore(score);

        } catch (Exception e) {
            System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
            result.setScore(0);
        }
        try {
            ProgramUtil.runCommand(labPath, "git", "checkout", "main");
        } catch (Exception e) {
            System.err.println("Error: " + Arrays.toString(e.getStackTrace()));
            exit(-1);
        }
    }

    private String getCommitDate(Path labPath, String branch, String type) throws IOException, InterruptedException {
        String commitHash;
        if ("first".equals(type)) {
            commitHash = ProgramUtil.runCommand(labPath, "git", "rev-list", "--max-parents=0", branch).trim();
        } else {
            commitHash = ProgramUtil.runCommand(labPath, "git", "rev-parse", branch).trim();
        }

        return ProgramUtil.runCommand(labPath, "git", "show", "-s", "--format=%ci", commitHash).trim();
    }

    private boolean isBeforeOrEqual(String commitDate, LocalDate deadline) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
            LocalDateTime commitDateTime = LocalDateTime.parse(commitDate, formatter);
            LocalDate commitLocalDate = commitDateTime.toLocalDate();
            return commitLocalDate.isBefore(deadline) || commitLocalDate.isEqual(deadline);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing dates: " + Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    private void parseTestResults(Path labPath, TaskResult result) throws ParserConfigurationException, IOException, SAXException {
        int errors = 0;
        int failed = 0;
        int total = 0;

        File dir = new File(labPath.toString() + "/build/test-results/test");

        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Директория с результатами тестов не найдена: " + dir.getAbsolutePath());
            result.setTestsPassed(0);
            result.setTestsFailed(0);
            result.setTestsError(0);
            return;
        }

        // Проверяем наличие файлов в директории
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("В директории " + dir.getAbsolutePath() + " нет файлов");
            result.setTestsPassed(0);
            result.setTestsFailed(0);
            result.setTestsError(0);
            return;
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().endsWith(".xml")) {
                // Парсим каждый XML-файл
                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(file);
                Element testsuite = (Element) doc.getElementsByTagName("testsuite").item(0);
                total += Integer.parseInt(testsuite.getAttribute("tests"));
                failed += Integer.parseInt(testsuite.getAttribute("failures"));
                errors += Integer.parseInt(testsuite.getAttribute("errors"));
            }
        }

        result.setTestsPassed(total - failed - errors);
        result.setTestsFailed(failed);
        result.setTestsError(errors);
    }

    private boolean runGradleCommand(Path projectPath, String... commands) throws IOException, InterruptedException {
        String gradlew = projectPath.resolve(System.getProperty("os.name").toLowerCase().contains("win") ?
            "gradlew.bat" : "gradlew").toString();

        // Make gradlew executable on Unix systems
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            new ProcessBuilder("chmod", "+x", gradlew).start().waitFor();
        }

        String[] fullCommand = new String[commands.length + 1];
        fullCommand[0] = gradlew;
        System.arraycopy(commands, 0, fullCommand, 1, commands.length);

        Process process = new ProcessBuilder(fullCommand)
            .directory(projectPath.toFile())
            .redirectErrorStream(true)
            .start();

        return process.waitFor() == 0;
    }
}
