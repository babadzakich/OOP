package ru.nsu.chuvashov.expressionParser;

public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    public Div(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    @Override
    public double eval(String variables) {
        if (right.eval(variables) == 0) {
            throw new ArithmeticException("Can`t divide by zero!!");
        }
        return left.eval(variables) / right.eval(variables);
    }

    @Override
     public void print() {
        System.out.print('(');
        left.print();
        System.out.print(" / ");
        right.print();
        System.out.print(')');
    }

    @Override
    public Expression derivative(String variable) {
        return new Div(
                new Sub(new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }
}
