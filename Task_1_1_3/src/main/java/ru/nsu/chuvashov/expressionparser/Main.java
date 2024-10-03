package ru.nsu.chuvashov.expressionparser;

import java.util.Scanner;
import ru.nsu.chuvashov.expressionparser.backusnaurparser.Parser;
import ru.nsu.chuvashov.expressionparser.values.Expression;

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
        System.out.println(" " + parsed.eval("X = 2;Y = 3"));
    }
}