package by.romanoff.aois;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    public void LogicExpressionProgramm() {
        System.out.println("Enter expression:");
        Scanner scanner = new Scanner(System.in);
        String expression = getInput(scanner);
        LogicExpressionParser parser = new LogicExpressionParser();
        TruthTable truthTable = new TruthTable();
        truthTable.createTruthTable(parser.parseOnBasicExpressions(expression,truthTable.getStatements()));
        truthTable.print();
        NormalFormCreator normalFormCreator = new NormalFormCreator();
        Map<String, Object> result ;
        result = normalFormCreator.sdnf(truthTable.getCombinations(), truthTable.getStatements());
        System.out.println("СДНФ" + result.get("result"));
        System.out.println("Числовая форма" + result.get("NumericalForm"));
        result = normalFormCreator.sknf(truthTable.getCombinations(), truthTable.getStatements());
        System.out.println("СКНФ" + result.get("result"));
        System.out.println("Числовая форма" + result.get("NumericalForm"));
        DecimalConverter decimalConverter = new DecimalConverter();
        ArrayList<Integer> normalForm = normalFormCreator.indexForm(truthTable);
        System.out.println("Индексная форма" + normalForm + "-" + decimalConverter.ConvertFrBinToDecimal(normalForm));
    }

    public String getInput(Scanner scanner) {
        while (true) {
            String expression = scanner.nextLine();
            int amnt = 0;
            Pattern LogicOperationSequence = Pattern.compile("(&|\\||~|->|!|\\()(&|\\||~|->|\\))");
            Pattern Symbols = Pattern.compile("\\w+");
            Matcher LogicOperationsMatch;
            LogicOperationsMatch = LogicOperationSequence.matcher(expression);
            if (!LogicOperationsMatch.find()) {
                Stack<String> brackets = new Stack<>();
                for (String symbol : expression.split("")) {
                    if (symbol.equals("(")) {
                        brackets.push(symbol);
                    } else if (symbol.equals(")")) {
                        if (brackets.empty())
                            continue;
                        brackets.pop();
                    }
                }
                if (brackets.isEmpty())
                    return expression;
            }
            LogicOperationsMatch = Symbols.matcher(expression);
            while (LogicOperationsMatch.find()) {
                amnt++;
            }
            if (amnt < 5) {
                return expression;
            }
        }

    }
}
