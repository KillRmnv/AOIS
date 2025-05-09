package by.romanoff.aois;

import java.util.ArrayList;
import java.util.List;

public class BinConverter {
    public List<Integer> ConvertToBin(double number) {
        List<Integer> num = new ArrayList<>();
        number = (int) number;
        if (number == 0) {
            for (int i = 0; i < 9; i++) {
                num.add(0);
            }
            return num;
        }
        boolean sign = number < 0 ? false : true;
        if (!sign)
            number *= -1;
        for (; number != 1; ) {
            num.add(0, (int) (number % 2));
            number /= 2;
            number = (int) number;
        }
        num.add(0, 1);
        if (sign)
            num.add(0, 0);
        else
            num.add(0, 1);
        return num;
    }

    public List<Integer> ConvertToFixBin(double number) {
        List<Integer> num = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        num.addAll(ConvertToBin(number));
        int amntOfDigits = num.size() - 1;
        for (int i = amntOfDigits; i < 14; i++) {
            num.add(1, 0);
        }
        result.addAll(num);
        num.clear();
        double intPart = (int) number;
        number = number - intPart;
        for (int i = 0; i < 16; i++) {
            number *= 2;
            if (number > 1) {
                number -= 1;
                num.add(1);
            } else if (number < 1) {
                num.add(0);
            } else {
                num.add(1);
                for (int j = i; j < 16; j++) {
                    num.add(0);
                }
                break;
            }
        }
        num.addAll(0, result);
        if (intPart > 0) {
            num.add(0, 0);
        } else {
            num.add(0, 1);
        }
        return num;
    }

    public List<Integer> ConvertToFloatBin(double number) {
        List<Integer> IntPart = new ArrayList<>();
        IntPart = ConvertToBin(number);
        List<Integer> num = new ArrayList<>();
        num.addAll(IntPart);
        num.remove(0);
        IntPart.clear();
        IntPart.addAll(num);
        int amntOfDigits = num.size();
        num.clear();
        double intPart = (int) number;
        number = number - intPart;
        for (int i = 0; i < 23 - amntOfDigits; i++) {
            number *= 2;
            if (number > 1) {
                number -= 1;
                num.add(1);
            } else if (number < 1) {
                num.add(0);
            } else {
                num.add(1);
                for (int j = i + 1; j < 23- amntOfDigits; j++) {
                    num.add(0);
                }
                break;
            }
        }
        num.addAll(0, IntPart);
        if (amntOfDigits > 1) {
            IntPart = new ArrayList<>();
            IntPart = ConvertToBin(amntOfDigits + 126);
            num.addAll(0, IntPart);
            num.remove(0);
        } else {
            for (amntOfDigits = 1; amntOfDigits < 22 && num.get(amntOfDigits) == 0; amntOfDigits++) ;
            if(amntOfDigits==1)
                amntOfDigits=0;

            IntPart = ConvertToBin(127 - amntOfDigits);
            if(IntPart.size()==8)
                IntPart.add(1,0);
            num.addAll(0, IntPart);
            num.remove(0);
        }
        if (intPart > 0) {
            num.add(0, 0);
        } else {
            num.add(0, 1);
        }
        num.remove(9);
        number *= 2;
        if(number==2){
            num.add(0);
        }else
        if (number > 1) {
            num.add(1);
        } else if (number < 1) {
            num.add(0);
        }

        return num;
    }
}