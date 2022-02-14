package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.JHeap;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;
import org.junit.runners.model.InitializationError;

/**
 * create new object
 * @author Zyi
 */
public class NEW extends Index16Instruction {

    public static final int INSTR_LENGTH = 3;

    /**
     * 索引指向的运行时常量池项是一个类或接口的符号引用
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        // 索引指向的必须是一个接口或者类的符号引用
        // 这个类或接口必须是已经解析过的
        // 最终解析结果应该是一个具体的类
        // 以此为类的新实例会分配到GC中，且该实例的所有实例变量都会初始化为相应类型的初始值
        // 同时 objectRef会入栈到操作数栈中
        // 成功解析但未初始化的类将会在这个时候进行初始化

        // 获得运行时常量池
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        ClassRef classRef = (ClassRef) runtimeConstantPool.getConstant(index);

        try {
            // 获得已经解析好的类
            JClass clazz = classRef.getResolvedClass();
            // 判断类是否已经初始化
            if (clazz.getInitState() == InitState.PREPARED) {
                frame.setNextPC(frame.getNextPC() - INSTR_LENGTH);
                clazz.initClass(frame.getThread(), clazz);
            }

            // 判断是否是接口或者是抽象类不可实例化
            if (clazz.isInterface() || clazz.isAbstract()) {
                throw new InstantiationError();
            }
            // 实例化
            NonArrayObject ref = clazz.newObject();
            // 将新实例加入堆
            JHeap.getInstance().addObj(ref);
            frame.getOperandStack().pushObjectRef(ref);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
