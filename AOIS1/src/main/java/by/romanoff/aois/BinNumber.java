package by.romanoff.aois;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class BinNumber {
    private boolean checkForZeros(List<Integer> result) {
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) == 1)
                return false;
        }
        return true;
    }

    private List<Integer> num = new ArrayList<>();

    public List<Integer> convertToOneComplmnt() {
        BinNumber binNumber = new BinNumber();
        if (num.get(0) == 0) {
            binNumber.num.addAll(num);
            return binNumber.num;
        }
        binNumber.num.add(1);
        for (int i = 1; i < num.size(); i++) {
            if (num.get(i) == 0)
                binNumber.num.add(1);
            else
                binNumber.num.add(0);
        }
        return binNumber.num;
    }

    public List<Integer> convertToTwoComplmnt() {
        BinNumber result = new BinNumber();
        if (num.get(0) == 0) {
            result.num.addAll(num);
            return result.num;
        }
        result.Setter(convertToOneComplmnt());
        List<Integer> oneOnEnd = new ArrayList<>();
        for (int i = 0; i < result.num.size() - 1; i++) {
            oneOnEnd.add(0);
        }
        oneOnEnd.add(1);
        result.Setter(result.AddingOperation(oneOnEnd));
        return result.num;
    }

    public List<Integer> Subtracting(List<Integer> anthrBin) {
        if (num.size() > anthrBin.size()) {
            int diff = num.size() - anthrBin.size();
            for (int i = 0; i < diff; i++) {
                anthrBin.add(1, 0);
            }
        } else if (num.size() < anthrBin.size()) {
            int diff = anthrBin.size() - num.size();
            for (int i = 0; i < diff; i++) {
                num.add(1, 0);
            }
        }
        BinNumber binNumber = new BinNumber();
        boolean adding = false;
        if (anthrBin.get(0) == 0 && num.get(0) == 1) {
            num.set(0, 0);
            adding = true;
        } else {
            if (anthrBin.get(0) == 0) {
                anthrBin.set(0, 1);
            } else {
                anthrBin.set(0, 0);
            }
        }
        binNumber.Setter(anthrBin);

        binNumber.Setter(AddingOperation(binNumber.num));
        int amnt = Math.max(anthrBin.size(), num.size());
        for (int i = 0; i < binNumber.num.size() - amnt; i++) {
            binNumber.num.remove(0);
        }
        if (adding)
            binNumber.num.set(0, 1);
        return binNumber.num;
    }

    private List<Integer> AddingOperation(List<Integer> anthrBin) {
        List<Integer> result = new ArrayList<Integer>();
        int remain = 0;
        BinNumber binNumber = new BinNumber();
        binNumber.Setter(anthrBin);
        boolean OneOnEnd = true;
        for (int i = 0; i < anthrBin.size() - 1; i++) {
            if (anthrBin.get(i) == 1) {
                OneOnEnd = false;
                break;
            }
        }
        if (anthrBin.get(anthrBin.size() - 1) == 0) {
            OneOnEnd = false;
        }
        if (!OneOnEnd) {
            binNumber.Setter(binNumber.convertToTwoComplmnt());
            int size = num.size();
            num = convertToTwoComplmnt();
            for (int i = 0; i < num.size() - size; i++) {
                num.remove(0);
            }
        }
        for (int i = num.size() - 1; i > -1; i--) {
            int intermediateResult = num.get(i) + binNumber.num.get(i) + remain;
            if (intermediateResult > 1) {
                result.add(0, (intermediateResult - 2));
                remain = 1;
            } else {
                result.add(0, intermediateResult);
                remain = 0;
            }
        }
        if (remain == 1)
            result.add(0, 1);
        //System.out.println("Дополнительный код:" + result);
        return result;
    }

    public List<Integer> Adding(List<Integer> anthrBin) {

        num.add(1, 0);
        anthrBin.add(1, 0);

        if (num.size() > anthrBin.size()) {
            int diff = num.size() - anthrBin.size();
            for (int i = 0; i < diff; i++) {
                anthrBin.add(1, 0);
            }
        } else if (num.size() < anthrBin.size()) {
            int diff = anthrBin.size() - num.size();
            for (int i = 0; i < diff; i++) {
                num.add(1, 0);
            }
        }
        int numSize=num.size();
        int binSize=anthrBin.size();
        if (anthrBin.get(0) == 1) {
            anthrBin.set(0, 0);
            num = Subtracting(anthrBin);
            while(num.size() > numSize) {
                num.removeFirst();
                anthrBin.removeFirst();
            }
            return convertToTwoComplmnt();
        }
        num = AddingOperation(anthrBin);
            while(num.size() > numSize) {
                num.removeFirst();
                anthrBin.removeFirst();
            }
        return convertToTwoComplmnt();
    }

    public List<Integer> Getter() {
        return num;
    }

    public void Setter(List<Integer> anthrBin) {
        num.clear();
        num.addAll(anthrBin);
    }

    public List<Integer> multiplication(List<Integer> anthrBin) {
        int IntBin = 0;
        for (int i = 0; i < num.size(); i++) {
            IntBin += num.get(i) * pow(10, num.size() - 1 - i);
        }
        int resultINT = 0;
        for (int i = anthrBin.size() - 1; i > 0; i--) {
            resultINT += anthrBin.get(i) * IntBin * pow(10, anthrBin.size() - 1 - i);
        }
        String numStr = String.valueOf(resultINT);
        List<Integer> result = new ArrayList<>();
        for (String num : numStr.split("")) {
            result.add(Integer.parseInt(num));
        }
        IntBin = 0;
        for (int i = result.size() - 1; i > -1; i--) {
            int intermediateResult = result.get(i) + IntBin;
            if (intermediateResult > 1) {
                result.set(i, intermediateResult - 2);
                IntBin = 1;
            } else {
                if (IntBin == 1)
                    result.set(i, 1);
                IntBin = 0;
            }
        }
        if (IntBin == 1)
            result.add(0, 1);
        if ((num.get(0) == 1 && anthrBin.get(0) == 1) || (num.get(0) == 0 && anthrBin.get(0) == 0)) {
            result.add(0, 0);
        } else
            result.add(0, 1);

        return result;
    }

    public List<Integer> division(List<Integer> anthrBin) {
        List<Integer> substracted = new ArrayList<>();
        substracted.addAll(num);
        List<Integer> remainer = new ArrayList<>();
        List<Integer> IntegerDigits = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        temp.addAll(anthrBin);
        int currPos = 0;
        boolean sign, remainCounting = false;

        sign = (num.get(0) == anthrBin.get(0));

        num.clear();
        substracted.remove(0);
        anthrBin.set(0, 0);
        for (int i = 0; i < anthrBin.size() - 1 && i < substracted.size(); i++)
            num.add(substracted.get(i));
        currPos += anthrBin.size() - 2;

        performDivision(anthrBin, substracted, IntegerDigits, remainer, temp, currPos, remainCounting);

        processRemainder(IntegerDigits, remainer, sign);

        return num;
    }

    private void performDivision(List<Integer> anthrBin, List<Integer> substracted, List<Integer> IntegerDigits, List<Integer> remainer, List<Integer> temp, int currPos, boolean remainCounting) {
        List<Integer> result = new ArrayList<>();
        while (remainer.size() < 16) {
            num.add(0, 0);

            if (remainCounting)
                num.add(0);
            result.clear();
            if (num.size() >= anthrBin.size() && currPos < substracted.size()) {
                if (currPos + 1 >= substracted.size() && checkForZeros(num)) {
                    num.clear();
                    IntegerDigits.add(0);
                    break;
                }
                result.addAll(Subtracting(anthrBin));
                anthrBin.clear();
                anthrBin.addAll(temp);
                if (result.get(0) == 1) {
                    IntegerDigits.add(0);
                    result.clear();

                    currPos++;
                    if (currPos < substracted.size())
                        num.add(substracted.get(currPos));
                    else if (checkForZeros(num)) {
                        num.clear();
                        IntegerDigits.add(0);
                        break;
                    }
                } else {
                    IntegerDigits.add(1);
                    num.clear();
                    for (; !result.isEmpty() && result.get(0) == 0; )
                        result.remove(0);
                    result.add(0, 0);
                    num.addAll(result);

                    currPos++;
                    if (currPos < substracted.size())
                        num.add(substracted.get(currPos));
                    else if (checkForZeros(num)) {
                        num.clear();
                        break;
                    }
                    if (checkForZeros(num)) {
                        num.clear();
                        IntegerDigits.add(0);
                        break;
                    }
                }
            } else if (currPos < substracted.size() && num.size() < anthrBin.size()) {
                IntegerDigits.add(0);
                currPos++;
                num.add(substracted.get(currPos));
            } else if (currPos >= substracted.size() && num.size() < anthrBin.size()) {
                if (remainCounting)
                    remainer.add(0);
                else
                    remainCounting = true;
            } else if (currPos >= substracted.size() && num.size() >= anthrBin.size() && remainCounting) {
                result.addAll(Subtracting(anthrBin));
                anthrBin.clear();
                anthrBin.addAll(temp);
                if (result.get(0) == 1) {
                    remainer.add(0);
                    result.clear();
                } else {
                    remainer.add(1);
                    num.clear();
                    num.addAll(result);
                    result.clear();
                }
            } else
                remainCounting = true;
            num.remove(0);
        }
    }

    private void processRemainder(List<Integer> IntegerDigits, List<Integer> remainer, boolean sign) {
        for (int i = IntegerDigits.size(); i < 15; i++) {
            IntegerDigits.add(0, 0);
        }
        for (int i = remainer.size(); i < 16; i++) {
            remainer.add(0);
        }
        num.clear();
        num.addAll(remainer);
        num.addAll(0, IntegerDigits);
        if (sign)
            num.add(0, 0);
        else
            num.add(0, 1);
    }}