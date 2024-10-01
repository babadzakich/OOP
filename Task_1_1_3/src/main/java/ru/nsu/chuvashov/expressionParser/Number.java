package ru.nsu.chuvashov.expressionParser;

public class Number extends Expression {

    private final double value;
    public Number (int number) {
        this.value = number;
    }

    @Override
    public double eval(String variables) {
        return value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0) ;
    }
}
