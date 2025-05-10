package by.romanoff.aois;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class MatrixTest {
    private Matrix matrix;
    private int[][] testData;

    @BeforeEach
    void setUp() {
        testData = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                testData[i][j] = (i + j) % 2;
            }
        }
        matrix = new Matrix(testData);
    }

    @Test
    void testSize() {
        assertEquals(16, matrix.size());
    }

    @Test
    void testConstructorWithParameter() {
        assertArrayEquals(testData, matrix.getMatrix());
    }

    @Test
    void testDefaultConstructor() {
        Matrix defaultMatrix = new Matrix();
        assertEquals(16, defaultMatrix.getMatrix().length);
        assertEquals(16, defaultMatrix.getMatrix()[0].length);
    }

    @Test
    void testGetWord() {
        int[] expectedWord = new int[16];
        for (int i = 1; i < 16; i++) {
            expectedWord[i] = (5 + i-1) % 2;
        }
        expectedWord[0]=(5+15)%2;
        int[] actualWord = matrix.getWord(5);
        assertArrayEquals(expectedWord, actualWord);
    }

    @Test
    void testGetColumn() {
        int[] expectedColumn = new int[16];
        for (int i = 0; i < 16; i++) {
            expectedColumn[i] = 1;
        }

        int[] actualColumn = matrix.getColumn(3);
        assertArrayEquals(expectedColumn, actualColumn);
    }

    @Test
    void testWriteWord() {
        int[] newWord = new int[16];
        Arrays.fill(newWord, 1);

        matrix.writeWord(7, newWord);
        int[] retrievedWord = matrix.getWord(7);
        assertArrayEquals(newWord, retrievedWord);
    }

    @Test
    void testWriteColumn() {
        int[] newColumn = new int[16];
        Arrays.fill(newColumn, 0);

        matrix.writeColumn(4, newColumn);
        int[] retrievedColumn = matrix.getColumn(4);
        assertArrayEquals(newColumn, retrievedColumn);
    }

    @Test
    void testFindWords() {
        matrix.getMatrix()[2][2] = 1;
        matrix.getMatrix()[2][3] = 0;
        matrix.getMatrix()[2][4] = 1;

        List<Integer> searchPattern = List.of(1, 0, 1);
        Map<int[], Integer> result = matrix.findWords(searchPattern);

        assertEquals(1, result.size());

    }

    @Test
    void testFindWordsNoMatch() {
        List<Integer> searchPattern = List.of(1, 1, 1);
        Map<int[], Integer> result = matrix.findWords(searchPattern);
        assertTrue(result.isEmpty());
    }

    @Test
    void testPrintMatrixFormatted() {
        assertDoesNotThrow(() -> matrix.printMatrixFormatted());
    }

    @Test
    void testArithmeticOperation() {
        int[] testWord = new int[16];
        testWord[0] = 1;
        testWord[1] = 0;
        testWord[2] = 1;
        testWord[3] = 1;
        testWord[4] = 0;
        testWord[5] = 1;
        testWord[6] = 0;
        testWord[7] = 0;
        testWord[8] = 1;
        testWord[9] = 1;
        testWord[10] = 0;
        testWord[11]=1;
        testWord[12] = 0;
        testWord[13] = 1;
        testWord[14] = 0;
        testWord[15] = 0;
        matrix.writeWord(0, testWord);

        matrix.arithmeticOperation(5);

        int[] resultWord = matrix.getWord(0);
        int[] expectedBits = {1,0,0,0,0};

        for (int i = 0; i < expectedBits.length; i++) {
            assertEquals(expectedBits[i], resultWord[11 + i]);
        }
    }
}