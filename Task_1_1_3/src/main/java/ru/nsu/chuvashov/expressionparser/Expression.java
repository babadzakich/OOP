package ru.nsu.chuvashov.expressionparser;

import java.util.Map;

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
    abstract public double eval(String variables) throws Exception;

    /**
     * We print our statement.
     */
    abstract public void print();

    /**
     * We take derivative.
     *
     * @param variable by which we take derivative.
     * @return new derivative expression.
     */
    abstract public Expression derivative(String variable);

    /**
     * Method for testing purposes.
     *
     * @return expression with brackets.
     */
    abstract public String toString();
}
