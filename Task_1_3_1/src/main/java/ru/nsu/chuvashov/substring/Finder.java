package ru.nsu.chuvashov.substring;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Finder {
    public List<Integer> find(String file, String pattern) throws IOException {
        InputStream inputStream;
        inputStream = Finder.class.getClassLoader().getResourceAsStream(file);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + file);
        }
        int BUFFER_SIZE = 8192;
        List<Integer> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        char[] buffer = new char[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, bytesRead);
            KnuthMorrisPratt.getPattern(stringBuilder.toString(), pattern, result);

            if (stringBuilder.length() > pattern.length()) {
                stringBuilder.delete(0, stringBuilder.length() - pattern.length());
            }
        }
        return result;
    }
}
