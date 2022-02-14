package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;

/**
 * set field in object
 * @author Zyi
 */
public class PUTFIELD extends Index16Instruction {

    /**
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        // 该索引所指向的运行时常量池项是一个对字段的符号引用
        // 包含字段的名称和描述符, 以及包含字段的类的符号引用
        // objectRef引用的对象不能是数组类型
        // 如果取值的字段是protected的，并且这个字段是当前类某个超类的成员，而这个字段又没有在同一个运行时包中定义过
        // objectRef所指向的对象类型必须为当前类或者当前类的子类

        // 该字段的符号引用是被解析过的(resolved)，被该指令存储到字段的value值必须与字段的描述符相匹配
        // 注意如果字段是final的，那么只有在当前类的实例初始化方法设置当前类的final字段才是合法的

        // value和objectRef从操作数栈中出栈

        // 获得当前栈帧所对应类的运行时常量池，操作数栈
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        OperandStack operandStack = frame.getOperandStack();
        Field field;
        // 获得value和objectRef，这里objectRef引用对象不能是数组类型
        try {
            // 获得已经解析过的字段
            field = fieldRef.getResolvedFieldRef();

            // 如果field已解析的字段是静态的，那么会抛出IncompatibleClassChangeError
            if (field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }
            // 如果field是final的，那么只有在当前类的实例初始化方法中设置才是合理的，否则抛出IllegalAccessError
            if (field.isFinal()) {
                if (!field.getClazz().getName().equals(frame.getMethod().getClazz().getName()) || !"<init>".equals(frame.getMethod().getName())) {
                    throw new IllegalAccessError();
                }
            }

            String descriptor = field.getDescriptor();
            int slotID = field.getSlotID();
            resolveDescriptor(descriptor, operandStack, slotID);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void resolveDescriptor(String descriptor, OperandStack operandStack, int slotID) {
        // 这里这个ref直接设置成非数组类型是由指令描述所保证的
        NonArrayObject ref;
        JObject tempRef;

        switch (descriptor.charAt(0)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                // <= 32位的数据类型
                int value = operandStack.popInt();
                // 首先检查下一个pop出的是不是一个非数组类型的对象
                tempRef = operandStack.popObjectRef();
                checkRef(tempRef);
                ref = (NonArrayObject) tempRef;
                ref.getFields().setInt(slotID, value);
                break;
            case 'F':
                float floatValue = operandStack.popFloat();
                tempRef = operandStack.popObjectRef();
                checkRef(tempRef);
                ref = (NonArrayObject) tempRef;
                ref.getFields().setFloat(slotID, floatValue);
                break;
            case 'J':
                long longValue = operandStack.popLong();
                tempRef = operandStack.popObjectRef();
                checkRef(tempRef);
                ref = (NonArrayObject) tempRef;
                ref.getFields().setLong(slotID, longValue);
                break;
            case 'D':
                double doubleValue = operandStack.popDouble();
                tempRef = operandStack.popObjectRef();
                checkRef(tempRef);
                ref = (NonArrayObject) tempRef;
                ref.getFields().setDouble(slotID, doubleValue);
                break;
            case '[':
                // 数组类型，do nothing
                // 这里break或者不break都可以，会到checkRef来抛出异常
            case 'L':
                // 引用类型
                JObject refValue = operandStack.popObjectRef();
                tempRef = operandStack.popObjectRef();
                ref = (NonArrayObject) tempRef;
                ref.getFields().setObjectRef(slotID, refValue);
                break;
            default:
        }
    }

    private void checkRef(JObject ref) {
        // 字节码指令规范没规定ObjectRef如果是数组类型对象时应该抛出何种异常
        assert ref instanceof NonArrayObject;
        if (ref.isNull()) {
            throw new NullPointerException();
        }
    }

}
