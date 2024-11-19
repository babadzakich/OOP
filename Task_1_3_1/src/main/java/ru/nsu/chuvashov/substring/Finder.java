package ru.nsu.chuvashov.substring;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to implement reading from file.
 */
public class Finder {
    /**
     * We read from file by chunks and work with them using KMP.
     * A chunk is 8 kilobyte.
     *
     * @param file - file where we search.
     * @param pattern - what we search.
     * @return positions of patterns in file.
     * @throws IOException if file is not presented.
     */
    public static List<Integer> find(String file, String pattern) throws IOException {
        InputStream inputStream = Finder.class.getClassLoader().getResourceAsStream(file);
        if (inputStream == null) {
            inputStream = new FileInputStream(file);
        }
        int bufferSize = 8192;
        List<Integer> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        int filesRead = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                StandardCharsets.UTF_8));
        char[] buffer = new char[bufferSize];
        int bytesRead;

        while ((bytesRead = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, bytesRead);
            KnuthMorrisPratt.getPattern(stringBuilder.toString(), pattern, result, filesRead);
            filesRead += stringBuilder.length() - pattern.length();
            if (stringBuilder.length() > pattern.length()) {
                stringBuilder.delete(0, stringBuilder.length() - pattern.length());
            }
        }
        return result;
    }
}
