package com.njuse.seecjvm.instructions.comparison;

/**
 * condition is true iff value == 0
 * @author Zyi
 */
public class IFEQ extends IFCOND {

    @Override
    public boolean condition(int value) {
        return value == 0;
    }
}
