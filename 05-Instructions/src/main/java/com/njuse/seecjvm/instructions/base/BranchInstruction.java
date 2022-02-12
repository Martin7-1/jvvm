package com.njuse.seecjvm.instructions.base;

import java.nio.ByteBuffer;

/**
 * @author Zyi
 */
public abstract class BranchInstruction extends Instruction {
    /**
     * type of offset is signed short
     */
    protected int offset;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        offset = reader.getShort();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " offset: " + offset;
    }
}
