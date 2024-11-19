package ru.nsu.chuvashov.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void mainTest() {
        ByteArrayInputStream in;
        ByteArrayOutputStream byteArrayOutputStream;
        in = new ByteArrayInputStream("test1.txt\nбра\n".getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream input;
        OutputStream output;
        input = System.in;
        output = System.out;
        System.setIn(in);
        System.setOut(new PrintStream(byteArrayOutputStream));
        Main.main(new String[]{});
        System.setIn(input);
        System.setOut(new PrintStream(output));
        assertEquals("[1, 8]\n", byteArrayOutputStream.toString());
    }

    @Test
    void noFileTest() {
        ByteArrayInputStream in;
        ByteArrayOutputStream byteArrayOutputStream;
        in = new ByteArrayInputStream("test10.txt\nLINTER\n".getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream input;
        OutputStream output;
        input = System.in;
        output = System.out;
        System.setIn(in);
        System.setOut(new PrintStream(byteArrayOutputStream));
        Main.main(new String[]{});
        System.setIn(input);
        System.setOut(new PrintStream(output));
        System.out.println(byteArrayOutputStream);
        assertEquals("test10.txt (No such file or directory)\n",
                byteArrayOutputStream.toString());
    }
}