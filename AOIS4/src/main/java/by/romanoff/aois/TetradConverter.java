package by.romanoff.aois;

import java.io.PrintStream;
import java.util.*;

public class TetradConverter {
    private BinNumber addingN;
    private BinNumber toZero;
    private TruthTable truthTable;
    private KarnosMapMethod karnosMapMethod;

    public TetradConverter() {
        BinConverter binConverter = new BinConverter();
        addingN = new BinNumber();
        addingN.Setter(binConverter.ConvertToBin(9));
        truthTable = new TruthTable();
        Map<String, Character> Statements=new LinkedHashMap<>();
        Statements.put("A", 'n');
        Statements.put("B", 'k');
        Statements.put("C", 'm');
        Statements.put("D", 's');
        Statements.put("Y0", 't');
        Statements.put("Y1", 't');
        Statements.put("Y2", 't');
        Statements.put("Y3", 't');
        karnosMapMethod = new KarnosMapMethod();
        truthTable.setStatements(Statements);
        truthTable.setCombinations(truthTable.allPossibleCombinations(4));
    }

    public List<List<Integer>> adding() {
        List<List<Integer>> combinations = truthTable.allPossibleCombinations(4);
        List<List<Integer>> result = new ArrayList<>();
        var adding = new ArrayList<>(addingN.Getter());
        boolean biggerThen9=false;
        BinConverter binConverter = new BinConverter();
        int num=0;
        for (List<Integer> combination : combinations) {
//            if(biggerThen9){
//                if(num>9){
//                    num=0;
//                }
//               var dontCare= binConverter.ConvertToBin(num);
//
//               while(dontCare.size()<5){
//                   dontCare.add(1,0);
//               }
//                dontCare.removeFirst();
//               trim(dontCare);
//               result.add(dontCare);
//                num++;
//                continue;
//            }
            combination.addFirst(0);
            List<Integer> addition = addingN.Adding(combination);
            addingN.Setter(adding);
            trim(addition);
            trim(combination);
//            if(addition.getFirst()==1&&addition.get(2)==1){
//                biggerThen9=true;
//                var dontCare= binConverter.ConvertToBin(num);
//                dontCare.removeFirst();
//                trim(dontCare);
//                result.add(dontCare);
//                num++;
//                continue;
//            }
            result.add(addition);
        }
        return result;
    }
    public void print(List<List<Integer>> tetrad) {
        int maxLength = 0;

        for(String expression : truthTable.getStatements().keySet()) {
            if (expression.length() > maxLength) {
                maxLength = expression.length();
            }
        }

        for(String statement : truthTable.getStatements().keySet()) {
            System.out.print(statement + " ");
        }

        System.out.println();
        int index=0;
        for(List<Integer> combination : truthTable.getCombinations()) {

            for(int i = 0; i <combination.size(); ++i) {
                int value = (Integer)combination.get(i);
                System.out.printf("%-" + (maxLength + 1) + "d", value);
            }
            for(int i = 0; i <tetrad.get(index).size(); ++i) {
                int value = (Integer)tetrad.get(index).get(i);
                System.out.printf("%-" + (maxLength + 1) + "d", value);
            }
            index++;
            System.out.println();
        }
        truthTable.getStatements().remove("Y0", 't');
        truthTable.getStatements().remove("Y1", 't');
        truthTable.getStatements().remove("Y2", 't');
        truthTable.getStatements().remove("Y3", 't');
    }
    public void minimizeTetrads(List<List<Integer>> tetrads){
        for(int index=0;index<tetrads.getFirst().size();++index) {
            for(int tetrad=0 ;tetrad< tetrads.size();++tetrad) {
                truthTable.getCombinations().get(tetrad).add(tetrads.get(tetrad).get(index));
            }
            karnosMapMethod=new KarnosMapMethod();
            karnosMapMethod.print(karnosMapMethod.KarnosMapMethod(truthTable,1),1);
            karnosMapMethod=new KarnosMapMethod();
            karnosMapMethod.print(karnosMapMethod.KarnosMapMethod(truthTable,0),0);
        }
    }

    private void trim(List<Integer> num){
        while(num.size()>4){
            num.removeFirst();
        }
    }
}
