package ru.nsu.chuvashov.expressionparser;

public enum TokenType {
    
    TOKENADD(0),
    TOKENSUB(1),
    TOKENMUL(2),
    TOKENDIV(3),
    OPENBB(4),
    CLOSEDBB(5),
    CONST(6),
    TOKENEOF(7);

    private final int value;

    TokenType(int i) {
        this.value = i;
    }
    public int getValue() {
        return value;
    }
}
