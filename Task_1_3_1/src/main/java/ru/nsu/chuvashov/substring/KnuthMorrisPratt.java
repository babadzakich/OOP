package ru.nsu.chuvashov.substring;

import java.util.List;

public class KnuthMorrisPratt {
    public static void getPattern(String text, String pattern, List<Integer> result, int offset) {
        int[] prefix = prefixFunction(pattern + '@' + text);
        for (int i = 0; i < text.length(); i++) {
            if (prefix[i + 1 + pattern.length()] == pattern.length()) {
                result.add(i - pattern.length() + 1 + offset);
            }
        }
    }

    private static int[] prefixFunction(String text) {
        int[] prefix = new int[text.length() + 1];
        prefix[0] = 0;
        for (int i = 1; i < text.length(); i++) {
            int k = prefix[i - 1];
            while (k > 0 && text.charAt(i) != text.charAt(k)) {
                k = prefix[k - 1];
            }
            if (text.charAt(i) == text.charAt(k)) {
                k++;
            }
            prefix[i] = k;
        }
        return prefix;
    }
}
