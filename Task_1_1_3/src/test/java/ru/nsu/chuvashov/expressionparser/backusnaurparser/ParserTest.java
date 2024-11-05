package ru.nsu.chuvashov.expressionparser.backusnaurparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;

class ParserTest {
    @Test
    void parseExpression() {
        Parser p1 = new Parser();
        Expression e1 = p1.parseExpression("2 + 2 * 2 - 2 / 2 + Y * (3 + 5) + 10 + XA");
        assertEquals(36, e1.eval("XA = 5; Y = 2"));
        try {
            p1.parseExpression("(2+2)(4+4)");
        } catch (Exception e) {
            assertInstanceOf(IllegalStateException.class, e);
        }

        Expression e3 = p1.parseExpression("2452");
        assertEquals(2452, e3.eval(""));

        try {
            p1.parseExpression("12 + + 2");
        } catch (Exception e) {
            assertInstanceOf(IllegalStateException.class, e);
        }

        try {
            p1.parseExpression("A B");
        } catch (Exception e) {
            assertInstanceOf(IllegalStateException.class, e);
        }

        try {
            p1.parseExpression("B(2)");
        } catch (Exception e) {
            assertInstanceOf(IllegalStateException.class, e);
        }
    }
}