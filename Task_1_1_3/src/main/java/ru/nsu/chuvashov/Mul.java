package ru.nsu.chuvashov;

public class Mul extends Expression{
    Expression left;
    Expression right;

    Mul(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    @Override
    double eval(String variables) {
        return left.eval(variables) * right.eval(variables);
    }

    @Override
    void print() {
        System.out.print('(');
        left.print();
        System.out.print(" * ");
        right.print();
        System.out.print(')');
    }

    @Override
    Expression derivative(String variable) {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
    }
}
