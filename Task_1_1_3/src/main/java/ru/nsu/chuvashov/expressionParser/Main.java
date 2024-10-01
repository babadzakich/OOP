package ru.nsu.chuvashov.expressionParser;

public class Main {
    public static void main(String[] args) {
        Expression e = new Add(new Add(new Number(3), new Variable("y")), new Mul(new Number(2),
                new Variable("x")));
        e.print();
        double result = e.eval("x = 10; y = 13");
        System.out.println(" " + result);

        Expression e2 = new Add(new Number(3), new Mul(new Number(2),
                new Variable("x")));
        Expression der = e2.derivative("x");
        der.print();
        result = der.eval("x = 12");
        System.out.println(result);
    }
}