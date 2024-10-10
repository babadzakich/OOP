package ru.nsu.chuvashov.expressionparser.values;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class NumberTest {

    /**
     * Test for printing expression.
     */
    @Test
    void testPrint() {
        final OutputStream saveOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Expression e = new Number(5);
        e.print();
        assertEquals("5.0", out.toString());
        System.setOut(new PrintStream(saveOut));
    }

    @Test
    void equalsCheck() {
        Expression e = new Number(5);
        Expression e2 = new Number(5);
        assertEquals(e, e2);
        assertEquals(e,e);
        assertNotEquals(e, null);

        Expression e3 = new Number(4);
        assertNotEquals(e, e3);

        Expression e4 = new Variable("X");
        assertNotEquals(e, e4);
    }

    @Test
    void testHashCode() {
        Expression e = new Number(5);
        assertEquals(1075052544, e.hashCode());
    }
}