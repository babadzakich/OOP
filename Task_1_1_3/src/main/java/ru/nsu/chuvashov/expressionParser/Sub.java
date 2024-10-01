package ru.nsu.chuvashov.expressionParser;

public class Sub extends Expression{
    private final Expression left;
    private final Expression right;

    public Sub(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    @Override
    public double eval(String variables) {
        return left.eval(variables) - right.eval(variables);
    }

    @Override
    public void print() {
        System.out.print('(');
        left.print();
        System.out.print(" - ");
        right.print();
        System.out.print(')');
    }

    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable),
                right.derivative(variable));
    }
}
