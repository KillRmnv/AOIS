package by.Romanoff.Aois;

import java.util.*;

public class CalculationMethod {
    public Map<Integer, String> applyMethodSknf(Map<Integer, String> sknf, int amnt) {
        sknf = GluerOfLogicExpression.glueLogicExpression(sknf, sknf.size(), amnt);
        return erasingImplicants(sknf, true);
    }

    public Map<Integer, String> applyMethodSdnf(Map<Integer, String> sdnf, int amnt) {
        sdnf = GluerOfLogicExpression.glueLogicExpression(sdnf, sdnf.size(), amnt);
        return erasingImplicants(sdnf, false);
    }

    // true--SKNF
    //false--SDNF
    private boolean equalStatements(Map<Integer, String> sknfOrSdnf,
                                    Map<Integer, String> sknfOrSdnfWithoutImplicant, boolean type,
                                    int numOfAnthrExpressionTraverse) {
        if (type) {
            int amnt = numOfAnthrExpressionTraverse;
            for (; sknfOrSdnf.containsKey(amnt); amnt++) ;
            if (amnt % 10 == 1) {
                sknfOrSdnfWithoutImplicant.put(numOfAnthrExpressionTraverse, "0");
                return false;
            } else
                sknfOrSdnfWithoutImplicant.remove(numOfAnthrExpressionTraverse);

        } else {
            int amnt = numOfAnthrExpressionTraverse;
            for (; sknfOrSdnf.containsKey(amnt); amnt++) ;
            if (amnt % 10 == 1) {
                sknfOrSdnfWithoutImplicant.put(numOfAnthrExpressionTraverse, "1");
                return false;
            } else
                sknfOrSdnfWithoutImplicant.remove(numOfAnthrExpressionTraverse);
        }
        return true;
    }

    private boolean notEqualStatements(Map<Integer, String> sknfOrSdnf,
                                       Map<Integer, String> sknfOrSdnfWithoutImplicant, boolean type,
                                       int numOfAnthrExpressionTraverse, int numOfExpressionTraverse,
                                       Set<Integer> anthrExpressionTraverseSet) {
        String Statement = sknfOrSdnf.get(numOfAnthrExpressionTraverse);
        StringBuilder builder = new StringBuilder();
        if (Statement.charAt(0) == '!') {
            builder.append(Statement.substring(1));
        } else {
            builder.append("!");
            builder.append(Statement);
        }
        if (sknfOrSdnf.get(numOfExpressionTraverse).contentEquals(builder)) {
            if (type) {
                for (var key : anthrExpressionTraverseSet) {
                    sknfOrSdnfWithoutImplicant.remove(key);
                }
                sknfOrSdnfWithoutImplicant.put((anthrExpressionTraverseSet.iterator().next() / 10) * 10 + 1, "1");
                return false;
            } else {

                for (var key : anthrExpressionTraverseSet) {
                    sknfOrSdnfWithoutImplicant.remove(key);
                }
                sknfOrSdnfWithoutImplicant.put((anthrExpressionTraverseSet.iterator().next() / 10) * 10 + 1, "0");
                return false;
            }
        }
        return true;
    }

    // метод берет одно утверждение и сравнивает с другим.Если совпадает для СДНФ,просто убирем его.Если совпадает для СКНФ и количество утверждений равно 1, заменяем на 0.
    //если совпадает Для СДНФ с !, вставляем 0 и удалем все остальное.Если совпадает для СКНФ с !, то просто удаляем все остальные
    private boolean checkForStatementsEqualness(Map<Integer, String> sknfOrSdnf,
                                                Map<Integer, String> sknfOrSdnfWithoutImplicant, boolean type,
                                                int numOfAnthrExpressionTraverse, int numOfExpressionTraverse,
                                                Set<Integer> anthrExpressionTraverseSet
    ) {
        if (sknfOrSdnf.get(numOfAnthrExpressionTraverse).equals(sknfOrSdnf.get(numOfExpressionTraverse))) {
            return equalStatements(sknfOrSdnf, sknfOrSdnfWithoutImplicant, type, numOfAnthrExpressionTraverse);
        } else {
            return notEqualStatements(sknfOrSdnf, sknfOrSdnfWithoutImplicant, type, numOfAnthrExpressionTraverse, numOfExpressionTraverse, anthrExpressionTraverseSet);
        }
    }

