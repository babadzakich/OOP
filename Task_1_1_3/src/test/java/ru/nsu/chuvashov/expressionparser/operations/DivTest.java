package ru.nsu.chuvashov.expressionparser.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

class DivTest {

    /**
     * Test for dividing by zero.
     */
    @Test
    void eval1() {
        boolean flag = false;
        try {
            new Div(new Number(5), new Sub(new Number(5), new Number(5))).eval("");
        } catch (Exception e) {
            flag = true;
        } finally {
            assertTrue(flag);
        }
    }

    /**
     * Simple test for division.
     */
    @Test
    void eval2() {
        double result;
        try {
            result = new Div(new Number(10),
                    new Add(new Variable("X"),
                            new Number(3))).eval("X = 2");
        } catch (Exception e) {
            result = 1;
        }

        assertEquals(2, result);
    }

    /**
     * Derivative calculating process.
     */
    @Test
    void derivative() throws Exception {
        Expression der = new Div(new Mul(new Number(5), new Variable("X")),
                new Number(3)).derivative("X");
        assertEquals((double) 15 / (double) 9,
                der.eval("X = 3"));
    }

    /**
     * Test for printing expression.
     */
    @Test
    void testPrint() {
        final OutputStream saveOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Expression e = new Div(new Number(5), new Variable("X"));
        e.print();
        assertEquals("(5.0 / X)", out.toString());
        System.setOut(new PrintStream(saveOut));
    }

    @Test
    void testSimplify() throws Exception {
        Expression e = new Div(new Number(10), new Number(2));
        Expression e2 = e.simplification();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        e2.print();
        assertEquals("5.0", out.toString());

        e = new Div(new Number(5), new Variable("X"));
        try {
            e.simplification();
        } catch (Exception ex) {
            assertInstanceOf(Exception.class, ex);
        }

        e = new Div(new Variable("X"), new Number(3));
        try {
            e.simplification();
        } catch (Exception ex) {
            assertInstanceOf(Exception.class, ex);
        }

        e = new Div(new Number(2), new Number(0));
        try {
            e.simplification();
        } catch (Exception ex) {
            assertInstanceOf(ArithmeticException.class, ex);
        }

        out.reset();
        Expression e4 = new Div(new Number(0), new Number(1212));
        Expression e5 = e4.simplification();
        e5.print();
        assertEquals("0.0", out.toString());
        System.setOut(new PrintStream(saveOut));
    }
}