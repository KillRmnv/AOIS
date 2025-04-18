package Aois.Romanoff;

import javafx.util.Pair;

import java.util.*;

public class KarnosMapMethod {
    private List<List<Integer>> map;
    private List<Graph> graphs;

    KarnosMapMethod() {
        map = new ArrayList<>();
        graphs = new ArrayList<>();
    }

    private void fillMap(List<List<Integer>> combinations, List<String> rowsGreysCode, List<String> columnsGreysCode, int type) {
        HashMap<String, List<Integer>> combinationGreyCode = new HashMap<>();
        for (int row = 0; row < rowsGreysCode.size(); row++) {
            this.map.add(new ArrayList<>());
            for (int col = 0; col < columnsGreysCode.size(); col++) {
                this.map.getLast().add(0);
                StringBuilder map = new StringBuilder();
                map.append(rowsGreysCode.get(row));
                map.append(columnsGreysCode.get(col));
                combinationGreyCode.put(map.toString(), new ArrayList<>(Arrays.asList(row, col)));
            }
        }
        int numColumns = 0;
        int numRows = 0;
        HashMap<String, Integer> graphsRelativeToTheirCode = new HashMap<>();
        boolean difficultCase = false;
        for (int i = 0; i < Math.floor(Math.pow(2, rowsGreysCode.getFirst().length() + columnsGreysCode.getFirst().length() - 4)); i++) {
            graphs.add(new Graph());
            difficultCase = true;
            if (numColumns >= columnsGreysCode.size() / 4) {
                numColumns = 0;
                numRows++;
            }
            String rowsGreyCode = rowsGreysCode.get(numRows * 4).substring(0, rowsGreysCode.get(numRows * 4).length() - 2);
            String columnGreyCode = columnsGreysCode.get(numColumns * 4).substring(0, columnsGreysCode.get(numColumns * 4).length() - 2);
            graphsRelativeToTheirCode.put(rowsGreyCode + columnGreyCode, i);
            graphs.getLast().setGraphCode(rowsGreyCode + columnGreyCode);
            numColumns++;
        }
        if (graphs.isEmpty()) {
            graphs.add(new Graph());
        }

        // из таблички формирует строку, которая будет использована для поиска инднксов соответствующего кода грея
        for (int row = 0; row < combinations.size(); row++) {
            StringBuilder table = new StringBuilder();
            for (int j = 0; j < rowsGreysCode.getFirst().length() + columnsGreysCode.getFirst().length(); j++)
                table.append(String.valueOf(combinations.get(row).get(j)));
            List<Integer> indexes = combinationGreyCode.get(table.toString());
            table.delete(0, rowsGreysCode.getFirst().length() - 2);
            table.delete(2, 2 + columnsGreysCode.getFirst().length() - 2);
            Node newNode = new Node((indexes.getFirst() % 4) * 10 + indexes.getLast() % 4, table.toString());
            //  int index = (indexes.getFirst() / 4) * (indexes.getLast()/4+1) + indexes.getLast() / 4;
            int index = (indexes.getFirst() / 4) * (columnsGreysCode.size() / 4) + indexes.getLast() / 4;
            if (combinations.get(row).getLast() == type)
                graphs.get(index).getNodes().put(newNode.getData(), newNode);

            this.map.get(indexes.getFirst()).set(indexes.getLast(), combinations.get(row).getLast());
        }
        if (difficultCase) {
            for (int graph = 0; graph < graphs.size(); graph++) {
                graphs.get(graph).createGraph(4, 4);
                List<Integer> neightbors = new ArrayList<>();
                StringBuilder neightborGraphCode = new StringBuilder();
                String graphCode = graphs.get(graph).getGraphCode();
                int graphCodeSize = graphCode.length();
                for (int i = 0; i < graphCodeSize; i++) {
                    neightborGraphCode.append(graphCode);
                    if (graphCode.charAt(i) == '0')
                        neightborGraphCode.replace(i, i + 1, "1");
                    else
                        neightborGraphCode.replace(i, i + 1, "0");
                    neightbors.add(graphsRelativeToTheirCode.get(neightborGraphCode.toString()));
                    neightborGraphCode = new StringBuilder();
                }
                graphs.get(graph).setNeightbors(neightbors);
            }
        }
    }

    private List<String> createGreysCode(int amnt) {
        List<String> greysCode = new ArrayList<>();
        greysCode.add("0");
        greysCode.add("1");
        for (int i = 1; i < amnt; i++) {
            List<String> reversedGreysCode = new ArrayList<>(greysCode);
            Collections.reverse(reversedGreysCode);
            for (int j = 0; j < reversedGreysCode.size(); j++) {
                StringBuilder newGreysCode = new StringBuilder();
                newGreysCode.append("1");
                newGreysCode.append(reversedGreysCode.get(j));
                reversedGreysCode.set(j, newGreysCode.toString());
            }
            for (int j = 0; j < greysCode.size(); j++) {
                StringBuilder newGreysCode = new StringBuilder();
                newGreysCode.append("0");
                newGreysCode.append(greysCode.get(j));
                greysCode.set(j, newGreysCode.toString());
            }
            greysCode.addAll(reversedGreysCode);
        }
        return greysCode;
    }

    private boolean checkForFull() {
        for (var raw : map) {
            for (var cell : raw) {
                if (cell == 0)
                    return false;
            }
        }
        return true;
    }

