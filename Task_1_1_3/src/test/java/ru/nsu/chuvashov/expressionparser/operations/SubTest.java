package ru.nsu.chuvashov.expressionparser.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
     */
    @Test
    void eval() {
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
     */
    @Test
    void derivative() {
        Expression expression = new Sub(new Number(2), new Variable("X"));
        assertEquals(-1, expression.derivative("X").eval("X = 2"));
    }

    @Test
    void testSimplify() {
        Expression e = new Sub(new Number(10), new Number(2));
        Expression e2 = e.simplification();
        assertEquals(new Number(8), e2);

        Expression e3 = new Sub(new Number(2), new Number(2));
        Expression e4 = e3.simplification();
        assertEquals(new Number(0), e4);

        e = new Sub(new Number(5), new Variable("X"));
        assertEquals(e, e.simplification());

        e = new Sub(new Variable("X"), new Number(1));
        assertEquals(e, e.simplification());
    }

    @Test
    void equalsCheck() {
        Expression e = new Sub(new Number(5), new Variable("X"));
        Expression e2 = new Sub(new Number(5), new Variable("X"));
        assertEquals(e, e2);
        assertEquals(e, e);
        assertNotEquals(e, null);

        Expression e3 = new Add(new Number(5), new Variable("X"));
        assertNotEquals(e, e3);

        Expression e4 = new Sub(new Number(4), new Variable("X"));
        assertNotEquals(e, e4);

        Expression e5 = new Sub(new Number(5), new Variable("Y"));
        assertNotEquals(e, e5);
    }

    @Test
    void hashCheck() {
        Expression e = new Sub(new Number(5), new Variable("X"));
        assertEquals(-1033102689, e.hashCode());
    }
}