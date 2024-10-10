package ru.nsu.chuvashov.expressionparser.operations;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(e, e.simplification());

        e = new Add(new Variable("X"), new Number(5));
        assertEquals(e, e.simplification());
    }

    @Test
    void equalsCheck() {
        Expression e = new Add(new Number(5), new Variable("X"));
        Expression e2 = new Add(new Number(5), new Variable("X"));
        assertEquals(e, e2);
        assertEquals(e,e);
        assertNotEquals(e, null);

        Expression e3 = new Sub(new Number(5), new Variable("X"));
        assertNotEquals(e, e3);

        Expression e4 = new Add(new Number(4), new Variable("X"));
        assertNotEquals(e, e4);

        Expression e5 = new Add(new Number(5), new Variable("Y"));
        assertNotEquals(e, e5);
    }

    @Test
    void hashCheck() {
        Expression e = new Add(new Number(5), new Variable("X"));
        assertEquals(-1033102689, e.hashCode());
    }
}