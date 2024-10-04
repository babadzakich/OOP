package ru.nsu.chuvashov.expressionparser.backusnaurparser;

/**
 * Enum for types of expression in token.
 */
public enum TokenType {
    
    TOKENADD(0),
    TOKENSUB(1),
    TOKENMUL(2),
    TOKENDIV(3),
    OPENBB(4),
    CLOSEDBB(5),
    NUMBER(6),
    VARIABLE(7),
    TOKENEOF(8),
    STARTPOINT(9);

    /**
     * Constructor.
     *
     * @param i = value for enumeration.
     */
    TokenType(int i) {
    }
}
