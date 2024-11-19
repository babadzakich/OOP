package ru.nsu.chuvashov.substring;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main class.
 */
public class Main {
    /**
     * Main method.
     *
     * @param args - command line.
     */
    public static void main(String[] args) {
        String file;
        String pattern;
        Scanner scanner = new Scanner(System.in);
        file = scanner.nextLine();
        pattern = scanner.nextLine();
        scanner.close();
        List<Integer> res;
        try {
            res = Finder.find(file, pattern);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(res);
    }
}
