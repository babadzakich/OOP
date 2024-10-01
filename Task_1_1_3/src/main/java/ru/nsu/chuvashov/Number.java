package ru.nsu.chuvashov;

public class Number extends Expression {

    double value;
    Number (int number) {
        this.value = number;
    }

    @Override
    double eval(String variables) {
        return value;
    }

    @Override
    void print() {
        System.out.print(value);
    }

    @Override
    Expression derivative(String variable) {
        return new Number(0) ;
    }
}
