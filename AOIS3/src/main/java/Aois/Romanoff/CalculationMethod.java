package Aois.Romanoff;

import java.util.*;

public class CalculationMethod {
    public HashMap<Integer, String> applyMethodSKNF(HashMap<Integer, String> SKNF, int amnt) {
        SKNF = GluerOfLogicExpression.glueLogicExpression(SKNF, SKNF.size(), amnt);
        return erasingImplicants(SKNF, true);
    }

    public HashMap<Integer, String> applyMethodSDNF(HashMap<Integer, String> SDNF, int amnt) {
        SDNF = GluerOfLogicExpression.glueLogicExpression(SDNF, SDNF.size(), amnt);
        // printResult(SDNF,1);
        return erasingImplicants(SDNF, false);
    }

    // метод берет одно утверждение и сравнивает с другим.Если совпадает для СДНФ,просто убирем его.Если совпадает для СКНФ и количество утверждений равно 1, заменяем на 0.
    //если совпадает Для СДНФ с !, вставляем 0 и удалем все остальное.Если совпадает для СКНФ с !, то просто удаляем все остальные
    private boolean checkForStatementsEqualness(HashMap<Integer, String> SKNForSDNF,
                                                HashMap<Integer, String> SKNForSDNFwithoutImplicant, boolean type,
                                                int numOfAnthrExpressionTraverse, int numOfExpressionTraverse,
                                                Set<Integer> anthrExpressionTraverseSet
    ) {
        if (SKNForSDNF.get(numOfAnthrExpressionTraverse).equals(SKNForSDNF.get(numOfExpressionTraverse))) {
            if (type) {
                int amnt = numOfAnthrExpressionTraverse;
                for (; SKNForSDNF.containsKey(amnt); amnt++) ;
                if (amnt % 10 == 1) {
                    SKNForSDNFwithoutImplicant.put(numOfAnthrExpressionTraverse, "0");
                    return false;
                } else
                    SKNForSDNFwithoutImplicant.remove(numOfAnthrExpressionTraverse);

            } else {
                int amnt = numOfAnthrExpressionTraverse;
                for (; SKNForSDNF.containsKey(amnt); amnt++) ;
                if (amnt % 10 == 1) {
                    SKNForSDNFwithoutImplicant.put(numOfAnthrExpressionTraverse, "1");
                    return false;
                } else
                    SKNForSDNFwithoutImplicant.remove(numOfAnthrExpressionTraverse);
            }
        } else {
            String Statement = SKNForSDNF.get(numOfAnthrExpressionTraverse);
            StringBuilder builder = new StringBuilder();
            if (Statement.charAt(0) == '!') {
                builder.append(Statement.substring(1));
            } else {
                builder.append("!");
                builder.append(Statement);
            }
            if (SKNForSDNF.get(numOfExpressionTraverse).contentEquals(builder)) {
                if (type) {
                    for (var key : anthrExpressionTraverseSet) {
                        SKNForSDNFwithoutImplicant.remove(key);
                    }
                    SKNForSDNFwithoutImplicant.put((anthrExpressionTraverseSet.iterator().next() / 10) * 10 + 1, "1");
                    return false;
                } else {

                    for (var key : anthrExpressionTraverseSet) {
                        SKNForSDNFwithoutImplicant.remove(key);
                    }
                    SKNForSDNFwithoutImplicant.put((anthrExpressionTraverseSet.iterator().next() / 10) * 10 + 1, "0");
                    return false;
                }
            }
        }
        return true;
    }

    // Метод удаляет конституэнту numOfExpressionTraverse в конституэнте numOfAnthrExpressionTraverse. Если досрочно уничтожены все утверждения
    private void delImplicant(HashMap<Integer, String> SKNForSDNF, int numOfexpression, int numOfAnthrExpression,
                              HashMap<Integer, String> SKNForSDNFwithoutImplicant, boolean type) {

        Set<Integer> expressionTraverseSet = new HashSet<>();
        Set<Integer> anthrExpressionTraverseSet = new HashSet<>();

        for (var key : SKNForSDNF.keySet()) {
            if (key / 10 == numOfexpression / 10) {
                expressionTraverseSet.add(key);
            } else if (key / 10 == numOfAnthrExpression / 10) {
                anthrExpressionTraverseSet.add(key);
            }
        }
        for (var numOfExpressionTraverse : expressionTraverseSet) {
            for (var numOfAnthrExpressionTraverse : anthrExpressionTraverseSet) {
                if (!checkForStatementsEqualness(SKNForSDNF, SKNForSDNFwithoutImplicant,
                        type, numOfAnthrExpressionTraverse, numOfExpressionTraverse, anthrExpressionTraverseSet))
                    return;
            }
        }
    }

