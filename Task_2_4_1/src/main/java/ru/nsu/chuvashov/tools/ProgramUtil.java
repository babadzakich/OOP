package ru.nsu.chuvashov.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Some utility.
 */
public class ProgramUtil {
    /**
     * Runs bash command.
     *
     * @param workingDir where to run.
     * @param command what to run.
     * @return result of command as string.
     * @throws IOException if command failed.
     * @throws InterruptedException - process exception.
     */
    public static String runCommand(Path workingDir, String... command)
            throws IOException, InterruptedException {

        Process p = new ProcessBuilder(command)
                .directory(workingDir.toFile())
                .redirectErrorStream(true)
                .start();

        String output = readOutput(p.getInputStream());
        int exitCode = p.waitFor();

        if (exitCode != 0) {
            throw new IOException("Command failed: "
                    + String.join(" ", command)
                    + "\n" + output);
        }

        return output;
    }

    /**
     * Read from stream.
     *
     * @param is stream.
     * @return data from stream.
     * @throws IOException if cant read.
     */
    private static String readOutput(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
