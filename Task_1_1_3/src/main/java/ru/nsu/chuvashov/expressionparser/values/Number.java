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
    public Expression derivative(String variable) {
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

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Number a) {
            return value == a.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(value).hashCode();
    }

    /**
     * Getter method.
     *
     * @return value.
     */
    public double getValue() {
        return value;
    }
}
