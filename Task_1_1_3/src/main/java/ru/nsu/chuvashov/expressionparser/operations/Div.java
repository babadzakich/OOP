package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;

/**
 * Division class.
 */
public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructor.
     *
     * @param a - left part
     * @param b - right part.
     */
    public Div(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    /**
     * Division logic.
     *
     * @param variables - our variables with value.
     * @return result of division.
     * @throws Exception when we try to divide by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        if (right.eval(variables) == 0) {
            throw new ArithmeticException("Can`t divide by zero!!");
        }
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * We print our division.
     */
    @Override
     public void print() {
        System.out.print(this);
    }

    /**
     * New derivative instance.
     *
     * @param variable by which we take derivative.
     * @return new derivative.
     */
    @Override
    public Expression derivative(String variable) throws Exception {
        return new Div(
                new Sub(new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }

    /**
     * Simplification for division.
     *
     * @return simlified expression.
     */
    @Override
    public Expression simplification() {
        double leftDouble;
        double rightDouble;
        try {
            leftDouble = left.eval("");
            if (leftDouble == 0) {
                return new Number(0);
            }
        } catch (Exception e) {
            System.out.println("Can`t simplify expression!");
            return this;
        }

        try {
            rightDouble = right.eval("");
        } catch (Exception e) {
            System.out.println("Can`t simplify expression!");
            return this;
        }
        if (rightDouble == 0) {
            throw new ArithmeticException("Can`t divide by zero!!");
        }
        return new Number(leftDouble / rightDouble);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Div a) {
            return this.left.equals(a.left) && this.right.equals(a.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return left.hashCode() + right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " / " + right.toString() + ")";
    }
}
