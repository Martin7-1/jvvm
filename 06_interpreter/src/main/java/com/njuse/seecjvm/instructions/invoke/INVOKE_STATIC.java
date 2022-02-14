package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.struct.Slot;

/**
 * @author Zyi
 */
public class INVOKE_STATIC extends INVOKE_BASE {

    /**
     * 1. TestUtil.equalInt(int a, int b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`, 其中，这个异常的message为
     * ：${第一个参数的值}!=${第二个参数的值}
     * 例如，TestUtil.equalInt(1, 2)应该抛出
     * RuntimeException("1!=2")
     *
     * 2. TestUtil.fail(): 抛出`RuntimeException`
     *
     * 3. TestUtil.equalFloat(float a, float b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`. 对于异常的message不作要求
     *
     */
    @Override
    public void execute(StackFrame frame) {
        Method toInvoke = getMethod(frame);

        JClass currentClz = frame.getMethod().getClazz();
        Constant ref = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert ref instanceof MethodRef;


        //check class whether init
        JClass currentClazz = toInvoke.getClazz();
        if (currentClazz.getInitState() == InitState.PREPARED) {
            // instruction length = opcode + operand = 3bytes
            frame.setNextPC(frame.getNextPC() - 3);
            currentClazz.initClass(frame.getThread(), currentClazz);
            return;
        }
        if (((MethodRef) ref).getClassName().contains("TestUtil")) {
            if (toInvoke.getName().contains("equalInt")) {
                int v2 = frame.getOperandStack().popInt();
                int v1 = frame.getOperandStack().popInt();
                if (v1 != v2) {
                    throw new RuntimeException(v1+"!="+v2);
                }
                frame.getOperandStack().pushInt(v2);
                frame.getOperandStack().pushInt(v1);

            } else if (toInvoke.getName().contains("equalFloat")) {
                float v1 = frame.getOperandStack().popFloat();
                float v2 = frame.getOperandStack().popFloat();
                if (v1 - v2 > 0.0001 || v1 - v2 < -0.0001) {
                    throw new RuntimeException();
                }
                frame.getOperandStack().pushFloat(v2);
                frame.getOperandStack().pushFloat(v1);

            } else if (toInvoke.getName().equals("fail")) {
                throw new RuntimeException();
            } else if (toInvoke.getName().contains("reach")) {
                int v1 = frame.getOperandStack().popInt();
                frame.getOperandStack().pushInt(v1);
                System.out.println(v1);
            }
        }

        invokeMethod(frame, initializeFrame(frame, toInvoke), toInvoke);
    }

    private Slot[] copyArguments(StackFrame frame, Method method) {
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }
        return argv;
    }


}
