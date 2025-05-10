package by.romanoff.aois;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Output {
    public static List<String> constituentsList(Map<Integer, String> SDNF, int type) {
        List<String> glued = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(SDNF.keySet());
        keys.sort(Integer::compareTo);
        StringBuilder minimizedFormula = new StringBuilder();
        String operation = new String();
        if (type == 1) {
            operation = "&";
        } else {
            operation = "|";
        }
        minimizedFormula.append("(");
        minimizedFormula.append(SDNF.get(keys.get(0)));
        if (keys.getFirst() / 10 != keys.get(1) / 10) {
            minimizedFormula = new StringBuilder();
            minimizedFormula.append(SDNF.get(keys.getFirst()));
            glued.add(minimizedFormula.toString());
            minimizedFormula = new StringBuilder();
        } else {
            minimizedFormula.append(operation);
        }
        for (int i = 1; i < keys.size() - 1; i++) {
            if (keys.get(i + 1) / 10 == keys.get(i) / 10 && !(keys.get(i - 1) / 10 == keys.get(i) / 10)) {
                minimizedFormula.append("(");
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(operation);
            } else if (keys.get(i + 1) / 10 == keys.get(i) / 10 && keys.get(i - 1) / 10 == keys.get(i) / 10) {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(operation);
            } else if (keys.get(i + 1) / 10 != keys.get(i) / 10 && keys.get(i - 1) / 10 == keys.get(i) / 10) {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                minimizedFormula.append(")");
                glued.add(minimizedFormula.toString());
                minimizedFormula = new StringBuilder();
            } else {
                minimizedFormula.append(SDNF.get(keys.get(i)));
                glued.add(minimizedFormula.toString());
                minimizedFormula = new StringBuilder();
            }
        }
        if (keys.getLast() / 10 != keys.get(keys.size() - 2) / 10) {
            minimizedFormula.append("(");
        }
        minimizedFormula.append(SDNF.get(keys.getLast()));

        minimizedFormula.append(")");
        glued.add(minimizedFormula.toString());
        return glued;
    }

   public static void printResult(Map<Integer, String> SDNF, int type) {
        var glued = constituentsList(SDNF, type);
        print(glued, type);
    }

    static void print(List<String> glued, int type) {
        for (var k=0; k<glued.size()-1;k++) {
            System.out.print(glued.get(k));
            if (type == 1)
                System.out.print("|");
            else
                System.out.print("&");
        }System.out.print(glued.getLast());
        System.out.println();
    }
}
