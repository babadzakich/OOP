package ru.nsu.chuvashov;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;



class MainTest {
    @Test
    void checkMain(){
        Main.main(new String[]{});
        assertTrue(true);
    }

    @Test
    void checkLargeArray(){
        int[] expected = new int[10000000];
        Random r = new Random();

        for (int i = 0; i < 10000000; i++) {
            expected[i] = r.nextInt();
        }

        int[] actual = Main.heapsort(expected);
        Arrays.sort(expected);

        assertArrayEquals(expected, actual);
    }

    @Test
    void checkSingleElemArray(){
        int[] expected = new int[]{1};

        int[] actual = Main.heapsort(new int[]{1});

        assertArrayEquals(expected, actual);
    }

    @Test
    void checkEmptyArray(){
        int[] expected = new int[]{};

        int[] actual = Main.heapsort(new int[]{});

        assertArrayEquals(expected, actual);
    }

    @Test
    void checkSortedArray(){
        int[] expected = new int[]{1,2,3,4,5,6};

        int[] actual = Main.heapsort(new int[]{1,2,3,4,5,6});

        assertArrayEquals(expected, actual);
    }

    @Test
    void checkReverseSortedArray(){
        int[] expected = new int[]{1,2,3,4,5,6};

        int[] actual = Main.heapsort(new int[]{6,5,4,3,2,1});

        assertArrayEquals(expected, actual);
    }
}