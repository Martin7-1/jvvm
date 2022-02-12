package com.njuse.seecjvm.instructions.base;

import com.njuse.seecjvm.runtime.StackFrame;

import java.nio.ByteBuffer;

public abstract class Instruction {

    /**
     * 执行
     * @param frame 栈帧
     */
    public abstract void execute(StackFrame frame);

    /**
     * 获得操作数
     * @param reader 字节的缓冲区
     */
    public abstract void fetchOperands(ByteBuffer reader);
}
