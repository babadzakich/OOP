package ru.nsu.chuvashov.tools;

import static java.lang.System.exit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ru.nsu.chuvashov.antlrconfig.configholder.Configuration;
import ru.nsu.chuvashov.antlrconfig.configholder.Group;
import ru.nsu.chuvashov.antlrconfig.configholder.Task;
import ru.nsu.chuvashov.datatransferobject.ConfigStudent;
import ru.nsu.chuvashov.datatransferobject.TaskResult;

/**
 * Class that runs all checks for labs.
 */
public class CriteriaChecker {
    private final String testerWorkspace = "/tmp/tester_workspace";
    private final Configuration config;

    /**
     * Constructor.
     *
     * @param config config.
     */
    public CriteriaChecker(Configuration config) {
        this.config = config;
    }

    /**
     * Runs checks for every student for one lab.
     *
     * @param groups all groups and students.
     * @param task - task to process.
     */
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

    /**
     * Finds path to directory with lab.
     *
     * @param groupNumber group number.
     * @param student student to find.
     * @param task to process.
     * @return path to folder of exact task at exact student.
     * @throws IOException if nothing found.
     */
    private Path findLabDirectory(Integer groupNumber,
                                  ConfigStudent student, Task task) throws IOException {
        Path studentDir = Paths.get(testerWorkspace,
                groupNumber.toString(), student.getNickname());
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

    /**
     * Run checks for lab.
     *
     * @param labPath where lab lies.
     * @param result where to write results.
     * @param task - to process.
     */
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

    /**
     * Run checks for suitable style.
     *
     * @param labPath - path to lab directory,
     * @param result - where to write result of check.
     */
    private void checkStyle(Path labPath, TaskResult result) {
        try {
            Process p = new ProcessBuilder("java", "-jar",
                    "checkstyle.jar", "-c", "google_checks.xml", labPath.toString()).start();
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

    /**
     * Looks at deadlines. If student is late -> minus 0.5 points.
     *
     * @param labPath - path to lab directory.
     * @param result - where to write results.
     * @param task - information about checked task.
     */
    private void checkLabScore(Path labPath, TaskResult result, Task task) {
        String branch = "task-" + task.task().replace('_', '-');
        double score = task.points();
        try {
            ProgramUtil.runCommand(labPath, "git", "checkout", branch);

            String firstCommitDate = getCommitDate(labPath, branch, "first");
            String lastCommitDate = getCommitDate(labPath, branch, "last");

            if (isLater(firstCommitDate, task.firstCommit())) {
                score -= 0.5;
            }

            if (isLater(lastCommitDate, task.lastCommit())) {
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

    /**
     * Look for commit date.
     *
     * @param labPath directory of lab.
     * @param branch - where to look for commits.
     * @param type - first or last commit.
     * @return date of commit.
     * @throws IOException - if cant get list of commits.
     * @throws InterruptedException - same.
     */
    private String getCommitDate(Path labPath, String branch,
                                 String type) throws IOException, InterruptedException {
        String commitHash;
        if ("first".equals(type)) {
            commitHash = ProgramUtil.runCommand(labPath, "git",
                    "rev-list", "--max-parents=0", branch).trim();
        } else {
            commitHash = ProgramUtil.runCommand(labPath, "git",
                    "rev-parse", branch).trim();
        }

        return ProgramUtil.runCommand(labPath, "git",
                "show", "-s", "--format=%ci", commitHash).trim();
    }

    /**
     * Compares two dates.
     *
     * @param commitDate - date of commit to compare with deadline.
     * @param deadline - deadline.
     * @return true if commit is late.
     */
    private boolean isLater(String commitDate, LocalDate deadline) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
            LocalDateTime commitDateTime = LocalDateTime.parse(commitDate, formatter);
            LocalDate commitLocalDate = commitDateTime.toLocalDate();
            return !commitLocalDate.isBefore(deadline) && !commitLocalDate.isEqual(deadline);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing dates: " + Arrays.toString(e.getStackTrace()));
            return true;
        }
    }

    /**
     * Parses test results from xml.
     *
     * @param labPath path to lab directory.
     * @param result - where to write results.
     * @throws ParserConfigurationException if cant parse xml.
     * @throws IOException - if no file found.
     * @throws SAXException - parse mistake.
     */
    private void parseTestResults(Path labPath, TaskResult result)
            throws ParserConfigurationException, IOException, SAXException {
        int errors = 0;
        int failed = 0;
        int total = 0;

        File dir = new File(labPath.toString()
                + "/build/test-results/test");

        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Директория с результатами тестов не найдена: "
                    + dir.getAbsolutePath());
            result.setTestsPassed(0);
            result.setTestsFailed(0);
            result.setTestsError(0);
            return;
        }

        // Проверяем наличие файлов в директории
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("В директории "
                    + dir.getAbsolutePath() + " нет файлов");
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

    /**
     * Runs gradlew with args.
     *
     * @param projectPath where to run command.
     * @param commands - args.
     * @return true if command was successful.
     * @throws IOException - process exception.
     * @throws InterruptedException - same.
     */
    private boolean runGradleCommand(Path projectPath, String... commands)
            throws IOException, InterruptedException {
        String gradlew = projectPath.resolve(System.getProperty("os.name")
                .toLowerCase().contains("win") ?
            "gradlew.bat" : "gradlew").toString();

        // Make gradlew executable on Unix systems
        if (!System.getProperty("os.name")
                .toLowerCase().contains("win")) {
            new ProcessBuilder("chmod", "+x", gradlew)
                    .start().waitFor();
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
