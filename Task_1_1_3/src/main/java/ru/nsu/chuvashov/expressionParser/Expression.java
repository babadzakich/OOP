package ru.nsu.chuvashov.expressionParser;

public abstract class Expression {
    abstract double eval(String variables);
    abstract void print();
    abstract Expression derivative(String variable);
}
