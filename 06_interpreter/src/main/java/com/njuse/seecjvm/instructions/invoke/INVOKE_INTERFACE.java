package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * 调用接口方法，运行时搜索一个实现了这个接口方法的对象，找出适合的方法并调用。
 * @author Zyi
 */
public class INVOKE_INTERFACE extends Index16Instruction {

    /**
     * TODO：实现这个方法
     * 这个方法用于读取这条指令操作码以外的部分
     */
    @Override
    public void fetchOperands(ByteBuffer reader) {
        super.fetchOperands(reader);
        reader.get();
        reader.get();
    }

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {
        JClass curClazz = frame.getMethod().getClazz();
        Constant methodRef = curClazz.getRuntimeConstantPool().getConstant(super.index);
        assert methodRef instanceof InterfaceMethodRef;
        Method method = ((InterfaceMethodRef) methodRef).resolveInterfaceMethodRef();

        // copy arguments
        int argCount = method.getArgc();
        Slot[] argv = new Slot[argCount];
        for (int i = 0; i < argCount; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }

        // lookup interface function
        JObject objectRef = frame.getOperandStack().popObjectRef();
        JClass clazz = objectRef.getClazz();
        Method invokeMethod = ((InterfaceMethodRef) methodRef).resolveInterfaceMethodRef(clazz);

        StackFrame newFrame = prepareNewFrame(frame, argCount, argv, objectRef, invokeMethod);

        // 将新的栈帧压入Java虚拟机栈
        frame.getThread().pushFrame(newFrame);

        // 处理本地方法
        if (method.isNative()) {
            if (!"registerNatives".equals(method.getName())) {
                System.out.println("Native method:"
                        + method.getClazz().getName()
                        + method.name
                        + method.descriptor);
            }
            frame.getThread().popFrame();
        }
    }

    private StackFrame prepareNewFrame(StackFrame frame, int argc, Slot[] argv, JObject objectRef, Method toInvoke) {
        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal() + 1);
        Vars localVars = newFrame.getLocalVars();
        Slot thisSlot = new Slot();
        thisSlot.setObject(objectRef);
        localVars.setSlot(0, thisSlot);
        for (int i = 1; i < argc + 1; i++) {
            localVars.setSlot(i, argv[argc - i]);
        }
        return newFrame;
    }


}
