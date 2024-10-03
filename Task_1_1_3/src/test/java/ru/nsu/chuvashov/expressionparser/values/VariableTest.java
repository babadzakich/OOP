package ru.nsu.chuvashov.expressionparser.values;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.operations.Add;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void eval() {
        Expression expression = new Add(new Variable("X"), new Variable("Y"));
        try {
            expression.eval("");
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        try {
            expression.eval("X = 5");
        } catch (Exception e) {
            assertInstanceOf(NoSuchFieldException.class, e);
        }

        try {
            expression.eval("X = 5 2");
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        double result = 0;
        try {
            result = expression.eval("X = 5; Y = 2");
        } catch (Exception e) {
            System.out.println("Holy hell, how could this happen");
            result = Double.NaN;
        } finally {
            assertEquals(7, result);
        }
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
            e.derivative("");
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