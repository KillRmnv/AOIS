package Aois.Romanoff;

import java.util.ArrayList;
import java.util.List;

public class BinFixPointNum {
    private List<Integer> num = new ArrayList<>();

    List<Integer> Getter() {
        return num;
    }

    void Setter(List<Integer> anthrBin) {
        num.clear();
        num.addAll(anthrBin);
    }
}