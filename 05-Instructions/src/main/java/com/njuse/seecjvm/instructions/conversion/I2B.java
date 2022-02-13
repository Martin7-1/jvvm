package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * convert int to byte
 * @author Zyi
 */
public class I2B extends NoOperandsInstruction implements Convertable {

    /**
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        // 将int转化成byte, 然后32位符号扩展后push到操作数栈中
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        int byteVal = (byte) value;
        operandStack.pushInt(byteVal);
    }

    @Override
    public int convert(int value) {
        return (byte) value;
    }
}
