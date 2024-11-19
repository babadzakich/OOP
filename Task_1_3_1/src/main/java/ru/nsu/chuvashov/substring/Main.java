package ru.nsu.chuvashov.substring;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String file;
        String pattern;
        Scanner scanner = new Scanner(System.in);
        file = scanner.nextLine();
        pattern = scanner.nextLine();
        scanner.close();
        List<Integer> res = Finder.find(file, pattern);
        System.out.println(res);
    }
}
