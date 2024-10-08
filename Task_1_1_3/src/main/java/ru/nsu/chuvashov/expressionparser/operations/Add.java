package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;

/**
 * Addition class.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructor.
     *
     * @param a - left part.
     * @param b - right part.
     */
    public Add(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    /**
     * Summary logic.
     *
     * @param variables - our variables with value.
     * @return summary.
     * @throws Exception when we divide by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        return left.eval(variables) + right.eval(variables);
    }

    /**
     * Printing statement.
     */
    @Override
    public void print() {
        System.out.println(this);
    }

    /**
     * We take derivative.
     *
     * @param variable by which we take derivative.
     * @return new derivative.
     */
    @Override
    public Expression derivative(String variable) throws Exception {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Simplifies summary if we can.
     *
     * @return simplified Expression.
     */
    @Override
    public Expression simplification() {
        final double leftDouble;
        final double rightDouble;

        try {
            leftDouble = left.eval("");
            rightDouble = right.eval("");
        } catch (Exception e) {
            System.out.println("Can't simplify expression");
            return this;
        }
        return new Number(leftDouble + rightDouble);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Add a) {
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
        return "(" + left.toString() + " + " + right.toString() + ")";
    }
}
