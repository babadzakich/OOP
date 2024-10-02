package ru.nsu.chuvashov.expressionparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    @Test
    void eval() {
        boolean flag = false;
        try {
            double result = new Div(new Number(5), new Sub(new Number(5), new Number(5))).eval("");
        } catch (Exception e) {
            flag = true;
        } finally {
            assertTrue(flag);
        }
    }

    @Test
    void print() {
        String expression = new Div(new Number(10), new Mul(new Number(1), new Number(2))).toString();
        assertEquals("(10.0 / (1.0 * 2.0))", expression);
    }

    @Test
    void derivative() {
        Expression der = new Div(new Mul(new Number(5), new Variable("X")), new Number(3)).derivative("X");
        assertEquals("(((((0.0 * X) + (5.0 * 1.0)) * 3.0) + ((5.0 * X) * 0.0)) / (3.0 * 3.0))", der.toString());
    }
}