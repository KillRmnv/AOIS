package by.romanoff.aois;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinFloatPointTest {
    @Test
    public void testConvert() {
        BinConverter binConverter = new BinConverter();
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(0,1,0,0,0,0,0,1,0,1,1,0,1,1,1,0,0,0,0,1,0,1,0,0,0,1,1,1,1,0,1,0));
        BinFloatPointNum num = new BinFloatPointNum();
        num.Setter(binConverter.ConvertToFloatBin(14.88));
        assertEquals(expected, num.Getter());
    }
    @Test
    void testAdding() {
        List<Integer> sum;
        BinConverter binConverter = new BinConverter();
        DecimalConverter decimalConverter = new DecimalConverter();
        BinFloatPointNum numFloat1 = new BinFloatPointNum();
        numFloat1.Setter(binConverter.ConvertToFloatBin(1.5));
        System.out.println(decimalConverter.ConvertFrFloatPntToDecimal(numFloat1.Getter()));
        BinFloatPointNum numFloat2 = new BinFloatPointNum();
        numFloat2.Setter(binConverter.ConvertToFloatBin(16.25));
        System.out.println(decimalConverter.ConvertFrFloatPntToDecimal(numFloat2.Getter()));
        System.out.println(numFloat1.Getter());
        System.out.println(numFloat2.Getter());
        sum = numFloat1.Adding(numFloat2.Getter());
        numFloat1.Setter(sum);
        assertEquals(17.75,decimalConverter.ConvertFrFloatPntToDecimal(numFloat1.Getter()),0.001);
    }
}
