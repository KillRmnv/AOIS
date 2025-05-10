package by.romanoff.aois;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinNumTest {
    @Test
    void testConvertToBinary() {
        BinNumber binNum = new BinNumber();
        BinConverter binConverter = new BinConverter();
        binNum.Setter(binConverter.ConvertToBin(194));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(0, 1, 1, 0, 0, 0, 0, 1, 0));
        assertEquals(expected, binNum.Getter(), "Ошибка при преобразовании в двоичный формат");
        binNum.Setter(binConverter.ConvertToBin(-112));
        expected = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 0, 0, 0, 0));
        assertEquals(expected, binNum.Getter(), "Ошибка при преобразовании в двоичный формат");
    }

    @Test
    void testAdding() {
        BinNumber num1 = new BinNumber();
        BinConverter binConverter = new BinConverter();
        DecimalConverter decimalConverter = new DecimalConverter();
        num1.Setter(binConverter.ConvertToBin(10));
        BinNumber num2 = new BinNumber();
        num2.Setter(binConverter.ConvertToBin(-20));
        List<Integer> sum = num1.Adding(num2.Getter());
        num1.Setter(sum);
        assertEquals(-10, decimalConverter.ConvertFrBinToDecimal(num1.Getter()));
    }

    @Test
    void testSubtracting() {
        BinNumber num1 = new BinNumber();
        BinConverter binConverter = new BinConverter();
        DecimalConverter decimalConverter = new DecimalConverter();
        num1.Setter(binConverter.ConvertToBin(-5));
        BinNumber num2 = new BinNumber();
        num2.Setter(binConverter.ConvertToBin(8));
        List<Integer> diff = num1.Subtracting(num2.Getter());
        num1.Setter(diff);
        assertEquals(-13, decimalConverter.ConvertFrBinToDecimal(num1.Getter()));
    }

    @Test
    void testMultiplication() {
        BinNumber num1 = new BinNumber();
        BinConverter binConverter = new BinConverter();
        DecimalConverter decimalConverter = new DecimalConverter();
        num1.Setter(binConverter.ConvertToBin(8));
        BinNumber num2 = new BinNumber();
        num2.Setter(binConverter.ConvertToBin(-20));
        List<Integer> product = num1.multiplication(num2.Getter());
        num1.Setter(product);
        assertEquals(-160, decimalConverter.ConvertFrBinToDecimal(num1.Getter()));
    }

    @Test
    void testDivision() {
        BinNumber num1 = new BinNumber();
        BinConverter binConverter = new BinConverter();
        DecimalConverter decimalConverter = new DecimalConverter();
        num1.Setter(binConverter.ConvertToBin(10));
        BinNumber num2 = new BinNumber();
        num2.Setter(binConverter.ConvertToBin(3));
        BinFixPointNum result = new BinFixPointNum();
        List<Integer> quotient = num1.division(num2.Getter());
        result.Setter(quotient);
        assertEquals(3.333, decimalConverter.ConvertFrFixPntToDecimal(result.Getter()), 0.001);
    }
}