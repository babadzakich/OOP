package ru.nsu.chuvashov;

public class Variable extends Expression {
    String var;

    Variable(String input) {
        this.var = input;
    }

    @Override
    double eval(String variables) {
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
    void print() {
        System.out.print(var);
    }

    @Override
    Expression derivative(String variable) {
        if (var.equals(variable)) {
            return new Number(1);
        }
        return this;
    }
}
