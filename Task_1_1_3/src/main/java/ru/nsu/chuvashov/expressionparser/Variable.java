package ru.nsu.chuvashov.expressionparser;

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
     * @throws Exception of dividing by zero.
     */
    @Override
    public double eval(String variables) throws Exception {
        if (variables.length() < 5) {
            throw new Exception("Not enough arguments for substituting"
            + " variable");
        }
        String[] vars = variables.split("; ");
        for (String s : vars) {
            String[] variab = s.split(" = ");
            if (variab[0].equals(var)) {
                return Double.parseDouble(variab[1]);
            }
        }
        throw new Exception("Variable " + this.var + " wasn`t introduced");
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
    public Expression derivative(String variable) {
        if (var.equals(variable)) {
            return new Number(1);
        }
        return this;
    }
}