    private List<int[]> checkFor4() {
        List<int[]> result = new ArrayList<>();
        for (int j = 0; j < map.getFirst().size() - 1; j++) {
            if (map.getFirst().get(j) == 1 && map.getFirst().get(j + 1) == 1
                    && map.getLast().get(j) == 1 && map.getLast().get(j + 1) == 1) {
                result.add(new int[]{j, j + 11});
                j++;
            }
        }
        return result;
    }

    private List<int[]> checkFor2(List<List<Integer>> covered) {
        List<int[]> result = new ArrayList<>();
        for (int j = 0; j < map.getFirst().size() - 1; j++) {
            if ((map.getFirst().get(j) == 1 && map.getFirst().get(j + 1) == 1) &&
                    (covered.getFirst().get(j) == 0 || covered.getFirst().get(j + 1) == 0)) {
                result.add(new int[]{j, j + 1});
            } else if ((map.getFirst().get(j) == 1 && map.getLast().get(j) == 1) &&
                    (covered.getFirst().get(j) == 0 || covered.getLast().get(j) == 0)) {
                result.add(new int[]{j, j + 10});
            } else if ((covered.getLast().get(j) == 1 && covered.getLast().get(j + 1) == 1) &&
                    (covered.getLast().get(j) == 1 || covered.getLast().get(j + 1) == 1)) {
                result.add(new int[]{j + 10, j + 11});
            }
        }
        if ((map.getFirst().getLast() == 1 && map.getLast().getLast() == 1) &&
                (covered.getFirst().getLast() == 0 || covered.getLast().getLast() == 0)) {
            result.add(new int[]{map.getFirst().size() - 1, map.getFirst().size() + 9});
        }
        return result;
    }

    private List<int[]> rectanglesInSimpleCase() {
        List<int[]> rectangles = new ArrayList<>();
        if (checkForFull()) {
            rectangles.add(new int[]{0, map.size() * 10 + map.getFirst().size()});
            return rectangles;
        }
        rectangles.addAll(checkFor4());
        List<List<Integer>> covered = new ArrayList<>(map);
        for (var raw : covered) {
            for (var cell : raw)
                raw.set(cell, 0);
        }
        for (var rectangle : rectangles) {
            covered.getFirst().set(rectangle[0], 1);
            covered.getFirst().set(rectangle[0] + 1, 1);
            covered.getLast().set(rectangle[0], 1);
            covered.getLast().set(rectangle[0] + 1, 1);
        }
        rectangles.addAll(checkFor2(covered));

        return rectangles;
    }

    private Set<Set<Integer>> findSquares(Node node, Set<Integer> covered) {
        Set<Set<Integer>> squares = new HashSet<>();
        if (node.getRight() != -10 && node.getRightDown() != -10 && node.getDown() != -10) {
            Set<Integer> square = new HashSet<>();
            square.add(node.getData());
            square.add(node.getRight());
            square.add(node.getRightDown());
            square.add(node.getDown());
            covered.addAll(square);

            squares.add(square);
        }
        if (node.getLeft() != -10 && node.getLeftUp() != -10 && node.getUp() != -10) {
            Set<Integer> square = new HashSet<>();
            square.add(node.getData());
            square.add(node.getLeft());
            square.add(node.getLeftUp());
            square.add(node.getUp());
            covered.addAll(square);
            squares.add(square);
        }
        if (node.getDown() != -10 && node.getLeftDown() != -10 && node.getLeft() != -10) {
            Set<Integer> square = new HashSet<>();
            square.add(node.getData());
            square.add(node.getDown());
            square.add(node.getLeftDown());
            square.add(node.getLeft());

            covered.addAll(square);
            squares.add(square);
        }
        if (node.getUp() != -10 && node.getRightUp() != -10 && node.getRight() != -10) {
            Set<Integer> square = new HashSet<>();
            square.add(node.getData());
            square.add(node.getUp());
            square.add(node.getRightUp());
            square.add(node.getRight());
            covered.addAll(square);

            squares.add(square);
        }
        return squares;
    }

    private Set<Set<Integer>> findRectangles(Node node, Set<Integer> covered, int num) {
        Node traverse = node;
        Set<Set<Integer>> rectangles = new HashSet<>();
        Set<Integer> rectangle = new HashSet<>();
        rectangle.add(traverse.getData());

        int amnt = 1;
        while (traverse.getRight() != -10) {
            traverse = graphs.get(num).getNodes().get(traverse.getRight());
            if (!rectangle.contains(traverse.getData())) {
                rectangle.add(traverse.getData());
                covered.add(traverse.getData());
                amnt++;
            } else {
                break;
            }
        }
        if (amnt == 4) {
            rectangles.add(new HashSet<>(rectangle));
            covered.add(node.getData());

        }
        rectangle.clear();
        amnt = 1;
        traverse = node;
        rectangle.add(traverse.getData());
        while (traverse.getUp() != -10) {
            traverse = graphs.get(num).getNodes().get(traverse.getUp());
            if (!rectangle.contains(traverse.getData())) {
                rectangle.add(traverse.getData());
                covered.add(traverse.getData());
                amnt++;
            } else {
                break;
            }
        }
        if (amnt == 4) {
            rectangles.add(new HashSet<>(rectangle));
            covered.add(node.getData());
        }
        if (!rectangles.isEmpty()) {
            return rectangles;
        }


        return rectangles;
    }