    private boolean checkForImplicantValidness(boolean type, HashMap<Integer, String> SKNForSDNF) {
        if (type && SKNForSDNF.containsValue("0")) {
            return true;
        } else if (!type && SKNForSDNF.containsValue("1")) {
            return true;
        }
        Set<String> uniqueStatements = new HashSet<>();
        if (!type) {
            uniqueStatements.add("0");
            uniqueStatements.add("(0)");
            if(SKNForSDNF.size()<2){
                return false;
            }
            List<String> constituentsList = Main.constituentsList(SKNForSDNF);
            constituentsList.removeAll(uniqueStatements);
            if(constituentsList.isEmpty()) {
                return false;
            }
            StringBuilder expression = new StringBuilder();
            for (int i = 0; i < constituentsList.size() - 1; i++) {
                expression.append(constituentsList.get(i));
                expression.append("|");
            }
            expression.append(constituentsList.getLast());
            LogicExpressionParser parser = new LogicExpressionParser();
            TruthTable truthTable = new TruthTable();
            truthTable.createTruthTable(parser.parseOnBasicExpressions(expression.toString(), truthTable.getStatements()));
            for (var row : truthTable.getCombinations()) {
                if (row.getLast() != 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // удаляет конституэнту в других конституэнтах(в цикле перебирает все)
    private HashMap<Integer, String> erasingImplicants(HashMap<Integer, String> SKNForSDNF, boolean type) {
        Set<Integer> keysSet = new HashSet<>();
        for (var k : SKNForSDNF.keySet()) {
            int newKey = k / 10;
            keysSet.add(newKey);
        }
        List<HashMap<Integer, String>> keys = new ArrayList<>();
        for (int numOfexpression = 11; keysSet.contains(numOfexpression / 10); numOfexpression += 10) {
            HashMap<Integer, String> SKNForSDNFwithoutImplicant = new HashMap<>(SKNForSDNF);
            for (int numOfAnthrExpression = 11; keysSet.contains(numOfAnthrExpression / 10); numOfAnthrExpression += 10) {
                if (numOfAnthrExpression == numOfexpression) {
                    Set<Integer> anthrExpressionTraverseSet = new HashSet<>();
                    for (var key : SKNForSDNF.keySet()) {
                        if (key / 10 == numOfAnthrExpression / 10) {
                            anthrExpressionTraverseSet.add(key);
                        }
                    }
                    for (var key : anthrExpressionTraverseSet) {
                        SKNForSDNFwithoutImplicant.remove(key);
                    }
                    continue;
                }
                delImplicant(SKNForSDNF, numOfexpression, numOfAnthrExpression, SKNForSDNFwithoutImplicant, type);
            }
            if (checkForImplicantValidness(type, SKNForSDNFwithoutImplicant)) {
                SKNForSDNFwithoutImplicant = new HashMap<>(SKNForSDNF);
                Set<Integer> anthrExpressionTraverseSet = new HashSet<>();
                for (var key : SKNForSDNF.keySet()) {
                    if (key / 10 == numOfexpression / 10) {
                        anthrExpressionTraverseSet.add(key);
                    }
                }
                for (var key : anthrExpressionTraverseSet) {
                    SKNForSDNFwithoutImplicant.remove(key);
                }
                keys.add(SKNForSDNFwithoutImplicant);
            }
        }

        if (keys.size() == 1) {
            return keys.getFirst();
        }
        if (keys.size() > 1) {
            int size = keys.size();
            for (int i = 0; i < size; i++)
                keys.add(erasingImplicants(keys.get(i), type));
            for (int i = 0; i < size; i++)
                keys.remove(i);
            size = 100000;
            int index = -1;
            for (int i = 0; i < keys.size(); i++)
                if (keys.get(i).size() < size) {
                    size = keys.get(i).size();
                    index = i;
                }
            return keys.get(index);
        }

        return SKNForSDNF;
    }
}
// true--SKNF
//false--SDNF