package ru.nsu.chuvashov.tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import ru.nsu.chuvashov.antlrconfig.configholder.Configuration;
import ru.nsu.chuvashov.antlrconfig.configholder.Group;
import ru.nsu.chuvashov.antlrconfig.configholder.Student;

/**
 * Class for repository management.
 */
public class RepositoryManager {
    private final String git = System.getProperty("os.name")
            .toLowerCase().contains("win") ? "git.exe" : "git";
    private final String testerWorkspace = "/tmp/tester_workspace";

    /**
     * Constructor that clones repositories of students for check.
     *
     * @param config config data.
     */
    public RepositoryManager(Configuration config) {
        try {
            createDirectoryIfNotExists(Path.of(testerWorkspace));
            for (Group group : config.groups()) {
                for (Student student : group.getStudents()) {
                    try {
                        Path studentPath = Path.of(testerWorkspace,
                                String.valueOf(group.getGroupNumber()), student.username());
                        System.out.println(studentPath);
                        cloneRepository(student.repo(), studentPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating tester workspace: " + e.getMessage());
        }
    }

    /**
     * Clone method.
     *
     * @param url - which repo to clone.
     * @param path - where to clone.
     * @throws IOException if cant create a directory.
     */
    private void cloneRepository(String url, Path path) throws IOException {
        createDirectoryIfNotExists(path);

        Process process = new ProcessBuilder()
            .command(git, "clone", url, path.toString())
            .start();
        waitForProcess(process);
    }

    /**
     * Creates directory.
     *
     * @param path of which directory to create.
     * @throws IOException if cant create dir.
     */
    private void createDirectoryIfNotExists(Path path) throws IOException {
        if (!path.toFile().exists() && !path.toFile().mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }
    }

    /**
     * Wait for process.
     *
     * @param process to wait for.
     * @throws IOException if cant clone.
     */
    private void waitForProcess(Process process) throws IOException {
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String output = readOutput(process.getInputStream());
                throw new IOException("Repository clone failed" + "\n" + output);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Process interrupted", e);
        }
    }

    /**
     * Reads data from stream.
     *
     * @param inputStream from where to read data.
     * @return data.
     * @throws IOException if cant read data.
     */
    private String readOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        return output.toString();
    }

    /**
     * Deletes directory.
     */
    public void stopWork() {
        try {
            Files.walk(Paths.get(testerWorkspace))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
