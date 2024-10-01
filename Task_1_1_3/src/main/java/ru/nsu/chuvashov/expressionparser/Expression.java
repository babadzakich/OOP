package ru.nsu.chuvashov.expressionparser;

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
     * @throws Exception division by zero.
     */
    abstract double eval(String variables) throws Exception;

    /**
     * We print our statement.
     */
    abstract void print();

    /**
     * We take derivative.
     *
     * @param variable by which we take derivative.
     * @return new derivative expression.
     */
    abstract Expression derivative(String variable);
}
