package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * convert int to byte
 * @author Zyi
 */
public class I2B extends NoOperandsInstruction {

    /**
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        // 将int转化成byte, 然后32位符号扩展后push到操作数栈中
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        // 32位左移24位变成8位
        int byteVal = value << 24;
        // 然后再向右符号右移24位，Java中符号右移是>>, 无符号右移是>>>
        byteVal >>= 24;
        operandStack.pushInt(byteVal);
    }
}
