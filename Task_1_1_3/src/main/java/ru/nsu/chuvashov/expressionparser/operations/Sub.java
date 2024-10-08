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
     * @throws Exception if we try to divide by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        return left.eval(variables) - right.eval(variables);
    }

    /**
     * Prints substraction.
     */
    @Override
    public void print() {
        System.out.print(this);
    }

    /**
     * Derivative calculation method.
     *
     * @param variable - derivative var.
     * @return derivative of substraction.
     */
    @Override
    public Expression derivative(String variable) throws Exception {
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

        double leftDouble;
        double rightDouble;
        try {
            leftDouble = left.eval("");
            rightDouble = right.eval("");
        } catch (Exception e) {
            System.out.println("Can`t simplify");
            return this;
        }
        return new Number(leftDouble - rightDouble);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Sub a) {
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
