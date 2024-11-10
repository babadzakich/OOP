package ru.nsu.chuvashov.substring;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class KnuthMorrisPrattTest {

  @Test
  void getPattern() {
    String text = "абракадабра";
    String pattern = "бра";
    List<Integer> result = new ArrayList<>();

    KnuthMorrisPratt.getPattern(text, pattern, result);
    System.out.println(result);
  }
}
