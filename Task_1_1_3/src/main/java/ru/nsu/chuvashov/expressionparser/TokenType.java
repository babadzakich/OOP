package ru.nsu.chuvashov.expressionparser;

public enum TokenType {
    
    TOKENADD(0),
    TOKENSUB(1),
    TOKENMUL(2),
    TOKENDIV(3),
    OPENBB(4),
    CLOSEDBB(5),
    NUMBER(6),
    VARIABLE(7),
    TOKENEOF(8);

    private final int value;

    TokenType(int i) {
        this.value = i;
    }
    public int getValue() {
        return value;
    }
}
