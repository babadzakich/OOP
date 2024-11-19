package ru.nsu.chuvashov.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    void testBigAssData() throws IOException {
        String line = "All work and no plаy делает Джека унылым!\n";
        String p = "src/main/resources/temp";
        Path path = Paths.get(p);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        try {
            Path temp = Files.createTempFile(path, "temp", ".txt");
            temp.toFile().deleteOnExit();
            Files.write(temp, line.repeat(1000).getBytes(StandardCharsets.UTF_8));
            List<Integer> res = Finder.find("temp/"+temp.getFileName().toString(), "A");
            System.out.println(res);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    void testReallyyBig() throws IOException {
        int maxSize = 100000;
        File newFile = new File("file4.txt");
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
        newFile.delete();
    }
}