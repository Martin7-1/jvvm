package com.njuse.seecjvm.instructions.comparison;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * compare double
 * NaN return -1
 * @author Zyi
 */
public class DCMPL extends NoOperandsInstruction {

    @Override
    public void execute(StackFrame frame) {
        // 栈是后入先出结构, 即value2会先被pop, value1后被pop
        double value2 = frame.getOperandStack().popDouble();
        double value1 = frame.getOperandStack().popDouble();

        if (Double.isNaN(value1) || Double.isNaN(value2)) {
            frame.getOperandStack().pushInt(-1);
        } else {
            frame.getOperandStack().pushInt(Double.compare(value1, value2));
        }
    }
}
