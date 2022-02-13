package com.njuse.seecjvm.instructions.load;

import com.njuse.seecjvm.runtime.StackFrame;

/**
 * load double from local variable
 * index represent n
 * @author Zyi
 */
public class DLOAD_N extends LOAD_N {

    public DLOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    /**
     * 这里用index来代表指令中的n
     * 其中成员index是这条指令的参数，已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        double value = frame.getLocalVars().getDouble(index);
        frame.getOperandStack().pushDouble(value);
    }
}
