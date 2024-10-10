package ru.nsu.chuvashov.expressionparser.values;

/**
 * Abstract class that has only signatures,
 * for all classes that extend this class.
 */
public abstract class Expression {
    /**
     * Evaluation method.
     *
     * @param variables - our variables with value.
     * @return result of evaluation.
     * @throws ArithmeticException division by zero.
     * @throws IllegalArgumentException when our variable evaluation is in wrong format.
     */
    public abstract double eval(String variables) throws IllegalArgumentException, ArithmeticException;

    /**
     * We print our statement.
     */
    public void print() {
        System.out.print(this);
    }

    /**
     * We take derivative.
     *
     * @param variable by which we take derivative.
     * @return new derivative expression.
     * @throws IllegalArgumentException when we take derivative by empty string.
     */
    public abstract Expression derivative(String variable) throws IllegalArgumentException;

    /**
     * Simplification method.
     *
     * @return simplified expression.
     */
    public abstract Expression simplification();
}
