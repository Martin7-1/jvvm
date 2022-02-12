package com.njuse.seecjvm.instructions.base;

import java.nio.ByteBuffer;

public abstract class NoOperandsInstruction extends Instruction {
    @Override
    public void fetchOperands(ByteBuffer reader) {
        //do nothing
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
