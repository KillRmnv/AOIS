package Aois.Romanoff;

import java.util.ArrayList;
import java.util.List;

public class DecimalConverter {
    public double ConvertFrFixPntToDecimal(List<Integer> num) {
        double result = 0;
        for (int i = 1; i < num.size(); i++) {
            result += Math.pow(2, 15 - i) * num.get(i);
        }
        if (num.get(0) == 1) {
            result *= -1;
        }
        return result;
    }

    public double ConvertFrFloatPntToDecimal(List<Integer> num) {
        double result = 0, exp = 0;
        for (int i = 1; i < 9; i++) {
            exp += Math.pow(2, 8 - i) * num.get(i);
        }
        exp = exp - 127;
        result += Math.pow(2, exp) * 1;
        exp--;
        for (int i = 9; i < num.size(); i++) {
            result += Math.pow(2, exp) * num.get(i);
            exp--;
        }

        if (num.get(0) == 1) {
            result *= -1;
        }
        return result;
    }

    public double ConvertFrBinToDecimal(List<Integer> num) {
        double result = 0;
        for (int i = 1; i < num.size(); i++) {
            result += Math.pow(2, num.size() - i - 1) * num.get(i);
        }
        if (num.get(0) == 1) {
            result *= -1;
        }
        return result;
    }


}
