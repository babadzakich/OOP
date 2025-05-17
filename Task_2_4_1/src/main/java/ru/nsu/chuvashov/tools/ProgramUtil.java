package ru.nsu.chuvashov.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ProgramUtil {
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

    private static String readOutput(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
