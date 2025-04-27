package by.Romanoff.Aois;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class GLSearchOperationsTest {
    private Matrix matrix;
    private GLSearchOperations glSearchOperations;

    @BeforeEach
    void setUp() {
        int[][] testData = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                testData[i][j] = (i + j) % 2;
            }
        }
        matrix = new Matrix(testData);
        glSearchOperations = new GLSearchOperations(matrix);
    }

    @Test
    void testConstructor() {
        assertNotNull(glSearchOperations);
        assertNotNull(glSearchOperations.getTruthTable());
        assertEquals("G|(!A&B&!L)", glSearchOperations.getG());
        assertEquals("L|(A&!B&!G)", glSearchOperations.getL());
    }

    @Test
    void testCompareEqualWords() {
        int[] word = matrix.getWord(0);
        int result = glSearchOperations.compare(word, word, 0);
        assertEquals(0, result);
    }

    @Test
    void testCompareGreaterWord() {
        int[] greaterWord = new int[16];
        Arrays.fill(greaterWord, 1);
        matrix.writeWord(1, greaterWord);

        int[] originalWord = matrix.getWord(0);
        int result = glSearchOperations.compare(originalWord, greaterWord, 0);
        assertEquals(-1, result);
    }

    @Test
    void testCompareLesserWord() {
        int[] lesserWord = new int[16];
        Arrays.fill(lesserWord, 0);
        matrix.writeWord(1, lesserWord);

        int[] originalWord = matrix.getWord(0);
        int result = glSearchOperations.compare(originalWord, lesserWord, 0);
        assertEquals(1, result);
    }

    @Test
    void testFindMaxWithSingleWord() {
        int result = glSearchOperations.findMax(0, 0);
        assertEquals(0, result);
    }

    @Test
    void testFindMaxWithMultipleWords() {
        int[] maxWord = new int[16];
        Arrays.fill(maxWord, 1);
        matrix.writeWord(2, maxWord);

        int result = glSearchOperations.findMax(0, 3);
        assertEquals(2, result);
    }

    @Test
    void testSort() {
        for (int i = 0; i < 16; i++) {
            int[] word = new int[16];
            Arrays.fill(word, i % 2);
            matrix.writeWord(i, word);
        }

        glSearchOperations.sort();

        for (int i = 0; i < 15; i++) {
            int[] current = matrix.getWord(i);
            int[] next = matrix.getWord(i + 1);
            assertTrue(countOnes(current) <= countOnes(next));
        }
    }

    private int countOnes(int[] word) {
        int count = 0;
        for (int bit : word) {
            count += bit;
        }
        return count;
    }

    @Test
    void testTruthTableInitialization() {
        TruthTable truthTable = glSearchOperations.getTruthTable();
        assertNotNull(truthTable.getCombinations());
        assertEquals(16, truthTable.getCombinations().size());
        assertEquals(2, truthTable.getCombinations().get(0).size());
    }
}