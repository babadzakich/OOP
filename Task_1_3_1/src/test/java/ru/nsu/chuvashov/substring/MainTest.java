package ru.nsu.chuvashov.substring;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void mainTest() {
        InputStream input = System.in;
        OutputStream output = System.out;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream("test1.txt\nбра\n".getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        System.setOut(new PrintStream(byteArrayOutputStream));
        Main.main(new String[]{});
        System.setIn(input);
        System.setOut(new PrintStream(output));
        assertEquals("[1, 8]\n", byteArrayOutputStream.toString());
    }
}