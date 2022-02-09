package com.njuse.seecjvm.instructions.store;

import com.njuse.seecjvm.runtime.StackFrame;

public class ISTORE_N extends STORE_N {
    public ISTORE_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    /**
     * TODO：实现这条指令
     * 其中，成员变量index是这个指令的参数，已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {

    }
}
