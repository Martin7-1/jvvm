package com.njuse.seecjvm.instructions.base;

import java.nio.ByteBuffer;

public abstract class Index8Instruction extends Instruction {
    /**
     * type of index is unsigned char
     */
    public int index;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        index = (int) reader.get() & 0xFF;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (index & 0XFF);
    }
}