    private boolean delRectangles(Set<Set<Integer>> squares, Set<Set<Integer>> rectangles, Node traverse, Set<Integer> covered) {
        for (var square : squares) {
            if (square.contains(traverse.getUp())) {
                Set<Set<Integer>> Squares = new HashSet<>(squares);
                Squares.remove(square);
                if (checkCovered(Squares, rectangles, covered.size())) {
                    squares.remove(square);
                } else
                    return true;
            }
        }
        if (squares.isEmpty())
            return true;
        return false;
    }

    private void checkForSmallRectangles(Node node, Set<Integer> covered, Set<Set<Integer>> squares, Set<Set<Integer>> rectangles) {
        Node traverse = node;
        Set<Integer> rectangle = new HashSet<>();
        if (traverse.getUp() != -10) {
            Set<Integer> coveredCopy = new HashSet<>(covered);
            rectangle.add(traverse.getData());
            rectangle.add(traverse.getUp());
            covered.add(traverse.getData());
            covered.add(traverse.getUp());
            rectangles.add(rectangle);
            if (!delRectangles(squares, rectangles, traverse, covered) && !delRectangles(rectangles, squares, traverse, covered)) {
                rectangles.remove(rectangle);
            } else {
                covered = new HashSet<>(coveredCopy);
                return;
            }
        }
        rectangle.clear();
        if (traverse.getDown() != -10) {
            Set<Integer> coveredCopy = new HashSet<>(covered);
            rectangle.add(traverse.getData());
            rectangle.add(traverse.getDown());
            covered.add(traverse.getData());
            covered.add(traverse.getDown());
            rectangles.add(rectangle);
            if (!delRectangles(squares, rectangles, traverse, covered) && !delRectangles(rectangles, squares, traverse, covered)) {

                rectangles.remove(rectangle);
            } else {
                covered = new HashSet<>(coveredCopy);
                return;
            }
        }
        rectangle.clear();
        if (traverse.getLeft() != -10) {
            Set<Integer> coveredCopy = new HashSet<>(covered);
            rectangle.add(traverse.getData());
            rectangle.add(traverse.getLeft());
            covered.add(traverse.getData());
            covered.add(traverse.getLeft());
            rectangles.add(rectangle);
            if (!delRectangles(squares, rectangles, traverse, covered) && !delRectangles(rectangles, squares, traverse, covered)) {
                rectangles.remove(rectangle);
            } else {
                covered = new HashSet<>(coveredCopy);
                return;
            }
        }
        rectangle.clear();
        if (traverse.getRight() != -10) {
            Set<Integer> coveredCopy = new HashSet<>(covered);
            rectangle.add(traverse.getData());
            rectangle.add(traverse.getRight());
            covered.add(traverse.getData());
            covered.add(traverse.getRight());
            rectangles.add(rectangle);
            if (!delRectangles(squares, rectangles, traverse, covered) && !delRectangles(rectangles, squares, traverse, covered)) {
                rectangles.remove(rectangle);
            } else {
                covered = new HashSet<>(coveredCopy);
                return;
            }

        }
        rectangle.clear();
    }

    private boolean checkCovered(Set<Set<Integer>> squares, Set<Set<Integer>> rectangles, int size) {
        Set<Integer> covered = new HashSet<>();
        for (var rectangle : rectangles) {
            covered.addAll(rectangle);
        }
        for (var square : squares) {
            covered.addAll(square);
        }
        if (covered.size() == size) {
            return true;
        }
        return false;
    }

