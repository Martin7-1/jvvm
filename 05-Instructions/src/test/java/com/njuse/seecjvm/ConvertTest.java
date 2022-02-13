package com.njuse.seecjvm;

import com.njuse.seecjvm.instructions.conversion.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    Convertable instr;

    /**
     * int convert to byte test
     */
    @Test
    public void I2BTest() {
        instr = new I2B();
        int value = 0x00000080;
        int actual = instr.convert(value);
        int expect = (byte) value;
        System.out.println("value = " + value);
        System.out.println("actual = " + actual);
        System.out.println("expect = " + expect);
        assertEquals(expect, actual);
    }

    @Test
    public void I2CTest() {
        instr = new I2C();
        int value = 0xFFFF0080;
        int actual = instr.convert(value);
        int expect = (char) value;
        System.out.println("value = " + value);
        System.out.println("actual = " + actual);
        System.out.println("expect = " + expect);
        assertEquals(expect, actual);
    }

    @Test
    public void I2DTest() {
        instr = new I2D();
        int value = 100;
        System.out.println("value = " + (double) value);
    }

    @Test
    public void I2FTest() {
        instr = new I2F();
        int value = 0x00000080;
        System.out.println("value = " + (float) value);
    }

    @Test
    public void I2LTest() {
        instr = new I2L();
        int value = 0x7FFFFFFF;
        System.out.println("value = " + (long) value);
    }

    @Test
    public void I2STest() {
        instr = new I2S();
        int value = 0xFFFF7000;
        int actual = instr.convert(value);
        int expect = (short) value;
        System.out.println("value = " + value);
        System.out.println("actual = " + actual);
        System.out.println("expect = " + expect);
        assertEquals(expect, actual);
    }
}
