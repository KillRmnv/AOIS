package by.Romanoff.Aois;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
@Data
public class GLSearchOperations {
    private final String G;
    private final String L;
    private TruthTable truthTable;
    private Matrix matrix;

    public GLSearchOperations(Matrix matrix) {
        this.matrix = matrix;
        G = "G|(!A&B&!L)";
        L = "L|(A&!B&!G)";
        truthTable = new TruthTable();
        List<List<Integer>> combinations = new ArrayList<>();
        for (int j = 0; j < matrix.size(); j++) {
            combinations.add(new ArrayList<>());
        }
        combinations.getFirst().add(0);
        combinations.getFirst().add(0);
        truthTable.setCombinations(combinations);
    }

    public int compare(int[] A, int[] S, int currentIteration) {
        LogicExpressionParser parser = new LogicExpressionParser();
        List<List<Integer>> combinations = truthTable.getCombinations();
        int traverse = currentIteration;
        if (combinations.get(traverse).size() > 2) {
            while (!combinations.get(traverse).isEmpty()) {
                combinations.get(traverse).clear();
                if (traverse != 15)
                    traverse++;
                else break;
            }
            if (currentIteration == 0) {
                combinations.get(currentIteration).add(0);
                combinations.get(currentIteration).add(0);
            }
        }
        combinations.get(currentIteration).addFirst(S[currentIteration]);
        combinations.get(currentIteration).addFirst(A[currentIteration]);
        truthTable.setStatements(new LinkedHashMap<>());
        truthTable.setBasicLogicExpressions(parser.parseOnBasicExpressions(L, truthTable.getStatements()));
        truthTable.createLine(currentIteration);
        int L0 = truthTable.getCombinations().get(currentIteration).getLast();
        if (currentIteration != 15) {
            combinations.get(currentIteration + 1).add(truthTable.getCombinations().get(currentIteration).getLast());
        }
        while (combinations.get(currentIteration).size() > 4) {
            combinations.get(currentIteration).removeLast();
        }
        truthTable.setStatements(new LinkedHashMap<>());
        truthTable.setBasicLogicExpressions(parser.parseOnBasicExpressions(G, truthTable.getStatements()));
        truthTable.createLine(currentIteration);
        int G0 = truthTable.getCombinations().get(currentIteration).getLast();
        if (L0 == 1 && G0 == 0) {
            return 1;
        } else if (G0 == 1 && L0 == 0) {
            return -1;
        }
        if (currentIteration != 15) {
            combinations.get(currentIteration + 1).add(truthTable.getCombinations().get(currentIteration).getLast());
            while (combinations.get(currentIteration).size() > 4) {
                combinations.get(currentIteration).removeLast();
            }
            return compare(A, S, currentIteration + 1);
        }
        while (combinations.get(currentIteration).size() > 4) {
            combinations.get(currentIteration).removeLast();
        }
        if (L0 == 0 && G0 == 0) {
            return 0;
        }
        return 0;
    }

    public int findMax(int num, int last) {
        for (int i = 0; i < last; i++) {

            if (compare(matrix.getWord(num), matrix.getWord(i), 0) < 0) {
                return i;
            }
        }
        return num;
    }

    public void sort() {
        DecimalConverter converter = new DecimalConverter();
        for (int last = matrix.size() - 1; last >= 0; last--) {
            int max = last;
            int maxLast = -1;
            while (maxLast != max) {
                maxLast = max;
                max = findMax(maxLast, last);

            }
            int[] wordToSwap = matrix.getWord(last);
            matrix.writeWord(last, matrix.getWord(max));
            matrix.writeWord(max, wordToSwap);
        }
    }
}