    private boolean checkFor8(int num, Set<Integer> fieldToCheck) {
        Node currentNode = graphs.get(num).getNodes().get(fieldToCheck.iterator().next());
        Node neightborNode = new Node(-10, new String(""));
        int currentNodeData = currentNode.getData();
        int neightborNodeData = 0;
        if (!fieldToCheck.contains(currentNode.getRight())) {
            neightborNode = graphs.get(num).getNodes().get(currentNode.getLeft());
            neightborNodeData = neightborNode.getData();
            while ((neightborNode.getUp() != -10 && currentNode.getUp() != -10) &&
                    (fieldToCheck.contains(neightborNode.getUp()) && fieldToCheck.contains(currentNode.getUp()))) {
                currentNode = graphs.get(num).getNodes().get(currentNode.getUp());
                neightborNode = graphs.get(num).getNodes().get(neightborNode.getUp());
                if (neightborNode.getData() == neightborNodeData || currentNode.getData() == currentNodeData) {
                    break;
                }
            }
        } else if (!fieldToCheck.contains(currentNode.getLeft())) {
            neightborNode = graphs.get(num).getNodes().get(currentNode.getRight());
            neightborNodeData = neightborNode.getData();
            while ((neightborNode.getUp() != -10 && currentNode.getUp() != -10) &&
                    (fieldToCheck.contains(neightborNode.getUp()) && fieldToCheck.contains(currentNode.getUp()))) {
                currentNode = graphs.get(num).getNodes().get(currentNode.getUp());
                neightborNode = graphs.get(num).getNodes().get(neightborNode.getUp());
                if (neightborNode.getData() == neightborNodeData || currentNode.getData() == currentNodeData) {
                    break;
                }
            }
        } else if (!fieldToCheck.contains(currentNode.getUp())) {
            neightborNode = graphs.get(num).getNodes().get(currentNode.getDown());
            neightborNodeData = neightborNode.getData();
            while (neightborNode.getRight() != -10 && currentNode.getRight() != -10 &&
                    (fieldToCheck.contains(neightborNode.getRight()) && fieldToCheck.contains(currentNode.getRight()))) {
                currentNode = graphs.get(num).getNodes().get(currentNode.getRight());
                neightborNode = graphs.get(num).getNodes().get(neightborNode.getRight());
                if (neightborNode.getData() == neightborNodeData || currentNode.getData() == currentNodeData) {
                    break;
                }
            }
        } else if (!fieldToCheck.contains(currentNode.getDown())) {
            neightborNode = graphs.get(num).getNodes().get(currentNode.getUp());
            neightborNodeData = neightborNode.getData();
            while (neightborNode.getRight() != -10 && currentNode.getRight() != -10 &&
                    (fieldToCheck.contains(neightborNode.getRight()) && fieldToCheck.contains(currentNode.getRight()))) {
                currentNode = graphs.get(num).getNodes().get(currentNode.getRight());
                neightborNode = graphs.get(num).getNodes().get(neightborNode.getRight());
                if (neightborNode.getData() == neightborNodeData || currentNode.getData() == currentNodeData) {
                    break;
                }
            }
        }
        if (currentNode.getData() == currentNodeData && neightborNode.getData() == neightborNodeData)
            return true;
        return false;
    }
    //TODO: minimize
    private Set<Set<Integer>> rectanglesIn4VariablesCase(int num) {
        Set<Integer> covered = new HashSet<>();
        for (var node : graphs.get(num).getCovered().keySet()) {
            covered.add(graphs.get(num).getCovered().get(node));
        }
        Set<Set<Integer>> rect8 = new HashSet<>();
        for (var node : graphs.get(num).getNodes().values()) {
            if (!covered.contains(node.getData())) {
                Set<Set<Integer>> copy = findrect8(node, num);
                if (copy != null)
                    rect8.addAll(copy);
            }
        }

        Set<Set<Integer>> squares = new HashSet<>();
        if(rect8.size()==3){
            Set<Integer> bigSquare = new HashSet<>();
            for(var rect: rect8){
                bigSquare.addAll(rect);
            }
            squares.add(bigSquare);
            return squares;
        }
        Set<Set<Integer>> rectangles = new HashSet<>();
        for (var node : graphs.get(num).getNodes().values()) {
            if (!covered.contains(node.getData())) {
                squares.addAll(findSquares(node, covered));
            }
        }
        Set<Integer> redundant = new HashSet<>(covered);
        for (var node : graphs.get(num).getNodes().values()) {
            if (!covered.contains(node.getData()))
                rectangles.addAll(findRectangles(node, covered, num));
        }
        if (rectangles.isEmpty() && covered.size() != redundant.size()) {
            covered = redundant;
        }
        int coveredSize = covered.size();
        Set<Set<Integer>> toDelete = new HashSet<>(squares);
        for (var square : squares) {
            Set<Set<Integer>> Squares = new HashSet<>(toDelete);
            Squares.remove(square);
            if (checkCovered(Squares, rectangles, coveredSize)) {
                toDelete.remove(square);
            }
        }
        squares = toDelete;
        for (var node : graphs.get(num).getNodes().values()) {
            if (!covered.contains(node.getData()))
                checkForSmallRectangles(node, covered, squares, rectangles);
        }
        Set<Set<Integer>> newSquares = new HashSet<>(squares);
        for (var square : squares) {
            for (var square2 : squares) {
                Set<Integer> Intersection = new HashSet<>(square);
                if (square.equals(square2))
                    continue;
                Intersection.addAll(square2);
                if (Intersection.size() == 8 && checkFor8(num, Intersection)) {
                    newSquares.remove(square);
                    newSquares.remove(square2);
                    newSquares.add(Intersection);
                } else if (newSquares.contains(Intersection)) {
                    newSquares.remove(Intersection);
                }
            }
        }
        if (squares.size() != newSquares.size())
            squares = newSquares;
        else if(!rect8.isEmpty()){
            squares=rect8;
        }
        for (var node : graphs.get(num).getNodes().values()) {
            if (!covered.contains(node.getData()) && node.getData() != -10) {
                Set<Integer> single = new HashSet<>();
                single.add(node.getData());
                squares.add(new HashSet<>(single));
            }
        }
        squares.addAll(rectangles);
        return squares;
    }
    //TODO: minimize
    private Set<Set<Integer>> findrect8(Node node, int num) {
        if ((node.getRight() == -10 && node.getLeft() == -10) || (node.getUp() == -10 && node.getDown() == -10)) {
            return null;
        }
        Set<Integer> rectangle = new HashSet<>();
        Node neightborNode = new Node(-10, new String(""));
        int amnt = 2;
        rectangle.add(node.getData());
        if (node.getRight() == -10) {
            neightborNode = graphs.get(num).getNodes().get(node.getLeft());
        } else if (node.getLeft() == -10) {
            neightborNode = graphs.get(num).getNodes().get(node.getRight());
        } else if (node.getUp() == -10) {
            neightborNode = graphs.get(num).getNodes().get(node.getDown());
        } else {
            neightborNode = graphs.get(num).getNodes().get(node.getUp());
        }
        rectangle.add(neightborNode.getData());
        if (node.getRight() == -10 || node.getLeft() == -10) {
            while (node.getUp() != -10 || neightborNode.getUp() != -10) {
                neightborNode = graphs.get(num).getNodes().get(neightborNode.getUp());
                node = graphs.get(num).getNodes().get(node.getUp());
                if (node == null || neightborNode == null)
                    break;
                if (!rectangle.contains(node.getData())) {
                    rectangle.add(node.getData());
                    rectangle.add(neightborNode.getData());
                    amnt++;
                    amnt++;
                } else break;
            }
        } else {
            while (node.getRight() != -10 || neightborNode.getRight() != -10) {
                neightborNode = graphs.get(num).getNodes().get(neightborNode.getRight());
                node = graphs.get(num).getNodes().get(node.getRight());
                if (node == null || neightborNode == null)
                    break;
                if (!rectangle.contains(node.getData())) {
                    rectangle.add(node.getData());
                    rectangle.add(neightborNode.getData());
                    amnt++;
                    amnt++;
                } else break;
            }
        }
        if (amnt == 8) {
            Set<Set<Integer>> squares = new HashSet<>();
            squares.add(rectangle);
            return squares;
        }
        return null;
    }

