package ru.nsu.chuvashov.expressionparser.operations;

import ru.nsu.chuvashov.expressionparser.values.Expression;
import ru.nsu.chuvashov.expressionparser.values.Number;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream saveOut = System.out;
        System.setOut(new PrintStream(out));
        left.print();
        String s1 = out.toString();
        out.reset();
        right.print();
        String s2 = out.toString();
        if (s1.equals(s2)) {
            System.setOut(new PrintStream(saveOut));
            return new Number(0);
        }
        System.setOut(new PrintStream(saveOut));
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


}
