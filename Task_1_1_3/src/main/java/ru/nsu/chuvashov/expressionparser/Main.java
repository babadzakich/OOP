package ru.nsu.chuvashov.expressionparser;

import java.util.Scanner;

/**
 * Our main class.
 */
public class Main {
    /**
     * Our main method.
     *
     * @param args - empty string
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Parser parserAutomaton = Parser.getParser();
        Expression parsed = parserAutomaton.parseExpression(input);
        parsed.print();
        System.out.println(parsed.eval("X = 2"));

//        Expression e = new Add(new Add(new Number(3), new Variable("y")), new Mul(new Number(2),
//                new Variable("x")));
//        e.print();
//        double result = e.eval("x = 10; y = 13");
//        System.out.println(" " + result);
//
//        Expression e2 = new Add(new Number(3), new Mul(new Number(2),
//                new Variable("x")));
//        Expression der = e2.derivative("x");
//        der.print();
//        result = der.eval("x = 12");
//        System.out.println(result);
    }
}