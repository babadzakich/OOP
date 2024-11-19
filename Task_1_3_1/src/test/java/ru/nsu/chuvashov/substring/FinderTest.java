package ru.nsu.chuvashov.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class FinderTest {

    @Test
    void find() {
        try {
            List<Integer> res = Finder.find("test1.txt", "бра");
            List<Integer> expected = List.of(1, 8);
            assertEquals(expected, res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGamlet() {
        try {
            List<Integer> res = Finder.find("test2.txt", "in");
            assertEquals(1867, res.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testStrugatskie() {
        try {
            List<Integer> res = Finder.find("test3.txt", "было");
            assertEquals(182, res.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testBigAssData() {
        String line = "⇑⇓⇑⇓⇐⇒⇐⇒AB";
        String line2 = "Примите лабу ☞☜ ☞☜ ☞☜\n";
        try {
            File temp = File.createTempFile( "temp", ".txt");
            temp.deleteOnExit();
            FileWriter writer = new FileWriter(temp);
            for (int i = 0; i < 60000000; i++) {
                if (i == 20234) {
                    writer.write("♚");
                }
                if (i % 2 == 1) {
                    writer.append(line2);
                } else {
                    writer.append(line);
                }
            }
            writer.close();
            List<Integer> res = Finder.find(temp.toString(), "♚");
            List<Integer> expected = new ArrayList<>();
            expected.add(323744);
            assertEquals(expected, res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testReallyyBig() throws IOException {
        int maxSize = 100000;
        File newFile = new File("file4.txt");
        newFile.deleteOnExit();
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(newFile), StandardCharsets.UTF_8);

        char[] chunk = new char[maxSize];
        Arrays.fill(chunk, 'h');

        for (int i = 0; i < 10000; i++) {
            fileWriter.write(chunk);
        }

        fileWriter.write("hello");

        Arrays.fill(chunk, 'l');
        for (int i = 0; i < 10000; i++) {
            fileWriter.write(chunk);
        }

        fileWriter.close();

        String pattern = "hello";
        List<Integer> resultBoyerMoore =  Finder.find("file4.txt", pattern);
        List<Integer> excepted = new ArrayList<>();
        excepted.add(1000000000);
        assertEquals(excepted, resultBoyerMoore);
    }
}