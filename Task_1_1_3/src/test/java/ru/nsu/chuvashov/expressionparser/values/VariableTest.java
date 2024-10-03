package ru.nsu.chuvashov.expressionparser.values;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void eval() {
    }

    @Test
    void print() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        Expression e = new Variable("X");
        e.print();
        assertEquals("X", out.toString());
        System.setOut(new PrintStream(saveOut));
    }

    @Test
    void derivativeOther() throws Exception {
        Expression e = new Variable("X");
        Expression e2 = e.derivative("Y");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        e2.print();
        assertEquals("X", out.toString());
        System.setOut(new PrintStream(saveOut));
    }

    @Test
    void derivativeEmpty() {
        boolean flag = false;
        Expression e = new Variable("X");
        try {
            Expression e2 = e.derivative("");
        } catch (Exception x) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    void derivative() throws Exception {
        Expression e = new Variable("X");
        Expression e2 = e.derivative("X");
        assertEquals(1, e2.eval(""));
    }
}