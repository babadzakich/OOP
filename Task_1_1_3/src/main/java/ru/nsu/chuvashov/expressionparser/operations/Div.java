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
     * @throws ArithmeticException when we try to divide by zero.
     * @throws IllegalArgumentException if we try to evaluate string in wrong format.
     */
    @Override
    public double eval(String variables) throws ArithmeticException, IllegalArgumentException {
        if (right.eval(variables) == 0) {
            throw new ArithmeticException("Can`t divide by zero!!");
        }
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * New derivative instance.
     *
     * @param variable by which we take derivative.
     * @return new derivative.
     * @throws IllegalArgumentException when we take derivative by empty string.
     */
    @Override
    public Expression derivative(String variable) throws IllegalArgumentException {
        return new Div(
                new Sub(new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }

    /**
     * Simplification for division. If divisor == 0,
     * we return 0, if divider == 0, throws ArithmeticException.-m,.
     *
     * @return simlified expression.
     */
    @Override
    public Expression simplification() {
        Expression leftSimplified = left.simplification();
        Expression rightSimplified = right.simplification();

        if (leftSimplified instanceof Number l && rightSimplified instanceof Number r) {
            if (l.equals(new Number(0))) {
                return new Number(0);
            }
            if (r.equals(new Number(0))) {
                throw new ArithmeticException("Can`t divide by zero!!");
            }
            if (r.equals(new Number(1))) {
                return l;
            }
            return new Number(l.eval("") / r.eval(""));
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
        if (o instanceof Div a) {
            return this.left.equals(a.left) && this.right.equals(a.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (31 * 5 + this.left.hashCode()) * 31 + this.right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " / " + right.toString() + ")";
    }
}
