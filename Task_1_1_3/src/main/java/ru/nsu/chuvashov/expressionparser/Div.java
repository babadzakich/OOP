package ru.nsu.chuvashov.expressionparser;

/**
 * Division class.
 */
public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructor.
     *
     * @param a - left part
     * @param b - right part.
     */
    public Div(Expression a, Expression b) {
        this.left = a;
        this.right = b;
    }

    /**
     * Division logic.
     *
     * @param variables - our variables with value.
     * @return result of division.
     * @throws Exception when we try to divide by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        if (right.eval(variables) == 0) {
            throw new ArithmeticException("Can`t divide by zero!!");
        }
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * We print our division.
     */
    @Override
     public void print() {
        System.out.print('(');
        left.print();
        System.out.print(" / ");
        right.print();
        System.out.print(')');
    }

    /**
     * New derivative instance.
     *
     * @param variable by which we take derivative.
     * @return new derivative.
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
                new Sub(new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }
}
