package com.njuse.seecjvm.instructions.control;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.JThread;
import com.njuse.seecjvm.runtime.StackFrame;

/**
 * return int from method
 * @author Zyi
 */
public class IRETURN extends NoOperandsInstruction {

    /**
     * 从当前栈帧的操作数栈中pop一个int，并压入调用者（即虚拟机栈中的下一个栈帧）的操作数栈中
     * @param frame 当前栈帧
     */
    @Override
    public void execute(StackFrame frame) {
        // 从当前栈帧的操作数栈中弹出一个int值然后压入调用者栈帧的操作数栈
        int value = frame.getOperandStack().popInt();
        JThread thread = frame.getThread();
        thread.popFrame();
        StackFrame callerFrame = thread.getTopFrame();
        callerFrame.getOperandStack().pushInt(value);
    }
}
