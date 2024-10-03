package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;

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
        System.out.print('(');
        left.print();
        System.out.print(" - ");
        right.print();
        System.out.print(')');
    }

    /**
     * Derivative calculation method.
     *
     * @param variable - derivative var.
     * @return derivative of substraction.
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable),
                right.derivative(variable));
    }
}
