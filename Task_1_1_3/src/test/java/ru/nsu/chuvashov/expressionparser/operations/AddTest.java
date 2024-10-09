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
 * Test class for adding operator.
 */
class AddTest {
    /**
     * We are summarizing 5 and X = 5 and get 10.
     */
    @Test
    void eval() {
        Expression expression = new Add(new Number(5), new Variable("X"));
        assertEquals(10, expression.eval("X = 5"));
    }

    /**
     * We create derivative of (5 + X) and we get (0 + 1).
     */
    @Test
    void derivative() {
        Expression expression = new Add(new Number(5), new Variable("X"));
        assertEquals(new Add(new Number(0), new Number(1)), expression.derivative("X"));
    }

    /**
     * We substitute stdout buffer for our buffer, so e.print() prints
     * result into our array so we can check it.
     */
    @Test
    void testPrint() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        Expression e = new Add(new Number(5), new Variable("X"));
        e.print();
        assertEquals("(5.0 + X)", out.toString());
        System.setOut(new PrintStream(saveOut));

    }

    @Test
    void simplification() {
        Expression e = new Add(new Number(5), new Number(5));
        Expression e2 = e.simplification();
        assertEquals(new Number(10), e2);
        e = new Add(new Number(5), new Variable("X"));
        try {
            e.simplification();
        } catch (Exception ex) {
            assertInstanceOf(Exception.class, ex);
        }
    }
}