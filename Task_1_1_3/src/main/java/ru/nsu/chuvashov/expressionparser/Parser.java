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
    }

    /**
     * Making of Singleton.
     *
     * @return Singletone instance.
     */
    public static Parser getParser() {
        if (parser == null) {
            parser = new Parser();
        }
        return parser;
    }

    /**
     * Public method that gets string and
     * starts its parsing.
     *
     * @param inn - input string.
     * @return parsed string in Expression class.
     */
    public Expression parseExpression(String inn) {
        input = inn;
        advance();
        return add();
    }

    /**
     * method that skips all whitespaces and
     * checks for type of current token,
     * it can be plus or minus or even number or variable,
     * and we parse number or variable getting it from string.
     */
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

                while (Character.isLetter(c)) {
                    var.append(c);
                    if (inIndex == input.length()) {
                        break;
                    }
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

    /**
     * The summary has the lowest priority
     * among other operations so we start from it,
     * making new Expression Add with requrcively,
     * calling mul so we look for operation with higher priority.
     *
     * @return new Add.
     */
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

    /**
     * We are making multiplication with number so.
     *
     * @return
     */
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
        Expression exp = add();
        advance();

        return exp;
    }
}