    private int[] differenceGreyCode(int[] previouseDigits, int[] digits) {
        if (previouseDigits == null) {
            return digits;
        }
        for (int i = 0; i < previouseDigits.length; i++) {
            if (previouseDigits[i] != digits[i]) {
                digits[i] = -1;
            }
        }
        return digits;
    }

    private List<String> collectGreysCode(List<String> rowsGreysCode, List<String> columnsGreysCode, Set<Integer> rectangle,
                                          LinkedHashMap<String, Character> statements) {
        int[] previouseDigitsRow = null;
        int[] previouseDigitsColumn = null;
        Pair<int[], int[]> grysCode = new Pair<>(previouseDigitsRow, previouseDigitsColumn);
        for (var node : rectangle) {
            grysCode = collectGreysCodeNode(grysCode.getKey(), grysCode.getValue(), node, rowsGreysCode, columnsGreysCode);
        }

        return unchangeableCode(grysCode.getValue(), grysCode.getKey(), statements);
    }

    private Pair<int[], int[]> collectGreysCodeNode(int[] previouseDigitsRow1,
                                                    int[] previouseDigitsColumn1, int node, List<String> rowsGreysCode1, List<String> columnsGreysCode1) {
        int row = node % 10;
        int column = node / 10;
        String rowLine = rowsGreysCode1.get(row);
        String columnLine = columnsGreysCode1.get(column);
        int[] digitsRow = new int[rowLine.length()];
        int[] digitsColumn = new int[columnLine.length()];

        for (int i = 0; i < rowLine.length(); i++)
            digitsRow[i] = Character.getNumericValue(rowLine.charAt(i));
        for (int i = 0; i < columnLine.length(); i++)
            digitsColumn[i] = Character.getNumericValue(columnLine.charAt(i));
        previouseDigitsRow1 = differenceGreyCode(previouseDigitsRow1, digitsRow);
        previouseDigitsColumn1 = differenceGreyCode(previouseDigitsColumn1, digitsColumn);
        return new Pair<>(previouseDigitsRow1, previouseDigitsColumn1);
    }

    private List<String> unchangeableCode(int[] previouseDigitsRow, int[] previouseDigitsColumn,
                                          LinkedHashMap<String, Character> statements) {
        List<String> result = new ArrayList<>();
        List<String> keys = new ArrayList<>(statements.keySet());

        for (int i = 0; i < previouseDigitsRow.length; i++) {
            if (previouseDigitsRow[i] != -1) {
                StringBuilder builder = new StringBuilder();
                if (previouseDigitsRow[i] == 0) {
                    builder.append('!');
                }
                builder.append(keys.get(i));
                result.add(builder.toString());
            }
        }
        for (int i = 0; i < previouseDigitsColumn.length; i++) {
            if (previouseDigitsColumn[i] != -1) {
                StringBuilder builder = new StringBuilder();
                if (previouseDigitsColumn[i] == 0) {
                    builder.append('!');
                }
                builder.append(keys.get(i + previouseDigitsRow.length));
                result.add(builder.toString());
            }
        }
        return result;
    }

    private List<String> collectGreysCode(List<String> rowsGreysCode, List<String> columnsGreysCode, int[] rectangle, LinkedHashMap<String, Character> statements) {
        int[] previouseDigitsRow = null;
        int[] previouseDigitsColumn = null;
        for (int i = rectangle[0]; i <= rectangle[1] % 10; i++) {
            int[] digits = new int[rowsGreysCode.getFirst().length()];
            for (int j = 0; j < rowsGreysCode.get(i).length(); j++)
                digits[j] = Character.getNumericValue(rowsGreysCode.get(i).charAt(j));
            previouseDigitsRow = differenceGreyCode(previouseDigitsRow, digits);
        }
        for (int i = rectangle[0]; i <= rectangle[1] / 10; i++) {
            int[] digits = new int[columnsGreysCode.getFirst().length()];
            for (int j = 0; j < columnsGreysCode.get(i).length(); j++)
                digits[j] = Character.getNumericValue(columnsGreysCode.get(i).charAt(j));
            previouseDigitsColumn = differenceGreyCode(previouseDigitsColumn, digits);
        }
        return unchangeableCode(previouseDigitsRow, previouseDigitsColumn, statements);
    }

    private void print(List<List<String>> minimized, int type) {
        StringBuilder result = new StringBuilder();
        for (List<String> row : minimized) {
            result.append("(");
            for (int i = 0; i < row.size() - 1; i++) {
                result.append(row.get(i));
                if (type == 1) {
                    result.append("&");
                } else {
                    result.append("|");
                }
            }
            result.append(row.getLast());
            result.append(")");
            if (type == 1) {
                result.append("|");
            } else {
                result.append("&");
            }
        }
        System.out.println(result);
    }

