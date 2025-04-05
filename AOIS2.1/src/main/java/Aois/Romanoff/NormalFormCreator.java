package Aois.Romanoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class NormalFormCreator {
    public HashMap<String, Object> SDNF(ArrayList<ArrayList<Integer>> truthTable, LinkedHashMap<String, Character> statements) {
        String result = new String();
        ArrayList<Integer> NumericalForm = new ArrayList<>();
        Iterator<String> keyIterator;
        HashMap<Integer,String> hashTableOfSDNF = new HashMap<>();
        int numberOfStatement=11;
        for (var line = 0; line < truthTable.size(); line++) {
            if (truthTable.get(line).getLast() == 1) {
                NumericalForm.add(line);
                keyIterator = statements.keySet().iterator();
                result += "(";
                int numberOfStatementsBuffer=numberOfStatement;
                for (int i = 0; keyIterator.hasNext(); i++) {
                    String key = keyIterator.next();
                    if (truthTable.get(line).get(i) == 1) {
                        result += key + "&";
                        hashTableOfSDNF.put(numberOfStatementsBuffer, key);
                        numberOfStatementsBuffer++;
                    } else {
                        result += "!" + key + "&";
                        hashTableOfSDNF.put(numberOfStatementsBuffer, "!"+key);
                        numberOfStatementsBuffer++;
                    }
                }
                numberOfStatement+=10;
                result = result.substring(0, result.length() - 1);
                result += ")";
                result += "|";
            }
        }
        result = result.substring(0, result.length() - 1);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("NumericalForm", NumericalForm);
        resultMap.put("result", result);
        resultMap.put("HashTableOfSDNF", hashTableOfSDNF);
        return resultMap;
    }

    public HashMap<String, Object> SKNF(ArrayList<ArrayList<Integer>> truthTable, LinkedHashMap<String, Character> statements) {
        String result = new String();
        HashMap<Integer,String> hashTableOfSKNF = new HashMap<>();
        int numberOfStatement=11;
        ArrayList<Integer> NumericalForm = new ArrayList<>();
        Iterator<String> keyIterator;
        for (var line = 0; line < truthTable.size(); line++) {
            if (truthTable.get(line).getLast() == 0) {
                NumericalForm.add(line);
                keyIterator = statements.keySet().iterator();
                result += "(";
                int numberOfStatementsBuffer=numberOfStatement;
                for (int i = 0; keyIterator.hasNext(); i++) {
                    String key = keyIterator.next();
                    if (truthTable.get(line).get(i) == 0) {
                        result += key + "|";
                        hashTableOfSKNF.put(numberOfStatementsBuffer, key);
                        numberOfStatementsBuffer++;
                    } else {
                        result += "!" + key + "|";
                        hashTableOfSKNF.put(numberOfStatementsBuffer, "!"+key);
                        numberOfStatementsBuffer++;
                    }
                }
                numberOfStatement+=10;
                result = result.substring(0, result.length() - 1);
                result += ")";
                result += "&";
            }
        }
        result = result.substring(0, result.length() - 1);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("NumericalForm", NumericalForm);
        resultMap.put("result", result);
        resultMap.put("HashTableOfSKNF", hashTableOfSKNF);
        return resultMap;
    }

    public ArrayList<Integer> IndexForm(TruthTable truthTable) {
        ArrayList<Integer> NumericalForm = new ArrayList<>();
        for (var line : truthTable.getCombinations()) {
            NumericalForm.add(line.getLast());
        }
        return NumericalForm;
    }
}