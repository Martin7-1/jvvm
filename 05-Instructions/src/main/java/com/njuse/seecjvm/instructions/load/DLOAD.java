package com.njuse.seecjvm.instructions.load;

import com.njuse.seecjvm.instructions.base.Index8Instruction;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * load double from local variable
 * @author Zyi
 */
public class DLOAD extends Index8Instruction {

    /**
     * 从局部变量表中load一个double，然后push到操作数栈中
     * 其中成员index是这条指令的参数，已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        double value = frame.getLocalVars().getDouble(index);
        frame.getOperandStack().pushDouble(value);
    }
}
