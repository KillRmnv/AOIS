package by.Romanoff.Aois;

import java.util.*;

public class DigitalVendingMachine {
    private TruthTable truthTable;
    private KarnosMapMethod karnosMapMethod;

    public DigitalVendingMachine() {
        truthTable = new TruthTable();
        karnosMapMethod = new KarnosMapMethod();
    }

    public void minimizeDigitalVendingMachine(int amnt) {
        Map<String, Character> Statements = new LinkedHashMap<>();
        for (int i = 0; i < amnt; i++) {
            Statements.put("Q" + i, (char) (945 + i));
        }
        truthTable.setStatements(Statements);
        truthTable.setCombinations(truthTable.allPossibleCombinations(amnt));
        List<List<Integer>> tTrigers = new ArrayList<>();
        for (int i = 0; i < amnt; i++) {
            tTrigers.add(new ArrayList<>());
        }
        for (var combinationFirst = 0; combinationFirst < truthTable.getCombinations().size() - 1; combinationFirst++) {
            for (int el = 0; el < truthTable.getCombinations().get(combinationFirst).size(); el++) {
                if (truthTable.getCombinations().get(combinationFirst).get(el).equals(truthTable.getCombinations().get(combinationFirst + 1).get(el))) {
                    tTrigers.get(el).add(0);
                } else {
                    tTrigers.get(el).add(1);
                }
            }
        }
        for (int el = 0; el < truthTable.getCombinations().getFirst().size(); el++) {
            if (truthTable.getCombinations().getLast().get(el).equals(truthTable.getCombinations().getFirst().get(el))) {
                tTrigers.get(el).add(0);
            } else {
                tTrigers.get(el).add(1);
            }
        }
        for (var tTriger : tTrigers) {
            for (int i = 0; i < tTriger.size(); i++) {
                truthTable.getCombinations().get(i).add(tTriger.get(i));
            }
            karnosMapMethod=new KarnosMapMethod();
            karnosMapMethod.print(karnosMapMethod.KarnosMapMethod(truthTable, 1), 1);
            karnosMapMethod=new KarnosMapMethod();
            karnosMapMethod.print(karnosMapMethod.KarnosMapMethod(truthTable, 0), 0);
        }
        for (int i = 0; i < amnt; i++) {
            Statements.put("T" + i, (char) (955 + i));
        }
        truthTable.print();
    }
}
