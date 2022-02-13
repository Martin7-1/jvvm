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
        int value1 = frame.getOperandStack().popInt();
        int value2 = frame.getOperandStack().popInt();
        if (condition(value1, value2)) {
            frame.setNextPC(frame.getNextPC() + offset);
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
