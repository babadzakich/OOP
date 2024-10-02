package ru.nsu.chuvashov.expressionparser;

public class Parser extends Expression {
    private TokenType type;
    private double value;


    @Override
    public double eval(String variables) throws Exception {
        return 0;
    }

    @Override
    public void print() {

    }

    @Override
    public Expression derivative(String variable) {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
