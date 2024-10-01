package ru.nsu.chuvashov.expressionParser;

public class Variable extends Expression {
    private final String var;

    public Variable(String input) {
        this.var = input;
    }

    @Override
    public double eval(String variables) {
        String[] vars = variables.split("; ");
        for (String s : vars) {
            String[] variab = s.split(" = ");
            if (variab[0].equals(var)) {
                return Double.parseDouble(variab[1]);
            }
        }
        return 1;
    }

    @Override
    public void print() {
        System.out.print(var);
    }

    @Override
    public Expression derivative(String variable) {
        if (var.equals(variable)) {
            return new Number(1);
        }
        return this;
    }
}
