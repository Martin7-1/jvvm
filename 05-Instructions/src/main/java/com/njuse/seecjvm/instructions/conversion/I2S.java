package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * convert int to short
 * @author Zyi
 */
public class I2S extends NoOperandsInstruction {

    /**
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        // short: 16bit
        // 符号扩展
        // int 转化为 short 然后push到操作数栈中
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        int shortVal = value << 16;
        shortVal >>= 16;
        operandStack.pushInt(shortVal);
    }
}
