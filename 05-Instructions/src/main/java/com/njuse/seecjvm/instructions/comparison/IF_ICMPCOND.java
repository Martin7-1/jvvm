package com.njuse.seecjvm.instructions.comparison;

import com.njuse.seecjvm.instructions.base.BranchInstruction;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * 如果两个int值的条件比较为真，则跳转指令
 * @author Zyi
 */
public abstract class IF_ICMPCOND extends BranchInstruction {

    /**
     * 其中，condition 方法是对具体条件的计算，当条件满足时返回true，否则返回false
     */
    @Override
    public void execute(StackFrame frame) {
        // 条件满足时跳转至 PC + offset的位置
        // 注意在取指之后PC已经指向了下一条指令
        // 根据手册我们知道offset是从跳转指令开始算的
        // 即我们要先将PC指回上一条指令，然后再进行跳转
        // 3 = opcode + offset

        // 同时, 栈是后入先出结构, 即value2会先被pop, value1后被pop
        int value2 = frame.getOperandStack().popInt();
        int value1 = frame.getOperandStack().popInt();
        if (condition(value1, value2)) {
            frame.setNextPC(frame.getNextPC() - INSTR_LENGTH + super.offset);
        }
    }

    /**
     * 判断条件是否为真
     * @param v1 第一个值
     * @param v2 第二个值
     * @return true if satisfy the condition, false otherwise
     */
    protected abstract boolean condition(int v1, int v2);
}
