package by.Romanoff.Aois;

import java.util.ArrayList;
import java.util.List;

public class BinFloatPointNum {
    private static class MagicNumbers {
        public static int Exponent(){
            return 8;
        }
        public static int Mantissa(){
            return 23;
        }
        public static int ExponentNumber(){
            return 127;
        }
    }
    private List<Integer> num = new ArrayList<>();

    private double extractExponent(List<Integer> binNum) {
        double exponent = 0;
        BinNumber exp = new BinNumber();
        List<Integer> exponentList = new ArrayList<>();
        for (int i = 1; i < MagicNumbers.Exponent()+1; i++) {
            exponentList.add(binNum.get(i));
        }
        exponentList.add(0, 0);
        exp.Setter(exponentList);
        DecimalConverter converter = new DecimalConverter();
        exponent = converter.ConvertFrBinToDecimal(exp.Getter());
        return exponent - MagicNumbers.ExponentNumber();
    }

    private List<Integer> extractMantissa(List<Integer> binNum) {
        List<Integer> mantissa = new ArrayList<>();
        mantissa.add(0, 1);
        for (int i = 1+MagicNumbers.Exponent(); i < binNum.size()-1; i++) {
            mantissa.add(binNum.get(i));
        }
        return mantissa;
    }

    private List<Integer> addMantissas(List<Integer> mant1, List<Integer> mant2) {
        int carry = 0;
        List<Integer> result = new ArrayList<>();
        for (int i = mant1.size() - 1; i >= 0; i--) {
            int sum = mant1.get(i) + mant2.get(i) + carry;
            result.add(0, sum % 2);
            carry = sum / 2;
        }
        if (carry > 0) {
            result.add(0, 1);
        }
        return result;
    }

    public List<Integer> Adding(List<Integer> anthrBin) {
        double exp1 = extractExponent(num);
        double exp2 = extractExponent(anthrBin);

        List<Integer> mant1 = extractMantissa(num);
        List<Integer> mant2 = extractMantissa(anthrBin);

        while (exp1 > exp2) {
            mant2.add(0, 0);
            mant2.remove(mant2.size() - 1);
            exp2++;
        }
        while (exp2 > exp1) {
            mant1.add(0, 0);
            mant1.remove(mant2.size() - 1);
            exp1++;
        }

        mant1 = addMantissas(mant1, mant2);
        while (mant1.size() > MagicNumbers.Mantissa()) {
            mant1.remove(mant1.size() - 1);
            exp1++;
        }

        List<Integer> result = new ArrayList<>();
        BinNumber exp = new BinNumber();
        BinConverter converter = new BinConverter();
        exp.Setter(converter.ConvertToBin(exp1 + MagicNumbers.ExponentNumber()));
        result.addAll(mant1);
        result.addAll(0, exp.Getter());
        result.remove(9);
        result.add(0);
        return result;
    }

    List<Integer> Getter() {
        return num;
    }

    void Setter(List<Integer> anthrBin) {
        num.clear();
        num.addAll(anthrBin);
    }
}