package com.njuse.seecjvm.instructions.load;

import com.njuse.seecjvm.instructions.base.Index8Instruction;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * load long from local variable to operand stack
 * @author Zyi
 */
public class ILOAD extends Index8Instruction {

    /**
     * 从局部变量表加载一个long，并push到操作数栈中
     * 其中成员index是这条指令的参数，已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        long value = frame.getLocalVars().getLong(index);
        frame.getOperandStack().pushLong(value);
    }
}
