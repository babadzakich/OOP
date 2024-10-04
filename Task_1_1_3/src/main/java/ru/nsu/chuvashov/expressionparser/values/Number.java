package ru.nsu.chuvashov.expressionparser.values;

/**
 * Class of numbers.
 */
public class Number extends Expression {

    private final double value;

    /**
     * Constructor.
     *
     * @param number - number that we use.
     */
    public Number(double number) {
        this.value = number;
    }

    /**
     * Just return of our number.
     *
     * @param variables - substitution variable.
     * @return our number.
     */
    @Override
    public double eval(String variables) {
        return value;
    }

    /**
     * Printing value of number.
     */
    @Override
    public void print() {
        System.out.print(value);
    }

    /**
     * Taking derivative from constant is 0.
     *
     * @param variable derivative var.
     * @return zero(result of taking derivative)
     */
    @Override
    public Expression derivative(String variable) throws Exception {
        return new Number(0);
    }

    /**
     * Cant simplify number, so just return number.
     *
     * @return this.
     */
    @Override
    public Expression simplification() {
        return this;
    }
}
