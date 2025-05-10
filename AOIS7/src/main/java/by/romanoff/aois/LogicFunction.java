package by.romanoff.aois;

import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class LogicFunction {
    @Setter
    private String expression;
    private TruthTable truthTable;
    public LogicFunction(String expression,int size) {
        this.expression = expression;
        this.truthTable = new TruthTable();
        LogicExpressionParser parser = new LogicExpressionParser();
        this.truthTable.setBasicLogicExpressions(parser.parseOnBasicExpressions(expression,truthTable.getStatements()));
        List<List<Integer>> combinations=new ArrayList<>();
        for (int i = 0; i < size; i++) {
            combinations.add(new ArrayList<>());
        }
        truthTable.setCombinations(combinations);
    }
    public void addWord(int[] word){
        int index=0;
        for(var symbol:word){
            truthTable.getCombinations().get(index).add(symbol);
            index++;
        }
    }
    public int[] createNewWord(int size){
        int[] result = new int[size];

        for(int i=0;i<size;i++){
            truthTable.createLine(i);
            result[i]=(truthTable.getCombinations().get(i).getLast());
        }
        return result;
    }
}
