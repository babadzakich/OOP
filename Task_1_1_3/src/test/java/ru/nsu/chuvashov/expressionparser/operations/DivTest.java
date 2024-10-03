package ru.nsu.chuvashov.expressionparser.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * Test for printing expression.
     */
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