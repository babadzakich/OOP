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
     * @throws Exception of dividing by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Printing multiplication.
     */
    @Override
    public void print() {
        System.out.print(this);
    }

    /**
     * We take derivative.
     *
     * @param variable - derivative var.
     * @return taking derivative result.
     */
    @Override
    public Expression derivative(String variable) throws Exception {
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
    public Expression simplification() throws Exception {
        double leftEval;
        double rightEval;
        try {
            leftEval = left.eval("");
            rightEval = right.eval("");
        } catch (Exception e) {
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

        return new Number(this.eval(""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Mul a) {
            return this.left.equals(a.left) && this.right.equals(a.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.left.hashCode() + this.right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + " + " + this.right.toString() + ")";
    }
}
