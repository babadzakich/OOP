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

class MulTest {

    /**
     * Test for printing expression.
     */
    @Test
    void testPrint() {
        final OutputStream saveOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Expression e = new Mul(new Number(5), new Variable("X"));
        e.print();
        assertEquals("(5.0 * X)", out.toString());
        System.setOut(new PrintStream(saveOut));
    }

    @Test
    void simplification() {
        Expression e = new Mul(new Number(0), new Number(4));
        Expression simple = e.simplification();
        assertEquals(new Number(0), simple);

        e = new Mul(new Number(0), new Variable("X"));
        simple = e.simplification();
        assertEquals(new Number(0), simple);

        Expression e2 = new Mul(new Number(1), new Number(4));
        simple = e2.simplification();
        assertEquals(new Number(4), simple);

        e2 = new Mul(new Number(1), new Variable("X"));
        simple = e2.simplification();
        assertEquals(new Variable("X"), simple);

        Expression e3 = new Mul(new Variable("X"), new Number(0));
        simple = e3.simplification();
        assertEquals(new Number(0), simple);

        e3 = new Mul(new Variable("X"), new Number(2));
        simple = e3.simplification();
        assertEquals(e3, simple);

        Expression e4 = new Mul(new Variable("Y"), new Number(1));
        simple = e4.simplification();
        assertEquals(new Variable("Y"), simple);

        Expression e5 = new Mul(new Variable("Z"), new Variable("X"));
        assertEquals(e5, e5.simplification());

        Expression e6 = new Mul(new Number(3), new Number(0));
        simple = e6.simplification();
        assertEquals(new Number(0), simple);

        Expression e7 = new Mul(new Number(3), new Number(1));
        simple = e7.simplification();
        assertEquals(new Number(3), simple);

        Expression e8 = new Mul(new Number(3), new Variable("Y"));
        assertEquals(e8, e8.simplification());

        Expression e9 = new Mul(new Number(3), new Number(2));
        simple = e9.simplification();
        assertEquals(new Number(6), simple);
    }

    @Test
    void equalsCheck() {
        Expression e = new Mul(new Number(5), new Variable("X"));
        Expression e2 = new Mul(new Number(5), new Variable("X"));
        assertEquals(e, e2);
        assertEquals(e, e);
        assertNotEquals(e, null);

        Expression e3 = new Add(new Number(5), new Variable("X"));
        assertNotEquals(e, e3);

        Expression e4 = new Mul(new Number(4), new Variable("X"));
        assertNotEquals(e, e4);

        Expression e5 = new Mul(new Number(5), new Variable("Y"));
        assertNotEquals(e, e5);
    }

    @Test
    void hashCheck() {
        Expression e = new Mul(new Number(5), new Variable("X"));
        assertEquals(-1033100767, e.hashCode());
    }
}