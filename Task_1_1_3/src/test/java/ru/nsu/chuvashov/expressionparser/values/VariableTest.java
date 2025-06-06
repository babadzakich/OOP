package ru.nsu.chuvashov.expressionparser.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.operations.Add;

/**
 * Class for variable test.
 */
class VariableTest {

    /**
     * Test for all eval troubles,
     * like empty variables, wrong format,
     * and lack of variable.
     */
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
            assertInstanceOf(IllegalArgumentException.class, e);
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

    /**
     * Test for print.
     */
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

    /**
     * Test for taking derivative by variable
     * that differs from our variable.
     */
    @Test
    void derivativeOther() {
        Expression e = new Variable("X");
        Expression e2 = e.derivative("Y");
        assertEquals(e, e2);
    }

    /**
     * Checking for exception when we insert
     * empty line in derivative.
     */
    @Test
    void derivativeEmpty() {
        Expression e = new Variable("X");
        try {
            e.derivative("");
        } catch (Exception x) {
            assertInstanceOf(IllegalArgumentException.class, x);
        }
    }

    /**
     * Just derivative check.
     */
    @Test
    void derivative() {
        Expression e = new Variable("X");
        Expression e2 = e.derivative("X");
        assertEquals(new Number(1), e2);
    }

    @Test
    void equalsCheck() {
        Expression e = new Variable("X");
        assertNotEquals(e, null);
    }
}