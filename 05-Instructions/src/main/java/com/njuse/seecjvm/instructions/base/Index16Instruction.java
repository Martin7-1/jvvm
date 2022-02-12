package com.njuse.seecjvm.instructions.base;

import java.nio.ByteBuffer;

public abstract class Index16Instruction extends Instruction {
    /**
     * type of index is unsigned short
     */
    public int index;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        index = (int) reader.getShort() & 0xFFFF;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (index & 0xFFFF);
    }
}
