package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;

/**
 * fetch field from object
 * @author Zyi
 */
public class GETFIELD extends Index16Instruction {

    /**
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        // objectRef从操作数栈中出栈，且必须是一个引用类型的数据
        // 该索引所指向的运行时常量池项是一个对字段的符号引用
        // 包含字段的名称和描述符, 以及包含字段的类的符号引用
        // objectRef引用的对象不能是数组类型
        // 如果取值的字段是protected的，并且这个字段是当前类某个超类的成员，而这个字段又没有在同一个运行时包中定义过
        // objectRef所指向的对象类型必须为当前类或者当前类的子类

        // 指令执行后，objectRef所引用的对象中该字段的值会被取出，并插入操作数栈顶
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        OperandStack operandStack = frame.getOperandStack();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        Field field;

        try {
            // 获得已经解析过的符号引用
            field = fieldRef.getResolvedFieldRef();

            if (field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }

            String descriptor = fieldRef.getDescriptor();
            int slotId = field.getSlotID();
            resolveDescriptor(descriptor, operandStack, slotId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void resolveDescriptor(String descriptor, OperandStack operandStack, int slotID) {
        JObject tempRef;
        NonArrayObject ref;
        tempRef = operandStack.popObjectRef();
        checkRef(tempRef);
        ref = (NonArrayObject) tempRef;

        switch (descriptor.charAt(0)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                int intValue = ref.getFields().getInt(slotID);
                operandStack.pushInt(intValue);
                break;
            case 'F':
                float floatValue = ref.getFields().getFloat(slotID);
                operandStack.pushFloat(floatValue);
                break;
            case 'J':
                long longValue = ref.getFields().getLong(slotID);
                operandStack.pushLong(longValue);
                break;
            case 'D':
                double doubleValue = ref.getFields().getDouble(slotID);
                operandStack.pushDouble(doubleValue);
                break;
            case '[':
                // 这里不可能出现数组类型 do nothing
                // 可以 break 也可以不 break, 前面已经处理了如果是数组类型的情况
            case 'L':
                JObject objectValue = ref.getFields().getObjectRef(slotID);
                operandStack.pushObjectRef(objectValue);
                break;
            default:
        }
    }

    private void checkRef(JObject ref) {
        // 这里Java虚拟机规范没有规定此时应该抛出何种异常
        assert ref instanceof NonArrayObject;
        if (ref.isNull()) {
            throw new NullPointerException();
        }
    }
}
