package ru.nsu.chuvashov.expressionparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.operations.Add;
import ru.nsu.chuvashov.expressionparser.operations.Div;
import ru.nsu.chuvashov.expressionparser.operations.Mul;
import ru.nsu.chuvashov.expressionparser.operations.Sub;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

class DivTest {

    /**
     * Test for dividing by zero.
     */
    @Test
    void eval1() {
        boolean flag = false;
        try {
            double result = new Div(new Number(5), new Sub(new Number(5), new Number(5))).eval("");
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
     * Test for printing brackets.
     */
    @Test
    void print1() {
        String expression = new Div(new Number(10),
                new Mul(new Number(1), new Number(2))).toString();
        assertEquals("(10.0 / (1.0 * 2.0))", expression);
    }

    /**
     * Another brackets print test.
     */
    @Test
    void print2() {
        String expression = new Div(new Add(new Number(10), new Number(2)),
                new Mul(new Variable("XA"), new Number(2))).toString();
        assertEquals("((10.0 + 2.0) / (XA * 2.0))", expression);
    }

    /**
     * Derivative calculating process.
     */
    @Test
    void derivative() {
        Expression der = new Div(new Mul(new Number(5), new Variable("X")),
                new Number(3)).derivative("X");
        assertEquals("(((((0.0 * X) + "
                        + "(5.0 * 1.0)) * 3.0) +"
                        + " ((5.0 * X) * 0.0)) / (3.0 * 3.0))",
                der.toString());
    }

    @Test
    void testPrint() {
        OutputStream saveOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Expression e = new Div(new Number(5), new Variable("X"));
        e.print();
        assertEquals("(5.0 / X)", out.toString());
        System.setOut(new PrintStream(saveOut));
    }
}