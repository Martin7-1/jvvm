package com.njuse.seecjvm.instructions.load;

import com.njuse.seecjvm.runtime.StackFrame;

/**
 * load long from local variable
 * index represent n
 * @author Zyi
 */
public class ILOAD_N extends LOAD_N {

    public ILOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    /**
     * 这里index代表指令中的N
     * 其中成员index是这条指令的参数，已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        long value = frame.getLocalVars().getLong(index);
        frame.getOperandStack().pushLong(value);
    }
}
