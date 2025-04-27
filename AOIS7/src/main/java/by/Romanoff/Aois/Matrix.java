package by.Romanoff.Aois;

import lombok.Data;

import java.util.*;
@Data
public class Matrix {
    private int[][] matrix;
    private static class MatrixConstants {
        public static final int size = 16;
        private MatrixConstants() {}
    }
    public int size(){
        return MatrixConstants.size;
    }
    public Matrix(int[][] matrix){
        this.matrix = matrix;
    }
    public Matrix() {
        Random random = new Random();
        matrix = new int[MatrixConstants.size][MatrixConstants.size];
        for (int i = 0; i < MatrixConstants.size; i++) {
            for (int j = 0; j < MatrixConstants.size; j++) {
                matrix[i][j] = random.nextInt(2);
            }
        }
    }
    public void printMatrixFormatted() {
        for (int j = 0; j < MatrixConstants.size; j++) {
            for (int i = 0; i < MatrixConstants.size; i++) {
                if (i > 0) {
                    System.out.print(" ");
                }
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
    public int[] getWord(int num) {
        int index=0;
        int[] word=new int[MatrixConstants.size];
        for(int i=num;i<MatrixConstants.size;i++){
            word[index]=matrix[num][i];
            index++;
        }
        for(int i=0;i<num;i++){
            word[index]=matrix[num][i];
            index++;
        }
        return word;

    }
    public int[] getColumn(int num) {
        int index=0;
        int[]column=new int[MatrixConstants.size];
        for(int i=num;i<MatrixConstants.size;i++){
            column[index]=matrix[index][i];
            index++;
        }
        int size=MatrixConstants.size-index;
        for(int i=0;i<size;i++){
            column[index]=matrix[index][i];
            index++;
        }
        return column;

    }
    public void writeWord(int num,int[]word) {
        int index=0;
        for(int i=num;i<MatrixConstants.size;i++){
            matrix[num][i]=word[index];
            index++;
        }
        for(int i=0;i<num;i++){
            matrix[num][i]=word[index];
            index++;
        }
    }
    public void writeColumn(int num,int[] column) {
        int index=0;
        for(int i=num;i<MatrixConstants.size;i++){
           matrix[index][i]= column[index];
            index++;
        }
        int size=MatrixConstants.size-index;
        for(int i=0;i<size;i++){
            matrix[index][i]=column[index];
            index++;
        }
    }
    public Map<int[],Integer> findWords(List<Integer> bits3){
        Map<int[],Integer> result=new HashMap<>();
        for(int i=0;i<MatrixConstants.size;i++){
            boolean check=true;
            for(int j=i;j<i+3;j++){
                if(j>=MatrixConstants.size){
                    if(matrix[i][j-MatrixConstants.size]!=bits3.get(j-i)){
                        check=false;
                    }
                }else
                if(matrix[i][j]!=bits3.get(j-i)){
                   check=false;
                }
            }
            if(check){
                result.put(getWord(i),i);
            }
        }
        return result;
    }
    void arithmeticOperation(int num){
        BinConverter converter = new BinConverter();
        List<Integer> binNum=converter.ConvertToBin(num);
        while(binNum.size()>3){
            binNum.removeFirst();
        }
        var words = findWords(binNum);
        BinNumber num1 = new BinNumber();
        BinNumber num2 = new BinNumber();

        for (var word : words.keySet()) {
            System.out.println();
            System.out.println(Arrays.toString(word));
            List<Integer> partOfNum = new ArrayList<>();
            partOfNum.add(0);
            for (int i = 3; i < 7; i++) {
                partOfNum.add(word[i]);
            }
            num1.Setter(partOfNum);
            partOfNum.clear();
            partOfNum.add(0);
            for (int i = 7; i < 11; i++) {
                partOfNum.add(word[i]);
            }
            num2.Setter(partOfNum);

            partOfNum = num1.Adding(num2.Getter());
            int indexWord = 11;
            int key = words.get(word);
            partOfNum.removeFirst();

            for (var el : partOfNum) {
                word[indexWord] = el;
                indexWord++;
            }

            System.out.println(Arrays.toString(word));
            writeWord(key, word);
            System.out.println();
        }
    }
}