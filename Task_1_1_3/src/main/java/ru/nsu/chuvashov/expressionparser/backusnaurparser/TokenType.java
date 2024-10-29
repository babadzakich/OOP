package ru.nsu.chuvashov.expressionparser.backusnaurparser;

/**
 * Enum for types of expression in token.
 */
public enum TokenType {
    
    TOKENADD,
    TOKENSUB,
    TOKENMUL,
    TOKENDIV,
    OPENBB,
    CLOSEDBB,
    NUMBER,
    VARIABLE,
    TOKENEOF,
    STARTPOINT;

    /**
     * We need this check for handling problems with wrong order of symbols.
     *
     * @return true if our token is Number or variable or closing bracket.
     */
    public boolean typeCheck() {
        return this == TokenType.NUMBER
                || this == TokenType.CLOSEDBB
                || this == TokenType.VARIABLE;
    }
}
