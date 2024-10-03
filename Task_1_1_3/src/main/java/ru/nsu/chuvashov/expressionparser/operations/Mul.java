package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;

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
        System.out.print('(');
        left.print();
        System.out.print(" * ");
        right.print();
        System.out.print(')');
    }

    /**
     * We take derivative.
     *
     * @param variable - derivative var.
     * @return taking derivative result.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
    }
}
