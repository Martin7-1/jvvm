package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.runtime.StackFrame;

import java.nio.ByteBuffer;

/**
 * 
 * @author Zyi
 */
public class INVOKE_INTERFACE extends Index16Instruction {

    /**
     * TODO：实现这个方法
     * 这个方法用于读取这条指令操作码以外的部分
     */
    @Override
    public void fetchOperands(ByteBuffer reader) {

    }

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {

    }


}
