package ru.nsu.chuvashov.expressionparser;

/**
 * Class for parsing expression from string.
 */
public class Parser {
    private static Parser parser = null;
    private TokenType type;
    private char lastChar = '\0';
    private String input;
    private int inIndex;
    private boolean end = false;
    private Expression expression;

    private Parser() {
        type = TokenType.NUMBER;
        inIndex = 0;
    };

    public static Parser getParser() {
        if(parser == null) {
            parser = new Parser();
        }
        return parser;
    }

    public Expression parseExpression(String inn) {
        input = inn;
        advance();
        return add();
    }

    private void advance() {
        if (end) {
            type = TokenType.TOKENEOF;
            return;
        }
        char c;
        if (lastChar == '\0') {
            c = input.charAt(inIndex++);
        } else {
            c = lastChar;
            lastChar = '\0';
        }

        while (Character.isWhitespace(c)) {
            c = input.charAt(inIndex++);
            if (c == '\0' || c == '\n') {
                type = TokenType.TOKENEOF;
                return;
            }
        }

        if (c == '+') {
            type = TokenType.TOKENADD;
        } else if (c == '-') {
            type = TokenType.TOKENSUB;
        } else if (c == '*') {
            type = TokenType.TOKENMUL;
        } else if (c == '/') {
            type = TokenType.TOKENDIV;
        } else if (c == '(') {
            type = TokenType.OPENBB;
        } else if (c == ')') {
            type = TokenType.CLOSEDBB;
        } else {
            if (Character.isDigit(c)) {
                type = TokenType.NUMBER;
                double value = 0;
                while (Character.isDigit(c)) {
                    value = value * 10 + (c - '0');
                    if (inIndex == input.length()) {
                        break;
                    }
                    c = input.charAt(inIndex++);
                }
                expression = new Number(value);
            } else {
                type = TokenType.VARIABLE;
                StringBuilder var = new StringBuilder();

                while (Character.isLetter(c) && inIndex < input.length()) {
                    var.append(c);
                    c = input.charAt(inIndex++);
                }
                expression = new Variable(var.toString());
            }

            lastChar = c;
            if (inIndex == input.length()) {
                end = true;
            }
        }
    }

    private Expression add() {
        Expression exp = mul();
        while (true) {
            switch (type) {
                case TOKENADD:
                    advance();
                    exp = new Add(exp, mul());
                    break;
                case TOKENSUB:
                    advance();
                    exp = new Sub(exp, mul());
                    break;
                default:
                    return exp;
            }

        }
    }

    private Expression mul() {
        Expression exp = constant();
        while (true) {
            switch (type) {
                case TOKENMUL:
                    advance();
                    exp = new Mul(exp, constant());
                    break;
                case TOKENDIV:
                    advance();
                    exp = new Div(exp, constant());
                    break;
                default:
                    return exp;
            }
        }
    }

    private Expression constant() {
        if (type == TokenType.NUMBER || type == TokenType.VARIABLE) {
            Expression exp = expression;
            advance();
            return exp;
        }
        return grouping();
    }

    private Expression grouping() {
        advance();
        Expression exp = expression;
        advance();

        return exp;
    }
}
