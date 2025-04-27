package by.Romanoff.Aois;

import lombok.Data;
import lombok.Getter;

import java.util.*;

@Data
public class TruthTable {
    @Getter
    private Map<Integer, String> BasicLogicExpressions = new HashMap<>();
    @Getter
    private Map<String, Character> Statements = new LinkedHashMap<>();
    @Getter
    private List<List<Integer>> combinations;

    public void createTruthTable(HashMap<Integer, String> basicLogicExpressions) {
        BasicLogicExpressions = basicLogicExpressions;
        combinations = allPossibleCombinations(Statements.size());
        int amntOfStatements = Statements.size();
        LogicExpressionAnalyser logicExpressionAnalyser = new LogicExpressionAnalyser();
        for (int i = 0; i < combinations.size(); i++) {
            for (int j = 0; j < BasicLogicExpressions.size(); j++) {
                combinations.get(i).add(logicExpressionAnalyser.isTrue((ArrayList<Integer>) combinations.get(i), BasicLogicExpressions.get(j), amntOfStatements) ? 1 : 0);
            }
        }
    }
    public void createLine(int line){
        LogicExpressionAnalyser logicExpressionAnalyser = new LogicExpressionAnalyser();
        for (int j = 0; j < BasicLogicExpressions.size(); j++) {
            combinations.get(line).add(logicExpressionAnalyser.isTrue((ArrayList<Integer>) combinations.get(line), BasicLogicExpressions.get(j), Statements.size()) ? 1 : 0);
        }
    }


    public List<List<Integer>> allPossibleCombinations(int amntOfStatements) {
        BinNumber binNumber = new BinNumber();
        List<Integer> number = new ArrayList<>();
        for (int i = 0; i < amntOfStatements + 1; i++) {
            number.add(0);
        }
        binNumber.Setter(new ArrayList<>(number));

        number.removeLast();
        number.add(1);
       List<List<Integer>> possibleCombinations = new ArrayList<>();
        for (int i = 0; i < Math.pow(2, amntOfStatements); i++) {
            possibleCombinations.add(new ArrayList<>(binNumber.Getter()));
            possibleCombinations.getLast().removeFirst();
            binNumber.Adding(number);
            List<Integer> binNumberArray = binNumber.Getter();
            for (; binNumberArray.size() - 1 != amntOfStatements; ) {
                binNumberArray.removeFirst();
                number.removeFirst();
            }
            //binNumber.Setter(binNumberArray);
        }
        return possibleCombinations;
    }
    //!(!(b|c)~a)

    private void changeSymbolsInStatements() {
        for (int expressionIndex=0 ; expressionIndex<BasicLogicExpressions.size() ; expressionIndex++) {
            String expression = BasicLogicExpressions.get(expressionIndex);
            StringBuilder newLine = new StringBuilder();
            for (int character = 0; character < expression.length(); character++) {
                if (expression.charAt(character) > 944) {
                    char symbol = expression.charAt(character);
                    for(var keys: Statements.keySet()){
                        if(symbol==Statements.get(keys)){
                            newLine.append(keys);
                        }
                    }
                  //  newLine.append(BasicLogicExpressions.symbol - 945);
                } else if (expression.charAt(character) > 47&&expression.charAt(character)<58) {
                    StringBuilder number = new StringBuilder();
                    for (int j = character; j < expression.length() && expression.charAt(j) > 47 && expression.charAt(j) < 58; j++) {
                        number.append(expression.charAt(j));
                    }
                    newLine.append('(');
                    newLine.append(BasicLogicExpressions.get(Integer.parseInt(number.toString())));
                    newLine.append(')');
                } else
                    newLine.append(expression.charAt(character));
            }
            expression = newLine.toString();
            BasicLogicExpressions.put(expressionIndex, expression);

        }
    }


    public void print() {
        changeSymbolsInStatements();
        int maxLength = 0;
        for (String expression : BasicLogicExpressions.values()) {
            if (expression.length() > maxLength) {
                maxLength = expression.length();
            }
        }
        for (String expression : Statements.keySet()) {
            if (expression.length() > maxLength) {
                maxLength = expression.length();
            }
        }

        for (String statement : Statements.keySet()) {

            System.out.print(statement + " ");
        }
        for (String expression : BasicLogicExpressions.values()) {
            System.out.printf("%-" + (maxLength + 1) + "s", expression);
        }
        System.out.println();


        // for 3 lab
        for (var combination : combinations) {
            for (int i = 0; i < Statements.size(); i++) {
                int value = combination.get(i);
                System.out.printf("%-" + (maxLength + 1) + "d", value);
            }

            for (int i = 0; i < BasicLogicExpressions.size(); i++) {
                int value = combination.get(i + Statements.size());
                System.out.printf("%-" + (maxLength + 1) + "d", value);
            }

            System.out.println();
        }
    }
}