package com.njuse.seecjvm.instructions.store;

import com.njuse.seecjvm.instructions.base.Index8Instruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * store the value pop from the operand stack to the local variable
 * @author Zyi
 */
public class ISTORE extends Index8Instruction {

    /**
     * 从操作数栈中pop出一个元素并存入局部变量表
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        frame.getLocalVars().setInt(index, value);
    }
}
