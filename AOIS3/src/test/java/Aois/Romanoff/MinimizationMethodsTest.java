package Aois.Romanoff;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinimizationMethodsTest {

    private HashMap<Integer, String> createTestData(String expression, int type, TruthTable truthTable) {
        LogicExpressionParser parser = new LogicExpressionParser();
        truthTable.createTruthTable(parser.parseOnBasicExpressions(expression, truthTable.getStatements()));
        NormalFormCreator normalFormCreator = new NormalFormCreator();
        HashMap<String, Object> result;

        if (type == 1) {
            result = normalFormCreator.sdnf(truthTable.getCombinations(), truthTable.getStatements());
        } else {
            result = normalFormCreator.sknf(truthTable.getCombinations(), truthTable.getStatements());
        }

        return (HashMap<Integer, String>) result.get(type == 1 ? "HashTableOfSDNF" : "HashTableOfSKNF");
    }

    @Test
    public void testCalculationMethodSDNF_2Variables() {
        TruthTable truthTable = new TruthTable();
        String expression = "A|B";
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        CalculationMethod method = new CalculationMethod();
        HashMap<Integer, String> result = method.applyMethodSDNF(sdnfTable, truthTable.getStatements().size());
        KarnosMapMethod karnosMapMethod = new KarnosMapMethod();
        karnosMapMethod.createKarnos(expression,1);
        List<String> constituents = Main.constituentsList(result, 1);
        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("([A-Z])|(\\(([A-Z]&?[!]?[A-Z]?&?)+\\))"));
        }
    }

    @Test
    public void testTableMethodSDNF_3Variables() {
        String expression = "(A&B)|C";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        TableMethod method = new TableMethod();
        HashMap<Integer, String> result = method.applyMethod(sdnfTable, 3, 1);

        List<String> constituents = Main.constituentsList(result, 1);
        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("([A-Z])|(\\(([A-Z]&?[!]?[A-Z]?&?)+\\))"));
        }
    }

    @Test
    public void testKarnosMapMethodSDNF_4Variables() {
        String expression = "((A&B)->C)|D";
        KarnosMapMethod method = new KarnosMapMethod();
        List<String> constituents = new ArrayList<>();
        var result = method.createKarnos(expression, 1);
        for(var row: result) {
            constituents.add(method.term(1, row));
        }

        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("(\\(?[!]?[A-Z]\\)?)|(\\(([!]?[A-Z]&?[!]?[A-Z]?&?)+\\))"));
        }
    }

    @Test
    public void testCalculationMethodSKNF_3Variables() {
        String expression = "(A|B)&C";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sknfTable = createTestData(expression, 0, truthTable);
        CalculationMethod method = new CalculationMethod();
        HashMap<Integer, String> result = method.applyMethodSKNF(sknfTable, 3);

        List<String> constituents = Main.constituentsList(result, 0);
        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("([A-Z])|(\\(([A-Z]\\|?[!]?[A-Z]?\\|?)+\\))"));
        }
    }

    @Test
    public void testTableMethodSKNF_5Variables() {
        String expression = "((A&B)->(C|D))~!E";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sknfTable = createTestData(expression, 0, truthTable);
        TableMethod method = new TableMethod();
        HashMap<Integer, String> result = method.applyMethod(sknfTable, 5, 0);
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        karnoMethod.createKarnos(expression, 1);
        List<String> constituents = Main.constituentsList(result, 0);
        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("([A-Z])|(\\(([!]?[A-Z]\\|?[!]?[A-Z]?\\|?)+\\))"));
        }
    }
    @Test
    public void testTableMethodSDNF_7Variables() {
        String expression = "(A&B)->(C|D)~(E&F)|G";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        TableMethod method = new TableMethod();
        CalculationMethod calculationMethod = new CalculationMethod();
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        karnoMethod.createKarnos(expression, 1);
        calculationMethod.applyMethodSDNF(sdnfTable,truthTable.getStatements().size());
        HashMap<Integer, String> result = method.applyMethod(sdnfTable, truthTable.getStatements().size(), 1);
        List<String> constituents = Main.constituentsList(result, 1);
        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("([!]?[A-Z])|(\\(([!]?[A-Z]&?[!]?[A-Z]?&?)+\\))"));
        }
    }
    @Test
    public void testKarnosMapMethodSKNF_4Variables() {
        String expression = "(A|B|C)&D";
        KarnosMapMethod method = new KarnosMapMethod();
        List<String> constituents = new ArrayList<>();
        var result = method.createKarnos(expression, 0);
        for(var row: result) {
            constituents.add(method.term(0, row));
        }
        assertFalse(constituents.isEmpty());
        for (String term : constituents) {
            assertTrue(term.matches("([A-Z])|(\\(([A-Z]\\|?[!]?[A-Z]?\\|?)+\\))"));
        }
    }

    @Test
    public void testAllMethodsConsistencySDNF_3Variables() {
        String expression = "A&(B|C)";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);

        CalculationMethod calcMethod = new CalculationMethod();
        TableMethod tableMethod = new TableMethod();
        KarnosMapMethod karnosMethod = new KarnosMapMethod();

        HashMap<Integer, String> calcResult = calcMethod.applyMethodSDNF(sdnfTable, 3);
        HashMap<Integer, String> tableResult = tableMethod.applyMethod(sdnfTable, 3, 1);
        var karnosResult = karnosMethod.createKarnos(expression, 1);
        List<String> constituents = new ArrayList<>();

        for(var row: karnosResult) {
            constituents.add(karnosMethod.term(1, row));
        }
        List<String> calcConstituents = Main.constituentsList(calcResult, 1);
        List<String> tableConstituents = Main.constituentsList(tableResult, 1);

        // Проверяем, что все методы дали одинаковое количество термов
        assertEquals(calcConstituents.size(), tableConstituents.size());
        assertEquals(tableConstituents.size(), constituents.size());
    }
    @Test
    public void testSimpleOrSDNF_2Variables() {
        String expression = "A|B";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        CalculationMethod method = new CalculationMethod();
        HashMap<Integer, String> result = method.applyMethodSDNF(sdnfTable, truthTable.getStatements().size());
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        var karnoResult = karnoMethod.createKarnos(expression, 0);
        List<String> constituents = Main.constituentsList(result, 1);
        assertEquals(2, constituents.size()); // A, B, (A&B)
        assertTrue(constituents.contains("A") || constituents.contains("(A)"));
        assertTrue(constituents.contains("B") || constituents.contains("(!B)"));
    }

    @Test
    public void testSimpleAndSKNF_2Variables() {
        String expression = "A&B";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sknfTable = createTestData(expression, 0, truthTable);
        TableMethod method = new TableMethod();
        HashMap<Integer, String> result = method.applyMethod(sknfTable, 2, 0);
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        var karnoResult = karnoMethod.createKarnos(expression, 0);
        List<String> constituents = Main.constituentsList(result, 0);
        assertEquals(2, constituents.size());
       // assertTrue(constituents.get(0).matches("\\((!?[A-Z]\\|!?[A-Z])\\)"));
    }


    @Test
    public void testImplicationSKNF_2Variables() {
        String expression = "A->B";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sknfTable = createTestData(expression, 0, truthTable);
        KarnosMapMethod method = new KarnosMapMethod();
        var karnoResult = method.createKarnos(expression, 0);
        List<String> constituents = new ArrayList<>();

        for(var row: karnoResult) {
            constituents.add(method.term(0, row));
        }

        assertFalse(constituents.isEmpty());
        assertTrue(constituents.stream().anyMatch(t -> t.matches("\\((!?[A-Z]\\|!?[A-Z])\\)")));
    }

    @Test
    public void testEquivalenceSDNF_2Variables() {
        String expression = "A~B";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        TableMethod method = new TableMethod();
        HashMap<Integer, String> result = method.applyMethod(sdnfTable, 2, 1);
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        var karnoResult = karnoMethod.createKarnos(expression, 0);
        List<String> constituents = Main.constituentsList(result, 1);
        assertEquals(2, constituents.size());
        assertTrue(constituents.contains("(A&B)") || constituents.contains("(!A&!B)"));
    }

    @Test
    public void testNotAndSDNF_2Variables() {
        String expression = "!(A&B)";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        CalculationMethod method = new CalculationMethod();
        HashMap<Integer, String> result = method.applyMethodSDNF(sdnfTable, truthTable.getStatements().size());
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        var karnoResult = karnoMethod.createKarnos(expression, 0);
        List<String> constituents = Main.constituentsList(result, 1);
        assertEquals(2, constituents.size());
        assertTrue(constituents.contains("!A") || constituents.contains("(!B)"));
    }

    @Test
    public void testNotOrSKNF_2Variables() {
        String expression = "!(A|B)";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sknfTable = createTestData(expression, 0, truthTable);
        KarnosMapMethod method = new KarnosMapMethod();
        var karnoResult = method.createKarnos(expression, 0);
        List<String> constituents = new ArrayList<>();

        for(var row: karnoResult) {
            constituents.add(method.term(0, row));
        }

        assertEquals(2, constituents.size());
        assertTrue(constituents.get(0).equals("(!A&!B)") ||
                constituents.get(0).equals("(!A)") ||
                constituents.get(0).equals("(!B)"));
    }

    @Test
    public void testCombinedOperators_2Variables() {
        String expression = "(A->B)&(!A|B)";
        TruthTable truthTable = new TruthTable();
        HashMap<Integer, String> sdnfTable = createTestData(expression, 1, truthTable);
        TableMethod method = new TableMethod();
        HashMap<Integer, String> result = method.applyMethod(sdnfTable, 2, 1);
        KarnosMapMethod karnoMethod = new KarnosMapMethod();
        var karnoResult = karnoMethod.createKarnos(expression, 0);
        List<String> constituents = Main.constituentsList(result, 1);
        assertFalse(constituents.isEmpty());
    }
}