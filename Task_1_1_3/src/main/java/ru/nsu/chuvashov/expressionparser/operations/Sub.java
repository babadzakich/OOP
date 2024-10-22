package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;

/**
 * Substraction class.
 */
public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructor.
     *
     * @param a - left part.
     * @param b - right part.
     */
    public Sub(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    /**
     * Calculates substraction.
     *
     * @param variables - vars that we substitute.
     * @return result of substraction.
     * @throws ArithmeticException if we try to divide by zero.
     * @throws IllegalArgumentException if we try to eval var without var.
     */
    @Override
    public double eval(String variables) throws ArithmeticException, IllegalArgumentException {
        return left.eval(variables) - right.eval(variables);
    }

    /**
     * Derivative calculation method.
     *
     * @param variable - derivative var.
     * @return derivative of substraction.
     * @throws IllegalArgumentException when we take derivative by empty string.
     */
    @Override
    public Expression derivative(String variable) throws IllegalArgumentException {
        return new Sub(left.derivative(variable),
                right.derivative(variable));
    }

    /**
     * Simplify if left == right.
     *
     * @return 0 if we can simplify.
     */
    @Override
    public Expression simplification() {
        if (this.left.equals(this.right)) {
            return new Number(0);
        }
        Expression leftSimplified = left.simplification();
        Expression rightSimplified = right.simplification();
        if (leftSimplified instanceof Number l && rightSimplified instanceof Number r) {
            return new Number(l.getValue() - r.getValue());
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Sub a) {
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
        return "(" + this.left.toString() + " - " + this.right.toString() + ")";
    }
}
