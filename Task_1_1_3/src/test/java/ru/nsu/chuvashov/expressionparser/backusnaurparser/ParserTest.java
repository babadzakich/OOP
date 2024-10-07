package ru.nsu.chuvashov.expressionparser.backusnaurparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.expressionparser.values.Expression;

class ParserTest {

    @Test
    void getParser() {
        Parser p1 = Parser.getParser();
        Parser p2 = Parser.getParser();
        assertEquals(p1, p2);
    }

    @Test
    void parseExpression() throws Exception {
        Parser p1 = Parser.getParser();
        Expression e1 = p1.parseExpression("2 + 2 * 2 - 2 / 2 + 2 * (3 + 5) + 10 + XA");
        assertEquals(36, e1.eval("XA = 5"));
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
    }
}