    private void printTable(List<String> rowLabels, List<String> columnLabels) {
        System.out.print("      ");
        for (String col : columnLabels) {
            System.out.print(String.format("%-4s", col));
        }
        System.out.println();
        for (int row = 0; row < rowLabels.size(); row++) {
            System.out.print(rowLabels.get(row) + "   ");
            for (int col = 0; col < columnLabels.size(); col++) {
                System.out.print(String.format("%-4d", map.get(row).get(col)));
            }
            System.out.println();
        }
    }

    private Set<String> findCommonInCycle(List<Integer> cycles) {
        Set<String> previouseGraph = new HashSet<>(graphs.get(cycles.getFirst()).greyCodes());
        Set<String> currentGraph = new HashSet<>();
        for (var cycle : cycles) {
            for (var node : graphs.get(cycle).greyCodes()) {
                if (previouseGraph.contains(node)
                ) {
                    currentGraph.add(node);
                }
            }
            previouseGraph = currentGraph;
            currentGraph = new HashSet<>();
        }
        return previouseGraph;
    }

    public static int[] findCommonBits(List<String> bitStrings) {
        if (bitStrings == null || bitStrings.isEmpty()) {
            return new int[0];
        }
        int length = bitStrings.get(0).length();
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            char referenceBit = bitStrings.get(0).charAt(i);
            boolean allMatch = true;
            for (int j = 1; j < bitStrings.size(); j++) {
                if (bitStrings.get(j).charAt(i) != referenceBit) {
                    allMatch = false;
                    break;
                }
            }
            result[i] = allMatch ? (referenceBit - '0') : -1;
        }

