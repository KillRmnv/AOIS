package Aois.Romanoff;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TableMethod {
    private List<List<Integer>> table;

    private Set<Integer> identifyRedundantImplicants(Set<Integer> questionableImplicants, Set<Integer> uncoveredConstituents, int type) {
        Map<Integer, Set<Integer>> coveredConstituents = new HashMap<>();
        for (Integer implicant : questionableImplicants) {
            Set<Integer> covered = new HashSet<>();
            for (int column = 0; column < table.get(implicant).size(); column++) {
                if (Objects.equals(table.get(implicant).get(column), type)) {
                    covered.add(column);
                }
            }
            coveredConstituents.put(implicant, covered);
        }

        List<Integer> implicantsList = new ArrayList<>(questionableImplicants);
        for (int k = 1; k <= implicantsList.size(); k++) {
            Set<Integer> solution = findCover(implicantsList, uncoveredConstituents, coveredConstituents, k, 0, new HashSet<>());
            if (solution != null) {
                for (var solutions : solution) {
                    questionableImplicants.remove(solutions);
                }
                questionableImplicants.addAll(identifyInvalidImplicants(type));

                return questionableImplicants;
            }
        }
        return null;
    }

    private Set<Integer> identifyInvalidImplicants(int type) {
        Set<Integer> invalidImplicants = new HashSet<>();
        for (var row = 0; row < table.size(); row++) {
            boolean check = true;
            for (var element : table.get(row)) {
                if (element != type) {
                    check = false;
                }
            }
            if (check) {
                invalidImplicants.add(row);
            }
        }
        return invalidImplicants;
    }

    private Set<Integer> findCover(List<Integer> implicantsList, Set<Integer> uncovered,
                                   Map<Integer, Set<Integer>> coverage, int k, int start, Set<Integer> current) {
        if (current.size() == k) {
            Set<Integer> covered = new HashSet<>();
            for (Integer imp : current) {
                covered.addAll(coverage.get(imp));
            }
            if (covered.containsAll(uncovered)) {
                return new HashSet<>(current);
            }
            return null;
        }

        for (int i = start; i < implicantsList.size(); i++) {
            current.add(implicantsList.get(i));
            Set<Integer> solution = findCover(implicantsList, uncovered, coverage, k, i + 1, current);
            if (solution != null) {
                return solution;
            }
            current.remove(implicantsList.get(i));
        }
        return null;
    }

    private void delRedundantImplicants(Set<Integer> redundantImplicants, HashMap<Integer, String> gluedExpression) {
        if (redundantImplicants == null) {
            return;
        }
        List<Integer> indexesRedundant = new ArrayList<>(redundantImplicants);
        indexesRedundant.sort(Comparator.reverseOrder()); // Сортируем в обратном порядке для безопасного удаления
        for (var implicant : indexesRedundant) {
            for (int indexToDel = (implicant+1) * 10+1; gluedExpression.containsKey(indexToDel); indexToDel += 1) {
                gluedExpression.remove(indexToDel);
            }
        }

        for (int index : indexesRedundant) {
            if (index >= 0 && index < table.size()) {
                table.remove(index);
            }
        }
    }
    private void formGluedExpression(HashMap<Integer, String> gluedExpression){
        List<Integer> keySet = new ArrayList<>(gluedExpression.keySet());
        keySet.sort(Integer::compareTo);
        int indexOfExpression = 11, indexTraverse = indexOfExpression;

        for (int i = 0; i < keySet.size() - 1; i++) {

            var line = gluedExpression.get(keySet.get(i));
            gluedExpression.remove(keySet.get(i));
            gluedExpression.put(indexTraverse, line);
            if (keySet.get(i + 1) / 10 == keySet.get(i) / 10) {
                indexTraverse++;
            } else {
                indexOfExpression += 10;
                indexTraverse = indexOfExpression;
            }
        }
        var line = gluedExpression.get(keySet.getLast());
        gluedExpression.remove(keySet.getLast());
        gluedExpression.put(indexTraverse, line);
    }
    private void formTable(HashMap<Integer, String> gluedExpression,List<String> constituents){
        for (int begNum = 11; gluedExpression.containsKey(begNum); begNum = begNum + 10) {
            table.add(new ArrayList<>());
            for (int indexOfConstituent = 0; indexOfConstituent < constituents.size(); indexOfConstituent++) {
                boolean check = true;
                for (int index = begNum; gluedExpression.containsKey(index); index++) {
                    Pattern pattern = Pattern.compile(gluedExpression.get(index));
                    Pattern patternNegation;
                    Matcher matcher;
                    boolean found = false;
                    if (gluedExpression.get(index).charAt(0) != '!') {
                        patternNegation = Pattern.compile("!" + gluedExpression.get(index));
                        matcher = patternNegation.matcher(constituents.get(indexOfConstituent));
                        if (matcher.find()) {
                            found = true;
                            check = false;
                        }
                    }
                    if (!found) {
                        matcher = pattern.matcher(constituents.get(indexOfConstituent));
                        if (!matcher.find())
                            check = false;

                    }
                }
                if (check) {
                    table.getLast().add(1);
                } else {
                    table.getLast().add(0);
                }
            }
        }
    }
    private void createTable(List<String> constituents, HashMap<Integer, String> gluedExpression, int type) {
        table = new ArrayList<>();
        Set<Integer> validImplicants = new HashSet<>(Set.of());
        Set<Integer> questionableImplicants = new HashSet<>();
        Boolean[] coveredConstituens = new Boolean[constituents.size()];
        for (int i = 0; i < constituents.size(); i++) {
            coveredConstituens[i] = false;
        }
        formGluedExpression(gluedExpression);
        formTable(gluedExpression,constituents);
        printTable(constituents, gluedExpression,type);
        int columns = table.getFirst().size();
        for (int i = 0; i < columns; i++) {
            List<Integer> nums = new ArrayList<>();
            for (int j = 0; j < table.size(); j++) {
                if (table.get(j).get(i) == 1) {
                    nums.add(j);
                }
            }
            if (nums.size() == 1) {
                validImplicants.add(nums.getFirst());
                coveredConstituens[i] = true;
            } else {
                for (var num : nums) {
                    if (!validImplicants.contains(num)) {
                        questionableImplicants.add(num);
                    } else {
                        coveredConstituens[i] = true;
                    }
                }
            }
        }
        for (var index : validImplicants) {
            questionableImplicants.remove(index);
        }
        validImplicants.clear();


        for (int coveredConstituent = 0; coveredConstituent < coveredConstituens.length; coveredConstituent++) {
            if (!coveredConstituens[coveredConstituent]) {
                boolean check = false;
                for (var row = 0; row < table.size(); row++) {
                    if (table.get(row).get(coveredConstituent) == 1 && !questionableImplicants.contains(row)) {
                        coveredConstituens[coveredConstituent] = true;
                        check = true;
                    }
                }
                if (!check)
                    validImplicants.add(coveredConstituent);
            }
        }
        if (!validImplicants.isEmpty())
            questionableImplicants = identifyRedundantImplicants(questionableImplicants, validImplicants, type);

        delRedundantImplicants(questionableImplicants, gluedExpression);
        printTable(constituents, gluedExpression,type);
    }

    private List<String> getKonstituents(HashMap<Integer, String> SKNForSDNF) {
        List<String> result = new ArrayList<String>();
        for (int begNum = 11; SKNForSDNF.containsKey(begNum); begNum = begNum + 10) {
            StringBuilder constituent = new StringBuilder();
            for (int traverse = begNum; SKNForSDNF.containsKey(traverse); traverse++) {
                constituent.append(SKNForSDNF.get(traverse));
            }
            result.add(constituent.toString());
        }
        return result;
    }

    private void printTable(List<String> constituents, HashMap<Integer, String> gluedExpression,int type) {

        List<String> glued = new ArrayList<>(Main.constituentsList(gluedExpression,type));

        int columnCount = constituents.size();
        int[] columnWidths = new int[columnCount + 1];
        for (int i = 0; i < glued.size(); i++) {
            columnWidths[0] = Math.max(columnWidths[0], glued.get(i).length());
        }
        for (int col = 0; col < columnCount; col++) {
            columnWidths[col + 1] = constituents.get(col).length();
        }

        System.out.printf("%-" + columnWidths[0] + "s  ", "");
        for (int col = 0; col < columnCount; col++) {
            System.out.printf("%-" + columnWidths[col + 1] + "s ", constituents.get(col));
        }
        System.out.println();

        for (int row = 0; row < table.size(); row++) {
            System.out.printf("%-" + columnWidths[0] + "s  ", glued.get(row));
            for (int col = 0; col < columnCount; col++) {
                System.out.printf("%-" + columnWidths[col + 1] + "s ", table.get(row).get(col));
            }
            System.out.println();
        }

    }

    public HashMap<Integer, String> applyMethod(HashMap<Integer, String> SKNForSDNF, int amnt, int type) {

        List<String> constituents = getKonstituents(SKNForSDNF);
        HashMap<Integer, String> gluedExpression = GluerOfLogicExpression.glueLogicExpression(SKNForSDNF, SKNForSDNF.size(), amnt);
        createTable(constituents, gluedExpression, type);
        return gluedExpression;
    }
}