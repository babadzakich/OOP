package ru.nsu.chuvashov.expressionparser.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

/**
 * Test class for adding operator.
 */
class AddTest {

    /**
     * Checking add.
     *
     * @throws Exception if variable is not presented in eval.
     */
    @Test
    void eval() throws Exception {
        Expression expression = new Add(new Number(5), new Variable("X"));
        assertEquals(10, expression.eval("X = 5"));

        expression = new Add(new Mul(new Variable("X"), new Number(3)), new Variable("X"));
        assertEquals(40, expression.eval("X = 10"));
    }

    @Test
    void derivative() {
        Expression expression = new Add(new Number(5), new Variable("X"));
        assertEquals("(0.0 + 1.0)", expression.derivative("X").toString());

        expression = new Add(new Mul(new Variable("X"), new Number(3)), new Variable("X"));
        String expected = "(((1.0 * 3.0) + (X * 0.0)) + 1.0)";
//        assertEquals(expected, expression.derivative("X").toString());
    }

    @Test
    void testToString() {
        Expression e = new Add(new Number(5), new Variable("X"));
        Expression e2 = new Add(new Add(new Number(5), new Variable("X")), new Variable("X"));
        Expression e3 = new Add(new Number(5), new Add(new Variable("X"), new Variable("X")));
        assertEquals("(5.0 + X)", e.toString());
        assertEquals("((5.0 + X) + X)", e2.toString());
        assertEquals("(5.0 + (X + X))", e3.toString());
    }
}