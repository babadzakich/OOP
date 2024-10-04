package ru.nsu.chuvashov.expressionparser.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

/**
 * Class for substraction tests.
 */
class SubTest {

    /**
     * Evaluation test.
     *
     * @throws Exception of variables.
     */
    @Test
    void eval() throws Exception {
        Expression expression = new Sub(new Number(228), new Number(42));
        assertEquals(186, expression.eval(""));
    }

    /**
     * Print test.
     */
    @Test
    void print() {
        final OutputStream saveOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Expression e = new Sub(new Number(5), new Variable("X"));
        e.print();
        assertEquals("(5.0 - X)", out.toString());
        System.setOut(new PrintStream(saveOut));
    }

    /**
     * Derivative test.
     *
     * @throws Exception from variable.
     */
    @Test
    void derivative() throws Exception {
        Expression expression = new Sub(new Number(2), new Variable("X"));
        assertEquals(-1, expression.derivative("X").eval("X = 2"));
    }

    @Test
    void testSimplify() throws Exception {
        Expression e = new Sub(new Number(10), new Number(2));
        Expression e2 = e.simplification();
        Expression e3 = new Sub(new Number(2), new Number(2));
        Expression e4 = e3.simplification();

        assertEquals(0, e4.eval(""));

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        e2.print();
        assertEquals("8.0", out.toString());

        e = new Sub(new Number(5), new Variable("X"));
        try {
            e.simplification();
        } catch (Exception ex) {
            assertInstanceOf(Exception.class, ex);
        }
        System.setOut(new PrintStream(saveOut));
    }
}