package ru.nsu.chuvashov.expressionparser.operations;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    void derivative() {
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
    void testSimplify() {
        Expression e = new Div(new Number(10), new Number(2));
        Expression e2 = e.simplification();

        assertEquals(new Number(5), e2);

        e = new Div(new Number(5), new Variable("X"));
        e2 = e.simplification();
        assertEquals(e, e2);

        e = new Div(new Variable("X"), new Number(3));
        assertEquals(e, e.simplification());

        e = new Div(new Number(2), new Number(0));
        try {
            e.simplification();
        } catch (ArithmeticException ex) {
            assertInstanceOf(ArithmeticException.class, ex);
        }

        e = new Div(new Number(5), new Number(1));
        assertEquals(new Number(5), e.simplification());

        Expression e4 = new Div(new Number(0), new Number(1212));
        Expression e5 = e4.simplification();
        assertEquals(new Number(0), e5);
    }

    @Test
    void equalsCheck() {
        Expression e = new Div(new Number(5), new Variable("X"));
        Expression e2 = new Div(new Number(5), new Variable("X"));
        assertEquals(e, e2);
        assertEquals(e,e);
        assertNotEquals(e, null);

        Expression e3 = new Sub(new Number(5), new Variable("X"));
        assertNotEquals(e, e3);

        Expression e4 = new Div(new Number(4), new Variable("X"));
        assertNotEquals(e, e4);

        Expression e5 = new Div(new Number(5), new Variable("Y"));
        assertNotEquals(e, e5);
    }

    @Test
    void hashCheck() {
        Expression e = new Div(new Number(5), new Variable("X"));
        assertEquals(-1033104611, e.hashCode());
    }
}