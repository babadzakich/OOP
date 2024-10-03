package ru.nsu.chuvashov.expressionparser.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
     * Checking add.
     *
     * @throws Exception if variable is not presented in eval.
     */
    @Test
    void eval() throws Exception {
        Expression expression = new Add(new Number(5), new Variable("X"));
        assertEquals(10, expression.eval("X = 5"));
    }

    /**
     * Derivative creation test.
     */
    @Test
    void derivative() throws Exception {
        Expression expression = new Add(new Number(5), new Variable("X"));
        assertEquals(1, expression.derivative("X").eval(""));
    }

    /**
     * Print test.
     */
    @Test
    void testPrint() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        Expression e = new Add(new Number(5), new Variable("X"));
        e.print();
        assertEquals("(5.0 + X)", out.toString());
        System.setOut(new PrintStream(saveOut));
    }
}