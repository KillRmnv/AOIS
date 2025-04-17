package Aois.Romanoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String expression = "!((A->B)|(C~D))&E";
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
        SDNF = (HashMap<Integer, String>) result.get("HashTableOfSDNF");
        SDNF = calculationMethod.applyMethodSDNF((HashMap<Integer, String>) result.get("HashTableOfSDNF"), truthTable.getStatements().size());
        printResult(SDNF, 1);
        karnosMapMethod.createKarnos(expression, 1);
    }

    public static List<String> constituentsList(HashMap<Integer, String> SDNF) {
        List<String> glued = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(SDNF.keySet());
        keys.sort(Integer::compareTo);
        StringBuilder minimizedFormula = new StringBuilder();
        minimizedFormula.append("(");
        minimizedFormula.append(SDNF.get(keys.get(0)));
        if (keys.getFirst() / 10 != keys.get(1) / 10) {
            // minimizedFormula.append(")");
            minimizedFormula = new StringBuilder();
            minimizedFormula.append(SDNF.get(keys.getFirst()));
            glued.add(minimizedFormula.toString());
            minimizedFormula = new StringBuilder();
        } else {
            minimizedFormula.append("&");
        }
        for (int i = 1; i < keys.size() - 1; i++) {
            if (keys.get(i + 1) / 10 == keys.get(i) / 10 && !(keys.get(i - 1) / 10 == keys.get(i) / 10)) {
                minimizedFormula.append("(");
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append("&");
            } else if (keys.get(i + 1) / 10 == keys.get(i) / 10 && keys.get(i - 1) / 10 == keys.get(i) / 10) {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append("&");
            } else if (keys.get(i + 1) / 10 != keys.get(i) / 10 && keys.get(i - 1) / 10 == keys.get(i) / 10) {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(")");
                glued.add(minimizedFormula.toString());
                minimizedFormula = new StringBuilder();
            } else {
                // minimizedFormula.append("(");
                minimizedFormula.append(SDNF.get(keys.get(i)));
                //  minimizedFormula.append(")");
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
        var glued = constituentsList(SDNF);
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
//