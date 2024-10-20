package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;

/**
 * Multiplication class.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructor.
     *
     * @param a left part.
     * @param b right part.
     */
    public Mul(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    /**
     * We multiply two parts.
     *
     * @param variables - substitution var.
     * @return multiplication of two parts.
     * @throws ArithmeticException of dividing by zero in some expression below.
     * @throws IllegalStateException if our variables are in wrong format.
     */
    @Override
    public double eval(String variables) throws ArithmeticException, IllegalArgumentException {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * We take derivative.
     *
     * @param variable - derivative var.
     * @return taking derivative result.
     */
    @Override
    public Expression derivative(String variable) throws IllegalArgumentException {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
    }

    /**
     * Simplification function. If we see multiplication by 0, we return 0,
     * if we multiply by 1, we return other multiplier.
     *
     * @return simplified function if we can simplify.
     */
    @Override
    public Expression simplification() {
        Expression leftSimplified = left.simplification();
        Expression rightSimplified = right.simplification();

        if (leftSimplified instanceof Number l && rightSimplified instanceof Number r) {
            return new Number(l.getValue() * r.getValue());
        } else if ((leftSimplified instanceof Number l && l.equals(new Number(0)))
                || (rightSimplified instanceof Number r && r.equals(new Number(0)))) {
            return new Number(0);
        } else if (leftSimplified instanceof Number l && l.equals(new Number(1))) {
            return rightSimplified;
        } else if (rightSimplified instanceof Number r && r.equals(new Number(1))) {
            return leftSimplified;
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
        if (o instanceof Mul a) {
            return this.left.equals(a.left) && this.right.equals(a.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (31 * 9 + this.left.hashCode()) * 31 + this.right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + " * " + this.right.toString() + ")";
    }
}
