package Aois.Romanoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String expression = "(A&B)->(C|D)~(E&F)|G";
        LogicExpressionParser parser = new LogicExpressionParser();
        TruthTable truthTable = new TruthTable();
        truthTable.createTruthTable(parser.parseOnBasicExpressions(expression, truthTable.getStatements()));
        truthTable.print();
        NormalFormCreator normalFormCreator = new NormalFormCreator();
        HashMap<String, Object> result;
        result = normalFormCreator.sdnf(truthTable.getCombinations(), truthTable.getStatements());
        System.out.println("СДНФ" + result.get("result"));
        System.out.println("Числовая форма" + result.get("NumericalForm"));
        CalculationMethod calculationMethod = new CalculationMethod();
        TableMethod tableMethod = new TableMethod();
        KarnosMapMethod karnosMapMethod = new KarnosMapMethod();
        var SDNF = tableMethod.applyMethod((HashMap<Integer, String>) result.get("HashTableOfSDNF"), truthTable.getStatements().size(), 1);
        printResult(SDNF, 1);
        SDNF = calculationMethod.applyMethodSDNF((HashMap<Integer, String>) result.get("HashTableOfSDNF"), truthTable.getStatements().size());
        printResult(SDNF, 1);
        karnosMapMethod.print(karnosMapMethod.createKarnos(expression, 1), 1);
        result = normalFormCreator.sknf(truthTable.getCombinations(), truthTable.getStatements());
        var SKNF = tableMethod.applyMethod((HashMap<Integer, String>) result.get("HashTableOfSKNF"), truthTable.getStatements().size(), 0);
        printResult(SKNF, 0);
        SKNF = calculationMethod.applyMethodSKNF((HashMap<Integer, String>) result.get("HashTableOfSKNF"), truthTable.getStatements().size());
        printResult(SKNF, 0);
        karnosMapMethod = new KarnosMapMethod();
        karnosMapMethod.print(karnosMapMethod.createKarnos(expression, 0), 0);
    }

    public static List<String> constituentsList(HashMap<Integer, String> SDNF, int type) {
        List<String> glued = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(SDNF.keySet());
        keys.sort(Integer::compareTo);
        StringBuilder minimizedFormula = new StringBuilder();
        String operation = new String();
        if (type == 1) {
            operation = "&";
        } else {
            operation = "|";
        }
        minimizedFormula.append("(");
        minimizedFormula.append(SDNF.get(keys.get(0)));
        if (keys.getFirst() / 10 != keys.get(1) / 10) {
            minimizedFormula = new StringBuilder();
            minimizedFormula.append(SDNF.get(keys.getFirst()));
            glued.add(minimizedFormula.toString());
            minimizedFormula = new StringBuilder();
        } else {
            minimizedFormula.append(operation);
        }
        for (int i = 1; i < keys.size() - 1; i++) {
            if (keys.get(i + 1) / 10 == keys.get(i) / 10 && !(keys.get(i - 1) / 10 == keys.get(i) / 10)) {
                minimizedFormula.append("(");
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(operation);
            } else if (keys.get(i + 1) / 10 == keys.get(i) / 10 && keys.get(i - 1) / 10 == keys.get(i) / 10) {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(operation);
            } else if (keys.get(i + 1) / 10 != keys.get(i) / 10 && keys.get(i - 1) / 10 == keys.get(i) / 10) {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(")");
                glued.add(minimizedFormula.toString());
                minimizedFormula = new StringBuilder();
            } else {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                glued.add(minimizedFormula.toString());
                minimizedFormula = new StringBuilder();
            }
        }
        if (keys.getLast() / 10 != keys.get(keys.size() - 2) / 10) {
            minimizedFormula.append("(");
        }
        minimizedFormula.append(SDNF.get(keys.getLast()));

        minimizedFormula.append(")");
        glued.add(minimizedFormula.toString());
        return glued;
    }

    static void printResult(HashMap<Integer, String> SDNF, int type) {
        var glued = constituentsList(SDNF, type);
        print(glued, type);
    }

    static void print(List<String> glued, int type) {
        for (var k : glued) {
            System.out.print(k);
            if (type == 1)
                System.out.print("|");
            else
                System.out.print("&");
        }
        System.out.println();
    }
}


//(A->(B|!C))~(D&(E->Q))->!T
//(!A|B)->(C&(D~E))|(Q->T)
//(!(A&B)|(C->D))~(E&Q)->!T
//!((A&B)|(C->D))~E
//(A&B)|(!C->D)|E
//!(A|B)~(C&D)->E
//(A~(B->C))|(D&E)
//((A&B)->(C|D))~!E