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

    public boolean checkForNotNumOrOpenBB() {
        return this.equals(TokenType.NUMBER)
                || this.equals(TokenType.CLOSEDBB)
                || this.equals(TokenType.VARIABLE);
    }
}
