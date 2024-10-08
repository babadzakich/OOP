package ru.nsu.chuvashov.expressionparser.values;

/**
 * Variable class.
 */
public class Variable extends Expression {
    private final String var;

    /**
     * A constructor for class.
     *
     * @param input = variable name.
     */
    public Variable(String input) {
        this.var = input;
    }

    /**
     * Separates string by ;, and searches for
     * needed variable, if not found, throws exeption.
     *
     * @param variables - string with variables.
     * @return result of substitution.
     * @throws Exception if needed variable wasn`t presented.
     */
    @Override
    public double eval(String variables) throws Exception {
        if (variables.length() < 5) {
            throw new IllegalArgumentException("Not enough arguments for substituting"
            + " variable");
        }
        String[] vars = variables.split(";");
        for (String s : vars) {
            String[] variab = s.split("=");
            try {
                variab[0] = variab[0].trim();
                if (variab[0].equals(var)) {
                    double result;
                    result = Double.parseDouble(variab[1]);
                    return result;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Wrong number format");
            }
        }
        throw new NoSuchFieldException("Variable " + this.var + " wasn`t introduced");
    }

    /**
     * Prints variable.
     */
    @Override
    public void print() {
        System.out.print(var);
    }

    /**
     * Makes derivatization.
     *
     * @param variable - derivative var.
     * @return 1 if current var is derivat number.
     */
    @Override
    public Expression derivative(String variable) throws Exception {
        if (variable.isEmpty()) {
            throw new IllegalArgumentException("Empty variable");
        }
        if (var.equals(variable)) {
            return new Number(1);
        }
        return this;
    }

    /**
     * Cant simplify already simplified expression.
     *
     * @return this.
     */
    @Override
    public Expression simplification() {
        return this;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Variable a) {
            return var.equals(a.var);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }
}
