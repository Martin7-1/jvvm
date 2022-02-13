package com.njuse.seecjvm.instructions.comparison;

/**
 * condition is true iff value1 != value2
 * @author Zyi
 */
public class IF_ICMPNE extends IF_ICMPCOND {

    @Override
    protected boolean condition(int v1, int v2) {
        return v1 != v2;
    }
}
