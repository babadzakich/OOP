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
     * @throws ArithmeticException when we divide by zero.
     * @throws IllegalArgumentException when we try to evaluate program with wrong variable.
     */
    @Override
    public double eval(String variables) throws ArithmeticException, IllegalArgumentException {
        return left.eval(variables) + right.eval(variables);
    }

    /**
     * We take derivative.
     *
     * @param variable by which we take derivative.
     * @return new derivative.
     * @throws IllegalArgumentException when we take derivative by empty string.
     */
    @Override
    public Expression derivative(String variable) throws IllegalArgumentException {
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
            if (left instanceof Number l && right instanceof Number r) {
                return new Number(l.eval("") + r.eval(""));
            } else if (left instanceof Number l) {
                return new Number(l.eval("") + right.simplification().eval(""));
            }
            leftDouble = left.eval("");
            rightDouble = right.eval("");
        } catch (IllegalArgumentException e) {
            System.out.println("Can't simplify expression");
            return this;
        }
        return new Number(leftDouble + rightDouble);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Add a) {
            return this.left.equals(a.left) && this.right.equals(a.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (31 * 7 + this.left.hashCode()) * 31 + this.right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " + " + right.toString() + ")";
    }
}
