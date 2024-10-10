package ru.nsu.chuvashov.expressionparser.backusnaurparser;

import ru.nsu.chuvashov.expressionparser.operations.Add;
import ru.nsu.chuvashov.expressionparser.operations.Div;
import ru.nsu.chuvashov.expressionparser.operations.Mul;
import ru.nsu.chuvashov.expressionparser.operations.Sub;
import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;
import ru.nsu.chuvashov.expressionparser.values.Variable;

/**
 * Class for parsing expression from string.
 */
public class Parser {
    private TokenType type;
    private char lastChar;
    private String input;
    private int inIndex;
    private boolean end;
    private Expression expression;

    public Parser() {
        initParser();
    }

    private void initParser() {
        type = TokenType.STARTPOINT;
        inIndex = 0;
        lastChar = '\0';
        end = false;
    }

    /**
     * Public method that gets string and
     * starts its parsing.
     *
     * @param inn - input string.
     * @return parsed string in Expression class.
     */
    public Expression parseExpression(String inn) {
        initParser();
        input = inn.trim();
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
            type = TokenType.valueOf("TOKENEOF");
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
        }

        if (inIndex >= input.length()) {
            end = true;
            return;
        }

        checkToken(c);

        switch (c) {
            case ('+'):
                type = TokenType.valueOf("TOKENADD");
                break;
            case ('-'):
                type = TokenType.valueOf("TOKENSUB");
                break;
            case ('*'):
                type = TokenType.valueOf("TOKENMUL");
                break;
            case ('/'):
                type = TokenType.valueOf("TOKENDIV");
                break;
            case ('('):
                type = TokenType.valueOf("OPENBB");
                break;
            case (')'):
                type = TokenType.valueOf("CLOSEDBB");
                break;
            default:
                if (Character.isDigit(c)) {
                    type = TokenType.valueOf("NUMBER");
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
                break;
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
     * @return multiplication.
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

    /**
     * We check, whether what we parse right now is Number or Variable,
     * if it`s wrong, then it`s opened bracket, so we start grouping,
     * what is inside brackets.
     *
     * @return Number or Variable, else we return result of parsing expression
     * inside brackets.
     */
    private Expression constant() {
        if (type == TokenType.NUMBER || type == TokenType.VARIABLE) {
            Expression exp = expression;
            advance();
            return exp;
        }
        return grouping();
    }

    /**
     * We parse expressions inside brackets.
     *
     * @return parsed expression from brackets.
     */
    private Expression grouping() {
        advance();
        Expression exp = add();
        advance();

        return exp;
    }

    /**
     * Check for wrong format of input string like (2+2)(3+3),
     * or 2 + + 3.
     *
     * @param c - current symbol that we parse.
     * @throws IllegalStateException if we have wrong input format.
     */
    private void checkToken(char c) throws IllegalStateException {
        if (Character.isDigit(c) || Character.isLetter(c)) {
            return;
        }
        if ((!type.typeCheck() && c != '(')
                || (type.typeCheck() && c == '(')) {
            throw new IllegalStateException("Wrong order"
                    + "for " + c);
        }
    }
}