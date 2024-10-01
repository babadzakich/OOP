package ru.nsu.chuvashov;

public abstract class Expression {
    abstract double eval(String variables);
    abstract void print();
    abstract Expression derivative(String variable);
}
