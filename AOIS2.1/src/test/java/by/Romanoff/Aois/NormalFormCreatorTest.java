package by.Romanoff.Aois;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalFormCreatorTest {
    private NormalFormCreator normalFormCreator;
    private TruthTable truthTable;
    private Map<String, Character> statements;

    @BeforeEach
    void setUp() {
        normalFormCreator = new NormalFormCreator();
        LogicExpressionParser parser = new LogicExpressionParser();
        String expression = "(a&b)|!c";
        truthTable = new TruthTable();
        truthTable.createTruthTable(parser.parseOnBasicExpressions(expression,truthTable.getStatements()));
        statements = truthTable.getStatements();
    }

    @Test
    void testSDNF() {
        List<List<Integer>> combinations = truthTable.getCombinations();
        HashMap<String, Object> sdnfResult = normalFormCreator.sdnf(combinations, statements);

        ArrayList<Integer> numericalForm = (ArrayList<Integer>) sdnfResult.get("NumericalForm");
        assertEquals(5, numericalForm.size(), "Ожидаем 4 строки в числовой форме СДНФ");
        assertTrue(numericalForm.contains(0), "Числовая форма должна содержать 0");
        assertTrue(numericalForm.contains(4), "Числовая форма должна содержать 4");
        assertTrue(numericalForm.contains(2), "Числовая форма должна содержать 2");
        assertTrue(numericalForm.contains(6), "Числовая форма должна содержать 6");
        assertTrue(numericalForm.contains(7), "Числовая форма должна содержать 7");
        String sdnfString = (String) sdnfResult.get("result");
        assertTrue(sdnfString.contains("(!a&!b&!c)|"), "СДНФ должна содержать (!c&!a&!b)");
        assertTrue(sdnfString.contains("(!a&b&!c)|"), "СДНФ должна содержать (a&b&c)");
        assertTrue(sdnfString.contains("(a&!b&!c)|"), "СДНФ должна содержать (!a&b&!c)");
        assertTrue(sdnfString.contains("(a&b&c)"), "СДНФ должна содержать (a&!b&!c)");
    }

    @Test
    void testSKNF() {
        List<List<Integer>> combinations = truthTable.getCombinations();
        HashMap<String, Object> sknfResult = normalFormCreator.sknf(combinations, statements);

        ArrayList<Integer> numericalForm = (ArrayList<Integer>) sknfResult.get("NumericalForm");
        assertEquals(3, numericalForm.size(), "Ожидаем 4 строки в числовой форме СКНФ");
        assertTrue(numericalForm.contains(1), "Числовая форма должна содержать 4");
        assertTrue(numericalForm.contains(3), "Числовая форма должна содержать 5");
        assertTrue(numericalForm.contains(5), "Числовая форма должна содержать 6");

        String sknfString = (String) sknfResult.get("result");

        assertTrue(sknfString.contains("(a|b|!c)&"), "СКНФ должна содержать ");
        assertTrue(sknfString.contains("(a|!b|!c)&"), "СКНФ должна содержать ");
        assertTrue(sknfString.contains("(!a|b|!c)"), "СКНФ должна содержать ");
    }

    @Test
    void testIndexForm() {
        ArrayList<Integer> indexForm = normalFormCreator.indexForm(truthTable);

        assertEquals(8, indexForm.size(), "Ожидаем 8 значений в индексной форме");
        assertArrayEquals(new Integer[]{1,0,1,0,1,0,1,1}, indexForm.toArray(), "Индексная форма не совпадает с ожидаемой");
    }
}