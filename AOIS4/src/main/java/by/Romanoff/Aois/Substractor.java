package by.Romanoff.Aois;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Subtractor {
    private TruthTable truthTable;
    private NormalFormCreator normalFormCreator;
    private CalculationMethod calculationMethod;

    public Subtractor() {
        truthTable = new TruthTable();
        normalFormCreator = new NormalFormCreator();
        Map<String, Character> statements = new LinkedHashMap<>();
        statements.put("A", 'A');
        statements.put("B", 'B');
        statements.put("Bin", 'C');
        //statements.put("D", 'C');
       // statements.put("Bout", 'C');
        truthTable.setStatements(statements);
        calculationMethod = new CalculationMethod();
        truthTable.setCombinations(truthTable.allPossibleCombinations(3));
    }

    public void createTable() {
        for (int i = 0; i < truthTable.getCombinations().size(); i++) {
            truthTable.getCombinations().get(i).add(substract(truthTable.getCombinations().get(i)));
        }
        var result = normalFormCreator.sdnf(truthTable.getCombinations(), truthTable.getStatements());

        var sdnf1 = calculationMethod.applyMethodSdnf((Map<java.lang.Integer, java.lang.String>) result.get("HashTableOfSDNF"),
                truthTable.getStatements().size());
        truthTable.getStatements().put("D", 'C');
        truthTable.print();
        truthTable.getStatements().remove(("D"));
        Output.printResult((Map<java.lang.Integer, java.lang.String>) result.get("HashTableOfSDNF"), 1);
        System.out.println("Минимизированная:");
        Output.printResult(sdnf1, 1);
        for (int i = 0; i < truthTable.getCombinations().size(); i++) {
            if (borrow(truthTable.getCombinations().get(i), i < truthTable.getCombinations().size() / 2 ? true : false)) {
                truthTable.getCombinations().get(i).add(1);
            } else
                truthTable.getCombinations().get(i).add(0);
        }

        result = normalFormCreator.sdnf(truthTable.getCombinations(), truthTable.getStatements());
        var sdnf2 = calculationMethod.applyMethodSdnf((Map<java.lang.Integer, java.lang.String>) result.get("HashTableOfSDNF"),
                truthTable.getStatements().size());
        truthTable.getStatements().put("D", 'C');
        truthTable.getStatements().put("Bout", 'C');
        truthTable.print();
        Output.printResult((Map<java.lang.Integer, java.lang.String>) result.get("HashTableOfSDNF"), 1);
        System.out.println("Минимизированная:");
        Output.printResult(sdnf2, 1);
    }

    private int substract(List<Integer> truthTableLine) {
        int result = truthTableLine.getFirst();
        for (int i = 1; i < truthTableLine.size(); i++) {
            result = result - truthTableLine.get(i);
        }
        return Math.abs(result) % 2;
    }

    private boolean borrow(List<Integer> truthTableLine, boolean type) {
        int result = truthTableLine.get(1) + truthTableLine.get(2);
        return type ? 0 < result : 1 < result;
    }
}