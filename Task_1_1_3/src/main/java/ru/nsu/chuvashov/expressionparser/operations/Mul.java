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
     * Simplification function.
     *
     * @return simplified function if we can simplify.
     */
    @Override
    public Expression simplification() {
        double leftEval;
        double rightEval;
        try {
            leftEval = left.eval("");
            rightEval = right.eval("");
        } catch (IllegalArgumentException | ArithmeticException e ) {
            System.out.println("Can't simplify");
            return this;
        }

        if (rightEval == 0 || leftEval == 0) {
            return new Number(0);
        }

        if (rightEval == 1) {
            return left;
        }

        if (leftEval == 1) {
            return right;
        }

        return new Number(leftEval * rightEval);
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
        return (31 * 7 + this.left.hashCode()) * 31 + this.right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + " * " + this.right.toString() + ")";
    }
}
