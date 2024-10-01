package ru.nsu.chuvashov.expressionParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    @Test
    void eval() {
        try {
            double result = new Div(new Number(5), new Number(0)).eval("");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void print() {
        new Div(new Number(10), new Mul(new Number(1), new Number(2))).print();
//        assertTrue();
    }

    @Test
    void derivative() {
    }
}