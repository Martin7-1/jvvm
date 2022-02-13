package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * int convert to float
 * @author Zyi
 */
public class I2F extends NoOperandsInstruction implements Convertable {

    /**
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        // int 转化为 float 然后push到操作数栈中
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        float floatValue = (float) value;
        operandStack.pushFloat(floatValue);
    }

    @Override
    public int convert(int value) {
        return 0;
    }
}
