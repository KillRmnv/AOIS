package by.romanoff.aois;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class LogicFunctionTest {
    private Matrix matrix;
    private LogicFunction logicFunction;
    private final int TEST_SIZE = 16;

    @BeforeEach
    void setUp() {
        int[][] testData = new int[TEST_SIZE][TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            for (int j = 0; j < TEST_SIZE; j++) {
                testData[i][j] = (i + j) % 2;
            }
        }
        matrix = new Matrix(testData);
    }

    @Test
    void testConstructor() {
        String testExpression = "A&B";
        logicFunction = new LogicFunction(testExpression, TEST_SIZE);

        assertNotNull(logicFunction);
        assertEquals(testExpression, logicFunction.getExpression());
        assertNotNull(logicFunction.getTruthTable());
        assertEquals(TEST_SIZE, logicFunction.getTruthTable().getCombinations().size());
    }

    @Test
    void testAddWord() {
        logicFunction = new LogicFunction("A|B", TEST_SIZE);
        int[] testWord = matrix.getWord(0);

        logicFunction.addWord(testWord);

        List<List<Integer>> combinations = logicFunction.getTruthTable().getCombinations();
        for (int i = 0; i < testWord.length; i++) {
            assertEquals(testWord[i], combinations.get(i).get(0));
        }
    }

    @Test
    void testCreateNewWordF6() {
        // Test XOR function (!A&B)|(A&!B)
        logicFunction = new LogicFunction("(!A&B)|(A&!B)", 2);

        // Add test words
        logicFunction.addWord(new int[]{0, 1}); // A
        logicFunction.addWord(new int[]{1, 0}); // B

        int[] result = logicFunction.createNewWord(2);
        assertArrayEquals(new int[]{1, 1}, result); // XOR result
    }

    @Test
    void testCreateNewWordF9() {
        // Test XNOR function (A&B)|(!A&!B)
        logicFunction = new LogicFunction("(A&B)|(!A&!B)", 2);

        logicFunction.addWord(new int[]{0, 1}); // A
        logicFunction.addWord(new int[]{1, 0}); // B

        int[] result = logicFunction.createNewWord(2);
        assertArrayEquals(new int[]{0, 0}, result); // XNOR result
    }

    @Test
    void testCreateNewWordF4() {
        // Test !A&B function
        logicFunction = new LogicFunction("(!A&B)", 2);

        logicFunction.addWord(new int[]{0, 1}); // A
        logicFunction.addWord(new int[]{1, 0}); // B

        int[] result = logicFunction.createNewWord(2);
        assertArrayEquals(new int[]{1, 0}, result); // !A&B result
    }

    @Test
    void testCreateNewWordF11() {
        logicFunction = new LogicFunction("(A|!B)", 2);

        logicFunction.addWord(new int[]{0, 1}); // A
        logicFunction.addWord(new int[]{1, 0}); // B

        int[] result = logicFunction.createNewWord(2);
        assertArrayEquals(new int[]{0, 1}, result); // A|!B result
    }

    @Test
    void testExpressionSetter() {
        logicFunction = new LogicFunction("A&B", TEST_SIZE);
        String newExpression = "A|B";

        logicFunction.setExpression(newExpression);
        assertEquals(newExpression, logicFunction.getExpression());
    }

    @Test
    void testApplyLogicFunctionsExample() {
        int[] word1 = {0, 1, 0, 1};
        int[] word2 = {1, 0, 1, 0};

        LogicFunction f6 = new LogicFunction("(!A&B)|(A&!B)", 4);
        LogicFunction f9 = new LogicFunction("(A&B)|(!A&!B)", 4);
        LogicFunction f4 = new LogicFunction("(!A&B)", 4);
        LogicFunction f11 = new LogicFunction("(A|!B)", 4);

        // Add words
        f6.addWord(word1);
        f6.addWord(word2);
        f9.addWord(word1);
        f9.addWord(word2);
        f4.addWord(word1);
        f4.addWord(word2);
        f11.addWord(word1);
        f11.addWord(word2);

        // Test results
        assertArrayEquals(new int[]{1, 1, 1, 1}, f6.createNewWord(4)); // XOR
        assertArrayEquals(new int[]{0, 0, 0, 0}, f9.createNewWord(4)); // XNOR
        assertArrayEquals(new int[]{1, 0, 1, 0}, f4.createNewWord(4)); // !A&B
        assertArrayEquals(new int[]{0, 1, 0, 1}, f11.createNewWord(4)); // A|!B
    }
}