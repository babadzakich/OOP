package ru.nsu.chuvashov.substring;

import java.util.List;

/**
 * KnuttMorrisPratt algorithm implementation.
 */
public class KnuthMorrisPratt {
    /**
     * Adds pattern to text and runs prefixFunction.
     *
     * @param text - where we search.
     * @param pattern - what we search.
     * @param result - List where we store locations of patterns.
     * @param offset - amount of chars already processed by algorithm.
     */
    public static void getPattern(String text, String pattern, List<Integer> result, int offset) {
        int[] prefix = prefixFunction(pattern + '@' + text);
        for (int i = 0; i < text.length(); i++) {
            if (prefix[i + 1 + pattern.length()] == pattern.length()) {
                result.add(i - pattern.length() + 1 + offset);
            }
        }
    }

    /**
     * We smartly search for prefixes.
     *
     * @param text where we make prefixFunction.
     * @return array of prefixes values.
     */
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
