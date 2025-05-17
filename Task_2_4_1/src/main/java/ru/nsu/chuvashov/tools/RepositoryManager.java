package ru.nsu.chuvashov.tools;

import ru.nsu.chuvashov.configholder.Configuration;
import ru.nsu.chuvashov.configholder.Group;
import ru.nsu.chuvashov.configholder.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class RepositoryManager {
    private final static String GIT = System.getProperty("os.name")
            .toLowerCase().contains("win") ? "git.exe" : "git";
    private final static String TESTER_WORKSPACE = "/tmp/tester_workspace";

    public RepositoryManager(Configuration config) {
        try {
            createDirectoryIfNotExists(Path.of(TESTER_WORKSPACE));
            for (Group group : config.groups()) {
                for (Student student : group.getStudents()) {
                    try {
                        Path studentPath = Path.of(TESTER_WORKSPACE, String.valueOf(group.getGroupNumber()), student.username());
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

    private void cloneRepository(String url, Path path) throws IOException {
        createDirectoryIfNotExists(path);

        Process process = new ProcessBuilder()
            .command(GIT, "clone", url, path.toString())
            .start();
        waitForProcess(process);
    }

    private void createDirectoryIfNotExists(Path path) throws IOException {
        if (!path.toFile().exists() && !path.toFile().mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }
    }

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

//    public void validateGitConfig() throws IOException, InterruptedException {
//        String credentialHelper = ProgramUtil.runCommand(Path.of("."), "git", "config", "--global", "credential.helper");
//        if (!credentialHelper.trim().equals("store")) {
//            throw new IOException("Git credential helper is not configured for password-less access");
//        }
//    }

    public void stopWork() {
        try {
            Files.walk(Paths.get(TESTER_WORKSPACE)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
