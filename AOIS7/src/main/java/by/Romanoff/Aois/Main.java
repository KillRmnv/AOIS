package by.Romanoff.Aois;

import java.util.*;

public class Main {
    private static Matrix matrix;
    private static Scanner scanner;

    public static void main(String[] args) {
        initialize();
        runMainLoop();
    }

    private static void initialize() {
        matrix = new Matrix();
        scanner = new Scanner(System.in);
    }

    private static void runMainLoop() {
        int choice = -1;
        while (true) {
            choice = getMenuChoice();
            processChoice(choice);
            if (choice == 0) {
                return;
            }
        }
    }

    private static int getMenuChoice() {
        int choice = -1;
        while (choice < 0 || choice > 9) {
            printMenu();
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Очистка буфера
                choice = -1;
            }
        }
        return choice;
    }

    private static void printMenu() {
        System.out.print("""
                Меню:
                1. Вывести матрицу
                2. Прочитать слово
                3. Прочитать столбец
                4. Записать столбец
                5. Записать слово
                6. Применить логические функции
                (!A&B)|(A&!B)
                (A&B)|(!A&!B)
                (!A&B)
                (A|!B)
                7.Арифметичекие операции над полями слов
                8.Поиск
                0.Завершить работу
                """);
    }

    private static void processChoice(int choice) {
        switch (choice) {
            case 1 -> printMatrix();
            case 2 -> readWord();
            case 3 -> readColumn();
            case 4 -> writeColumn();
            case 5 -> writeWord();
            case 6 -> applyLogicFunctions();
            case 7 -> performArithmeticOperations();
            case 8 -> performSearchOperations();
            case 0 -> {} // Завершение работы
        }
    }

    private static void printMatrix() {
        matrix.printMatrixFormatted();
    }

    private static void readWord() {
        System.out.println("Номер слова:");
        try {
            System.out.println(Arrays.toString(matrix.getWord(scanner.nextInt())));
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            scanner.nextLine();
        }
    }

    private static void readColumn() {
        System.out.println("Номер столбца:");
        try {
            System.out.println(Arrays.toString(matrix.getColumn(scanner.nextInt())));
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            scanner.nextLine();
        }
    }

    private static void writeColumn() {
        System.out.println("Номер столбца:");
        int num = getNumberInput();
        if (num == -1) return;

        int[] inputArray = getInputArray("столбец");
        if (inputArray != null) {
            matrix.writeColumn(num, inputArray);
        }
    }

    private static void writeWord() {
        System.out.println("Номер слова:");
        int num = getNumberInput();
        if (num == -1) return;

        int[] inputArray = getInputArray("слово");
        if (inputArray != null) {
            matrix.writeWord(num, inputArray);
        }
    }

    private static int getNumberInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            scanner.nextLine();
            return -1;
        }
    }

    private static int[] getInputArray(String arrayType) {
        System.out.println("Введите " + matrix.size() + " чисел для массива (" + arrayType + "):");
        int[] inputArray = new int[matrix.size()];
        try {
            for (int i = 0; i < matrix.size(); i++) {
                inputArray[i] = scanner.nextInt();
            }
            return inputArray;
        } catch (InputMismatchException e) {
            System.out.println("Ошибка: введено не число!");
            scanner.nextLine();
            return null;
        }
    }

    private static void applyLogicFunctions() {
        LogicFunction f6 = new LogicFunction("(!A&B)|(A&!B)",matrix.size());
        LogicFunction f9 = new LogicFunction("(A&B)|(!A&!B)",matrix.size());
        LogicFunction f4 = new LogicFunction("(!A&B)",matrix.size());
        LogicFunction f11 = new LogicFunction("(A|!B)",matrix.size());

        int[] wordNumbers = getTwoWordNumbers();
        if (wordNumbers == null) return;

        f6.addWord(matrix.getWord(wordNumbers[0]));
        f6.addWord(matrix.getWord(wordNumbers[1]));
        f9.addWord(matrix.getWord(wordNumbers[0]));
        f9.addWord(matrix.getWord(wordNumbers[1]));
        f4.addWord(matrix.getWord(wordNumbers[0]));
        f4.addWord(matrix.getWord(wordNumbers[1]));
        f11.addWord(matrix.getWord(wordNumbers[0]));
        f11.addWord(matrix.getWord(wordNumbers[1]));

        System.out.println("Номер слова для размещения:");
        int targetWord = getNumberInput();
        if (targetWord == -1) return;
        System.out.println("Слово 1: "+ Arrays.toString(matrix.getWord((wordNumbers[0]))));
        System.out.println("Слово 2: "+ Arrays.toString(matrix.getWord((wordNumbers[1]))));
        applyAndShowResult(f6, targetWord, "f6:");
        applyAndShowResult(f9, targetWord, "f9:");
        applyAndShowResult(f4, targetWord, "f4:");
        applyAndShowResult(f11, targetWord, "f11:");
    }

    private static int[] getTwoWordNumbers() {
        int[] numbers = new int[2];
        for (int i = 0; i < 2; i++) {
            System.out.println("Номер слова " + (i + 1) + ":");
            numbers[i] = getNumberInput();
            if (numbers[i] == -1) {
                return null;
            }
        }
        return numbers;
    }

    private static void applyAndShowResult(LogicFunction function, int wordNumber, String functionName) {
        System.out.println(functionName);
        var newWord=function.createNewWord(matrix.size());
        System.out.println(Arrays.toString(newWord));
        matrix.writeWord(wordNumber, newWord);

    }

    private static void performArithmeticOperations() {
        System.out.println("Число от 0 до 7:");
        int num = getNumberInput();
        if (num == -1) return;
        matrix.arithmeticOperation(num);
        matrix.printMatrixFormatted();
    }

    private static void performSearchOperations() {
        GLSearchOperations operations = new GLSearchOperations(matrix);
        for(int i= matrix.size()-1;i>=0;i--){
            System.out.println(Arrays.toString(matrix.getWord(i)));
        }
        System.out.println();
        operations.sort();
        for(int i= matrix.size()-1;i>=0;i--){
            System.out.println(Arrays.toString(matrix.getWord(i)));
        }
    }
}