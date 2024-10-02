package ru.nsu.chuvashov.expressionparser;

/**
 * Addition class.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructor.
     *
     * @param a - left part.
     * @param b - right part.
     */
    public Add(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    /**
     * Summary logic.
     *
     * @param variables - our variables with value.
     * @return summary.
     * @throws Exception when we divide by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        return left.eval(variables) + right.eval(variables);
    }

    /**
     * Printing statement.
     */
    @Override
    public void print() {
        System.out.print('(');
        left.print();
        System.out.print(" + ");
        right.print();
        System.out.print(')');
    }

    /**
     * We take derivative.
     *
     * @param variable by which we take derivative.
     * @return new derivative.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    @Override
    public String toString() {
        return "("
                + left.toString()
                + " + "
                + right.toString()
                + ")";
    }
}
