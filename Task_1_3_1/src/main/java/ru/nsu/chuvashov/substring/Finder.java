package ru.nsu.chuvashov.substring;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Finder {
    public ArrayList<Integer> find(String file, String pattern) throws FileNotFoundException {
        InputStream inputStream;
        inputStream = Finder.class.getClassLoader().getResourceAsStream(file);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + file);
        }

        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        return null;
    }
}
