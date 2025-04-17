package Aois.Romanoff;

import java.util.*;

public class GluerOfLogicExpression {
    private static int findEqStatemets(List<String> EqualStatements, int numOfAnthrExpression, int numOfexpression, HashMap<Integer, String> SDNFOrSKNF, int amnt, Integer amntOfEqual) {
        int numOfExpressionTraverse = numOfexpression;
        int numOfAnthrExpressionTraverse;
        for (int i = 0; i < amnt; i++) {
            numOfExpressionTraverse = i + numOfexpression;
            numOfAnthrExpressionTraverse = i + numOfAnthrExpression;
            if (SDNFOrSKNF.get(numOfAnthrExpressionTraverse).equals(SDNFOrSKNF.get(numOfExpressionTraverse))) {
                EqualStatements.add(SDNFOrSKNF.get(numOfAnthrExpressionTraverse));
                amntOfEqual++;
            } else {
                EqualStatements.add("-");
            }
        }

        if (amnt != amntOfEqual)
            return amntOfEqual;
        return -1;
    }

    public static HashMap<Integer, String> glueLogicExpression(HashMap<Integer, String> SDNFOrSKNF, int previouseResultSize, int amnt) {
        HashMap<Integer, String> result = new HashMap<>();
        List<String> keysOfEqualStatements = new ArrayList<>();
        int keysOfResult = 11;
        boolean added=false;
        List<Boolean> used = new ArrayList<>();
        List<Boolean> equal=new ArrayList<>();
        for (int i = 0; i < previouseResultSize; i++) {
            used.add(false);
            equal.add(false);
        }
        for (int numOfexpression = 11; SDNFOrSKNF.containsKey(numOfexpression); numOfexpression += 10) {
            if(equal.get(numOfexpression/10-1)) {
                continue;
            }
            for (int numOfAnthrExpression = numOfexpression + 10; SDNFOrSKNF.containsKey(numOfAnthrExpression); numOfAnthrExpression += 10) {
                int amntOfEqual = 0;
                amntOfEqual = findEqStatemets(keysOfEqualStatements, numOfAnthrExpression, numOfexpression, SDNFOrSKNF, amnt, amntOfEqual);

                if (amntOfEqual >= amnt - 1) {
                    added = true;
                    used.set(numOfAnthrExpression / 10 - 1, true);
                    int keysOfResultTraverse = keysOfResult;
                    for (var key : keysOfEqualStatements) {
                        result.put(keysOfResultTraverse, key);
                        keysOfResultTraverse++;
                    }
                    keysOfResult += 10;
                }
                if (amntOfEqual == -1) {
                    used.set(numOfAnthrExpression / 10 - 1, true);
                    equal.set(numOfAnthrExpression / 10 - 1, true);
                }
                keysOfEqualStatements.clear();
            }
        }
        for (var constituent = 0; constituent < used.size(); constituent++) {
            if (!used.get(constituent)) {
                int keysOfResultTraverse = keysOfResult;
                int key = (constituent + 1) * 10 + 1;
                while (SDNFOrSKNF.containsKey(key)) {
                    result.put(keysOfResultTraverse, SDNFOrSKNF.get(key));
                    keysOfResultTraverse++;
                    key++;
                }
                keysOfResult += 10;
            }
        }
        if (added)
            return glueLogicExpression(new HashMap<>(result), result.size(), amnt);

        List<Integer> keys = new ArrayList<>(result.keySet());
        keys.sort(Integer::compareTo);
        for (int i = 0; i < keys.size(); i++) {
            if (result.get(keys.get(i)).equals("-")) {
                result.remove(keys.get(i));
            }
        }
        return result;
    }
}