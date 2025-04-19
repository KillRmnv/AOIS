package by.Romanoff.Aois;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicExpressionParser {
    private ArrayList<Pattern> expressionPatterns = new ArrayList<>();
    private Matcher expressionMatcher = null;

    LogicExpressionParser() {
        expressionPatterns.add(Pattern.compile("!+\\d"));
        expressionPatterns.add(Pattern.compile("!+[α-ω]"));
        expressionPatterns.add(Pattern.compile("\\(\\d+\\)"));
        expressionPatterns.add(Pattern.compile("\\(([α-ω]+|\\d+)&([α-ω]+|\\d+)\\)"));
        expressionPatterns.add(Pattern.compile("\\(([α-ω]+|\\d+)\\|([α-ω]+|\\d+)\\)"));
        expressionPatterns.add(Pattern.compile("\\(([α-ω]+|\\d+)->([α-ω]+|\\d+)\\)"));
        expressionPatterns.add(Pattern.compile("\\(([α-ω]+|\\d+)~([α-ω]+|\\d+)\\)"));
        expressionPatterns.add(Pattern.compile("([α-ω]|\\d+)+&([α-ω]|\\d+)+"));
        expressionPatterns.add(Pattern.compile("([α-ω]|\\d+)+\\|([α-ω]|\\d+)+"));
        expressionPatterns.add(Pattern.compile("([α-ω]|\\d+)+->([α-ω]|\\d+)+"));
        expressionPatterns.add(Pattern.compile("([α-ω]|\\d+)+~([α-ω]|\\d+)+"));
    }

    private String replaceStatements(String expression, LinkedHashMap<String, Character> Statements) {
        Pattern lines = Pattern.compile("\\w+");
        String copy = new String(expression);
        expressionMatcher = lines.matcher(expression);
        Set<String> linesSet = new HashSet<>();
        while (expressionMatcher.find()) {
            linesSet.add(expressionMatcher.group());
        }

        List<String> linesArray=new ArrayList<>(linesSet);
        linesArray.sort(String::compareTo);

        int num = 1;
        for (String line : linesArray) {
            Statements.put(line, (char) (Statements.size() + 945));
            expression = expression.replaceAll(line, String.valueOf((char) (num + 944)));
            num++;
        }

        return expression;
    }

    private String putInResult(int i, HashMap<Integer, String> result, String expression) {
        String key = expressionMatcher.group();
        if (i == 8) {
            result.put(result.size(), key);
            expression = replaceKeyInExpression(expression, key, result.size());
        } else if (i == 2) {
            int size = key.length();
            String logicExpression = key.substring(1, 2);
            key += "\\(" + key.substring(1, key.length() - 1) + "\\)";
            key = key.substring(size);
            expression = expression.replaceAll(key, logicExpression);
        } else if (i == 1) {
            expression = expression.replaceAll(key, String.valueOf(result.size()));
            result.put(result.size(), key);
        } else if (i > 2 && i < 7) {
            String logicExpression = expressionMatcher.group();
            int size = key.length();
            key += "\\(" + key.substring(1, key.length() - 1) + "\\)";
            key = key.substring(size);
            logicExpression = logicExpression.substring(1, logicExpression.length() - 1);
            result.put(result.size(), logicExpression);
            if (i == 4)
                expression = replaceKeyInExpression(expression, key, result.size());
            else
                expression = expression.replaceAll(key, String.valueOf(result.size() - 1));
        } else {
            result.put(result.size(), key);
            expression = expression.replaceAll(key, String.valueOf(result.size() - 1));
        }
        return expression;
    }

    public HashMap<Integer, String> parseOnBasicExpressions(String expression, LinkedHashMap<String, Character> Statements) {
        HashMap<Integer, String> result = new HashMap<>();
        expression = replaceStatements(expression, Statements);
        for (int i = 0; i < expressionPatterns.size(); i++) {
            expressionMatcher = expressionPatterns.get(i).matcher(expression);
            if (expressionMatcher.find()) {
                expression = putInResult(i, result, expression);
                i = -1;
            }
        }
        return result;
    }

    private static String replaceKeyInExpression(String expression, String key, int resultSize) {
        String escapedKey = key.replace("|", "\\|");
        return expression.replaceAll(escapedKey, String.valueOf(resultSize - 1));
    }
}