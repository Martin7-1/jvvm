package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * convert int to char
 * @author Zyi
 */
public class I2C extends NoOperandsInstruction {

    /**
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        // int转化为char, 然后32位无符号扩展后push到操作数栈
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        int charVal = value << 24;
        charVal >>>= 24;
        operandStack.pushInt(charVal);
    }
}