        return result;
    }

    private void adjastGraph(List<Integer> cycle, Set<String> nodes){
        Graph graph = new Graph(graphs.get(cycle.getFirst()));
        Set<String> commonNodes = new HashSet<>();
        Set<String> previouseNodes = new HashSet<>(graphs.get(cycle.getFirst()).getCovered().keySet());
        Set<String> currentNodes = graphs.get(cycle.get(1)).getCovered().keySet();
        for (var node : previouseNodes) {
            if (currentNodes.contains(node)) {
                commonNodes.add(node);
            }
        }
        previouseNodes = currentNodes;
        for (int i = 2; i < cycle.size(); i++) {
            currentNodes = new HashSet<>(graphs.get(cycle.get(i)).getCovered().keySet());
            for (var node : currentNodes) {
                if (!previouseNodes.contains(node)) {
                    commonNodes.remove(node);
                }
            }
        }

        graph.adjastNodes(nodes);
        graph.setCoveredStrings(commonNodes);
        graphs.add(graph);


    }

    private void findSquare1(HashMap<String, Integer> covered,List<Set<Set<Integer>>> commonRectangles,List<Integer> cycle,List<List<String>> graphCodes){
        List<String> graphsGreyCodes = new ArrayList<>();
        HashMap<String, Integer> covered1 = new HashMap<>();
        var coveredLastGraph = graphs.getLast().getCovered();
        for (var node : graphs.getLast().getNodes().values()) {
            if (!coveredLastGraph.containsValue(node.getData()) && node.getData() != -10) {
                Set<Integer> square1 = new HashSet<>();
                square1.add(node.getData());
                commonRectangles.getLast().add(square1);
                covered.put(graphs.getLast().getNodes().get(node.getData()).getGreyCode(), 0);
                covered1.put(graphs.getLast().getNodes().get(node.getData()).getGreyCode(), 0);
                graphsGreyCodes.add(graphs.getLast().getNodes().get(node.getData()).getGreyCode());
                graphCodes.add(new ArrayList<>(graphsGreyCodes));
                graphsGreyCodes.clear();
            }
        }
        for (var num : cycle) {
            graphs.get(num).setCovered(covered);
            for (var neightbor : graphs.get(num).getNeightbors()) {
                graphs.get(neightbor).setCovered(covered1);
            }
        }
    }

    private HashMap<Integer, Object> findRectangles(List<Integer> cycle) {
        HashMap<Integer, Object> result = new HashMap<>();
        if (cycle.size() > 2)
            cycle.removeLast();
        var nodes = findCommonInCycle(cycle);


        List<String> graphsGreyCodes = new ArrayList<>();
        for (var num : cycle)
            graphsGreyCodes.add(graphs.get(num).getGraphCode());
        int[] graphsBits = findCommonBits(graphsGreyCodes);

        adjastGraph(cycle, nodes);

        List<Set<Set<Integer>>> commonRectangles = new ArrayList<>();
        commonRectangles.add(rectanglesIn4VariablesCase(graphs.size() - 1));

        HashMap<String, Integer> covered = new HashMap<>();
        graphsGreyCodes = new ArrayList<>();
        List<List<String>> graphCodes = new ArrayList<>();
        for (var Rectangles : commonRectangles) {
            for (var rectangle : Rectangles) {
                for (var node : rectangle) {
                    graphsGreyCodes.add(graphs.getLast().getNodes().get(node).getGreyCode());
                    covered.put(graphs.getLast().getNodes().get(node).getGreyCode(), 0);
                }
                graphCodes.add(new ArrayList<>(graphsGreyCodes));
                graphsGreyCodes.clear();
            }
        }
        graphs.getLast().setCovered(covered);

        findSquare1(covered,commonRectangles,cycle,graphCodes);
        graphs.removeLast();

        result.put(1, commonRectangles);
        result.put(2, graphsBits);
        result.put(3, graphCodes);
        return result;
    }

    private List<String> getTerm(int[] graphsBits, List<String> graphsGreyCodes, TruthTable truthTable, List<String> rowsGreysCode, List<String> columnsGreysCode) {
        int rowLength = rowsGreysCode.getFirst().length() - 2;
        int columnsLength = columnsGreysCode.getFirst().length() - 2;

        int[] graphBits = findCommonBits(graphsGreyCodes);
        int[] unchangeableCodeColumns = new int[columnsGreysCode.getFirst().length()];
        int[] unchangeableCodeRows = new int[rowsGreysCode.getFirst().length()];
        for (int i = 0; i < rowLength; i++) {
            unchangeableCodeRows[i] = graphsBits[i];
        }
        for (int i = 0; i < 2; i++) {
            unchangeableCodeRows[i + rowLength] = graphBits[i];
        }
        for (int i = 0; i < columnsLength; i++) {
            unchangeableCodeColumns[i] = graphsBits[i + rowLength];
        }
        for (int i = 0; i < 2; i++) {
            unchangeableCodeColumns[i + columnsLength] = graphBits[i + 2];
        }
        return unchangeableCode(unchangeableCodeRows, unchangeableCodeColumns, truthTable.getStatements());
    }
    //TODO: minimize
    private List<Integer> identifyMax(List<Set<Set<Integer>>> graphRectangles, List<Set<Set<Integer>>> graphsRectangles, List<List<Set<Set<Integer>>>> rectInCycles,
                              List<List<Integer>> cycles) {

        int maxSize1 = 0;
        int max1 = 0;
        List<Integer> index1 = new ArrayList<>();
        List<Integer> index2 = new ArrayList<>();
        List<Integer> index3 = new ArrayList<>();
        int max2 = 0;
        int max3 = 0;
        int maxSize2 = 0;
        int maxSize3 = 0;
        for (var rectangle = 0; rectangle < graphsRectangles.size(); rectangle++) {
            if (graphsRectangles.get(rectangle) != null)
                for (var rect : graphsRectangles.get(rectangle)) {
                    max1 += rect.size() * 2;
                }
            if (max1 > maxSize1) {
                index1.clear();
                index1.add(rectangle);
                maxSize1 = max1;
            } else if (max1 == maxSize1) {
                index1.add(rectangle);
            }
            max1 = 0;
        }
        for (var rectangle = 0; rectangle < graphRectangles.size(); rectangle++) {
            if (graphRectangles.get(rectangle) != null)
                for (var rect : graphRectangles.get(rectangle)) {
                    max2 += rect.size();
                }
            if (max2 > maxSize2) {
                index2.clear();
                index2.add(rectangle);
                maxSize2 = max2;
            } else if (max2 == maxSize2) {
                index2.add(rectangle);
            }
            max2 = 0;
        }
        for (var rectangles = 0; rectangles < rectInCycles.size(); rectangles++) {
            for (int rectangle = 0; rectangle < rectInCycles.get(rectangles).size(); rectangle++) {
                if (rectInCycles.get(rectangles).get(rectangle) != null)
                    for (var rect : rectInCycles.get(rectangles).get(rectangle)) {
                        max3 += rect.size()*cycles.get(rectangles).size();
                    }
                if (max3 > maxSize3) {
                    index3.clear();
                    index3.add(rectangles);
                    maxSize3 = max3;
                } else if (max3 == maxSize3) {
                    index3.add(rectangle);
                }
                max3 = 0;
            }
            index3.add(-1);
        }
        List<Integer> result = new ArrayList<>();
        if (maxSize3 >= maxSize1&&maxSize1!=0&&maxSize3!=0) {
            result.add(1);
            result.addAll(index3);
            return result;
        }
        if (maxSize1 >= maxSize2&&maxSize1!=0&&maxSize2!=0) {
            result.add(2);
            result.addAll(index1);
            return result;
        }
        if (maxSize2 != 0) {
            result.add(3);
            result.addAll(index2);
            return result;
        }

        return null;
    }
    //TODO: minimize
    public void createKarnos(String expression, int type) {
        LogicExpressionParser parser = new LogicExpressionParser();
        TruthTable truthTable = new TruthTable();
        truthTable.createTruthTable(parser.parseOnBasicExpressions(expression, truthTable.getStatements()));
        int rows = truthTable.getStatements().size() / 2;
        List<String> rowsGreysCode = createGreysCode(rows);
        List<String> colomnsGreysCode = createGreysCode(truthTable.getStatements().size() - rows);
        fillMap(truthTable.getCombinations(), rowsGreysCode, colomnsGreysCode, type);
        List<List<String>> minimizedFormulas = new ArrayList<>();
        printTable(rowsGreysCode, colomnsGreysCode);
        if (truthTable.getStatements().size() < 4) {
            List<int[]> rectangles = rectanglesInSimpleCase();
            for (var rectangle : rectangles) {
                minimizedFormulas.add(collectGreysCode(rowsGreysCode, colomnsGreysCode, rectangle, truthTable.getStatements()));
            }
        } else if (truthTable.getStatements().size() == 4) {
            Set<Set<Integer>> rectangles = rectanglesIn4VariablesCase(0);
            for (var rectangle : rectangles) {
                minimizedFormulas.add(collectGreysCode(rowsGreysCode, colomnsGreysCode, rectangle, truthTable.getStatements()));
            }
        } else {
            while (true) {
                List<Graph> graphCopy = new ArrayList<>();
                for (Graph graph : graphs) {
                    graphCopy.add(new Graph(graph)); // Используем конструктор копирования
                }
                List<List<Integer>> cycles = new ArrayList<>(Graph.findAllCycles(graphs));
                cycles.sort((a, b) -> b.size() - a.size());
                List<HashMap<Integer, Object>> resultCycles = new ArrayList<>();
                List<List<Set<Set<Integer>>>> rectInCycles = new ArrayList<>(cycles.size());
                for (var cycle = 0; cycle < cycles.size(); cycle++) {
                    var res = findRectangles(cycles.get(cycle));
                    resultCycles.add(res);
                    List<Set<Set<Integer>>> commonRectangles = (List<Set<Set<Integer>>>) res.get(1);
                    rectInCycles.add(commonRectangles);
                    res.put(4,cycles.get(cycle));
                }

                Set<Integer> usedGraphs = new HashSet<>();

                List<HashMap<Integer, Object>> result = new ArrayList<>();
                for (var graph = 0; graph < graphs.size(); ++graph) {
                    List<Integer> neightbors = graphs.get(graph).getNeightbors();
                    for (var neightbor : neightbors) {
                        if (!usedGraphs.contains(neightbor)) {
                            List<List<Integer>> pair = new ArrayList<>();
                            pair.add(new ArrayList<>());
                            pair.getFirst().add(neightbor);
                            pair.getFirst().add(graph);
                            result.add(findRectangles(pair.getFirst()));
                            result.getLast().put(4, new ArrayList<>(Arrays.asList(graph, neightbor)));
                        } else
                            result.add(null);
                    }
                    usedGraphs.add(graph);
                }
                graphs = new ArrayList<>();
                for (Graph graph : graphCopy) {
                    graphs.add(new Graph(graph)); // Используем конструктор копирования
                }
                List<Set<Set<Integer>>> graphsRectangles = new ArrayList<>();
                for (var res = 0; res < result.size(); res++) {
                    if (result.get(res) != null)
                        graphsRectangles.addAll((List<Set<Set<Integer>>>) result.get(res).get(1));
                    else {
                        graphsRectangles.add(null);
                    }
                }
                List<Set<Set<Integer>>> graphRectangles = new ArrayList<>();
                List<List<String>> rowGreyCode = new ArrayList<>();
                List<List<String>> columnGreyCode = new ArrayList<>();
                int prevNum = -1;
                int numCol = 0;
                for (var graph = 0; graph < graphs.size(); ++graph) {
                    graphRectangles.add(rectanglesIn4VariablesCase(graph));
                    int numRow = graph % (rowsGreysCode.size() / 4);
                    if (numRow != prevNum + 1) {
                        numCol++;
                    }
                    prevNum = numRow;
                    rowGreyCode.add(rowsGreysCode.subList(numRow * 4, numRow * 4 + 4));
                    columnGreyCode.add(colomnsGreysCode.subList(numCol * 4, numCol * 4 + 4));
                }
                graphs = new ArrayList<>();
                for (Graph graph : graphCopy) {
                    graphs.add(new Graph(graph)); // Используем конструктор копирования
                }

                var max = identifyMax(graphRectangles, graphsRectangles, rectInCycles,cycles);
                if (max == null)
                    break;
                switch (max.getFirst()) {
                    case 1:
                        int cycleNum = 0;
                        for (int i = 1; i < max.size(); i++) {
                            if (max.get(i) == -1) {
                                cycleNum++;
                                continue;
                            }
                            for (var rect : rectInCycles.get(max.get(i)).getFirst()) {
                                //chsnge result
                                List<Integer> indexes = (ArrayList<Integer>) resultCycles.get(max.get(i)).get(4);
                                List<String> code = graphs.get(indexes.getLast()).codesRectangle(rect);
                                minimizedFormulas.add(getTerm((int[]) resultCycles.get(max.get(i)).get(2), code, truthTable, rowsGreysCode, colomnsGreysCode));
                                var prevCovered = graphs.get(indexes.getLast()).setGetCovered(rect);
                                for(int j=0;j<indexes.size()-1;j++)
                                graphs.get(indexes.get(j)).setCovered(prevCovered);
                                //neightbor
                            }
                        }
                        break;
                    case 2:
                        graphRectangles.clear();
                        for (int i = 1; i < max.size(); i++) {
                            if (graphsRectangles.get(max.get(i)) != null)
                                for (var rect : graphsRectangles.get(max.get(i))) {
                                    List<Integer> indexes = (ArrayList<Integer>) result.get(max.get(i)).get(4);
                                    List<String> code = graphs.get(indexes.getLast()).codesRectangle(rect);
                                    minimizedFormulas.add(getTerm((int[]) result.get(max.get(i)).get(2), code, truthTable, rowsGreysCode, colomnsGreysCode));
                                    var prevCovered = graphs.get(indexes.getLast()).setGetCovered(rect);
                                    graphs.get(indexes.getFirst()).setCovered(prevCovered);
                                    //neightbor
                                }
                        }
                        break;
                    case 3:
                        for (int i = 1; i < max.size(); i++) {
                            if (graphRectangles.get(max.get(i)) != null)
                                for (var rect : graphRectangles.get(max.get(i))) {
                                    minimizedFormulas.add(collectGreysCode(columnGreyCode.get(max.get(i)), rowGreyCode.get(max.get(i)),
                                            rect, truthTable.getStatements()));
                                    graphs.get(max.get(i)).setCovered(rect);
                                }
                        }


                }
            }
        }
        print(minimizedFormulas, type);
    }
}