    // Метод удаляет конституэнту numOfExpressionTraverse в конституэнте numOfAnthrExpressionTraverse. Если досрочно уничтожены все утверждения
    private void delImplicant(Map<Integer, String> sknfOrSdnf, int numOfexpression, int numOfAnthrExpression,
                              Map<Integer, String> sknfOrSdnfWithoutImplicant, boolean type) {

        Set<Integer> expressionTraverseSet = new HashSet<>();
        Set<Integer> anthrExpressionTraverseSet = new HashSet<>();

        for (var key : sknfOrSdnf.keySet()) {
            if (key / 10 == numOfexpression / 10) {
                expressionTraverseSet.add(key);
            } else if (key / 10 == numOfAnthrExpression / 10) {
                anthrExpressionTraverseSet.add(key);
            }
        }
        for (var numOfExpressionTraverse : expressionTraverseSet) {
            for (var numOfAnthrExpressionTraverse : anthrExpressionTraverseSet) {
                if (!checkForStatementsEqualness(sknfOrSdnf, sknfOrSdnfWithoutImplicant,
                        type, numOfAnthrExpressionTraverse, numOfExpressionTraverse, anthrExpressionTraverseSet))
                    return;
            }
        }
    }

    private boolean checkForImplicantValidness(boolean type, Map<Integer, String> sknfOrSdnf) {
        if (type && sknfOrSdnf.containsValue("0")) {
            return true;
        } else if (!type && sknfOrSdnf.containsValue("1")) {
            return true;
        }
        Set<String> uniqueStatements = new HashSet<>();
        if (!type) {
            uniqueStatements.add("0");
            uniqueStatements.add("(0)");
            if (sknfOrSdnf.size() < 2) {
                return false;
            }
            int Type = 1;
            if (type)
                Type = 0;
            List<String> constituentsList = Output.constituentsList(sknfOrSdnf, Type);
            constituentsList.removeAll(uniqueStatements);
            if (constituentsList.isEmpty()) {
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

    private void formKeys(Set<Integer> keysSet, List<Map<Integer, String>> keys, Map<Integer, String> sknfOrSdnf, boolean type) {

        for (int numOfexpression = 11; keysSet.contains(numOfexpression / 10); numOfexpression += 10) {
            Map<Integer, String> sknfOrSdnfWithoutImplicant = new HashMap<>(sknfOrSdnf);
            for (int numOfAnthrExpression = 11; keysSet.contains(numOfAnthrExpression / 10); numOfAnthrExpression += 10) {
                if (numOfAnthrExpression == numOfexpression) {
                    Set<Integer> anthrExpressionTraverseSet = new HashSet<>();
                    for (var key : sknfOrSdnf.keySet()) {
                        if (key / 10 == numOfAnthrExpression / 10) {
                            anthrExpressionTraverseSet.add(key);
                        }
                    }
                    for (var key : anthrExpressionTraverseSet) {
                        sknfOrSdnfWithoutImplicant.remove(key);
                    }
                    continue;
                }
                delImplicant(sknfOrSdnf, numOfexpression, numOfAnthrExpression, sknfOrSdnfWithoutImplicant, type);
            }
            if (checkForImplicantValidness(type, sknfOrSdnfWithoutImplicant)) {
                sknfOrSdnfWithoutImplicant = new HashMap<>(sknfOrSdnf);
                Set<Integer> anthrExpressionTraverseSet = new HashSet<>();
                for (var key : sknfOrSdnf.keySet()) {
                    if (key / 10 == numOfexpression / 10) {
                        anthrExpressionTraverseSet.add(key);
                    }
                }
                for (var key : anthrExpressionTraverseSet) {
                    sknfOrSdnfWithoutImplicant.remove(key);
                }
                keys.add(sknfOrSdnfWithoutImplicant);
            }
        }
    }

    // удаляет конституэнту в других конституэнтах(в цикле перебирает все)
    private Map<Integer, String> erasingImplicants(Map<Integer, String> sknfOrSdnf, boolean type) {
        Set<Integer> keysSet = new HashSet<>();
        for (var k : sknfOrSdnf.keySet()) {
            int newKey = k / 10;
            keysSet.add(newKey);
        }
        List<Map<Integer, String>> keys = new ArrayList<>();

        formKeys(keysSet, keys, sknfOrSdnf, type);

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

        return sknfOrSdnf;
    }
}
