package com.njuse.seecjvm.instructions.base;

import java.nio.ByteBuffer;

/**
 * 无操作数的指令
 * @author Zyi
 */
public abstract class NoOperandsInstruction extends Instruction {

    @Override
    public void fetchOperands(ByteBuffer reader) {
        // do nothing
        // this instruction do not need operand
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
