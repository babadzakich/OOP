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

class MulTest {

    @Test
    void simplification() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));

        Expression e = new Mul(new Number(0), new Number(4));
        Expression e2 = new Mul(new Number(1), new Number(4));
        Expression e3 = new Mul(new Variable("X"), new Number(0));
        Expression e4 = new Mul(new Variable("Y"), new Number(1));
        Expression e5 = new Mul(new Variable("Z"), new Variable("X"));
        Expression e6 = new Mul(new Number(3), new Number(0));
        Expression e7 = new Mul(new Number(3), new Number(1));
        Expression e8 = new Mul(new Number(3), new Variable("Y"));
        Expression e9 = new Mul(new Number(3), new Number(2));

        Expression simple = e.simplification();
        simple.print();
        assertEquals("0.0", out.toString());
        out.reset();

        simple = e2.simplification();
        simple.print();
        assertEquals("4.0", out.toString());
        out.reset();

        simple = e3.simplification();
        simple.print();
        assertEquals("0.0", out.toString());
        out.reset();

        simple = e4.simplification();
        simple.print();
        assertEquals("Y", out.toString());
        out.reset();

        try {
            e5.simplification();
        } catch (Exception ex) {
            assertInstanceOf(Exception.class, ex);
        } finally {
            out.reset();
        }

        simple = e6.simplification();
        simple.print();
        assertEquals("0.0", out.toString());
        out.reset();

        simple = e7.simplification();
        simple.print();
        assertEquals("3.0", out.toString());
        out.reset();

        try {
            e8.simplification();
        } catch (Exception ex) {
            assertInstanceOf(Exception.class, ex);
        } finally {
            out.reset();
        }

        simple = e9.simplification();
        out.reset();
        simple.print();
        assertEquals("6.0", out.toString());
        System.setOut(new PrintStream(saveOut));
    }
}