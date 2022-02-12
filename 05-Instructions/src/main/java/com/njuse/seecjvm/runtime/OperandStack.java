package com.njuse.seecjvm.runtime;

import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;
import lombok.Getter;
import lombok.Setter;

import java.util.EmptyStackException;

/**
 * @author Zyi
 */
@Getter
@Setter
public class OperandStack {
    /**
     * top指向的是栈顶的一个 空闲 位置
     */
    private int top;
    private int maxStackSize;
    private Slot[] slots;

    public OperandStack(int maxStackSize) {
        assert maxStackSize >= 0;
        this.maxStackSize = maxStackSize;
        slots = new Slot[maxStackSize];
        for (int i = 0; i < maxStackSize; i++) {
            slots[i] = new Slot();
        }
        top = 0;
    }

    /**
     * 向操作数栈顶端push一个int型变量
     * @param value 变量的值
     */
    public void pushInt(int value) {
        // 首先判断操作数栈是否已经满了
        if (top >= maxStackSize) {
            throw new StackOverflowError();
        }
        slots[top++].setValue(value);
    }

    /**
     * 从操作数栈顶端pop一个int型变量
     * @return 返回这个int的值
     */
    public int popInt() {
        if (top <= 0) {
            throw new EmptyStackException();
        }
        top--;
        int value = slots[top].getValue();
        slots[top] = new Slot();
        return value;
    }

    /**
     * 向操作数栈顶端push一个float型变量
     * @param value 变量的值
     */
    public void pushFloat(float value) {
        if (top >= maxStackSize) {
            throw new StackOverflowError();
        }
        slots[top].setValue(Float.floatToIntBits(value));
        top++;
    }

    /**
     * 从操作数栈顶端pop一个float型变量
     * @return 返回这个int的值
     */
    public float popFloat() {
        top--;
        if (top < 0) {
            throw new EmptyStackException();
        }
        float ret = Float.intBitsToFloat(slots[top].getValue());
        slots[top] = new Slot();
        return ret;
    }

    /**
     * 向操作数栈顶push一个 long 类型的变量
     * @param value 变量的值
     */
    public void pushLong(long value) {
        // push low 4 bytes to the stack first
        int low4Bytes = (int) value;
        int high4Bytes = (int) (value >> 32);
        pushInt(low4Bytes);
        pushInt(high4Bytes);
    }

    /**
     * 从操作数栈顶端pop一个long型变量
     * @return 返回这个long的值
     */
    public long popLong() {
        int high4Bytes = popInt();
        int low4Bytes = popInt();

        return (((long) high4Bytes) << 32) + low4Bytes;
    }

    /**
     * 向操作数栈push一个double类型的变量
     * @param value 变量的值
     */
    public void pushDouble(double value) {
        pushLong(Double.doubleToLongBits(value));
    }

    /**
     * 从操作数栈顶端pop一个double型变量
     * @return 返回这个double的值
     */
    public double popDouble() {
        return Double.longBitsToDouble(popLong());
    }

    /**
     * 向操作数栈push一个对象的引用
     * @param ref 对象引用
     */
    public void pushObjectRef(JObject ref) {
        if (top >= maxStackSize) {
            throw new StackOverflowError();
        }
        slots[top].setObject(ref);
        top++;
    }

    /**
     * 从操作数栈pop一个对象的引用
     * @return 对象引用
     */
    public JObject popObjectRef() {
        top--;
        if (top < 0) {
            throw new EmptyStackException();
        }
        JObject ret = slots[top].getObject();
        slots[top] = new Slot();
        return ret;
    }

    public void pushSlot(Slot slot) {
        if (top >= maxStackSize) {
            throw new StackOverflowError();
        }
        slots[top] = slot;
        top++;
    }

    public Slot popSlot() {
        top--;
        if (top < 0) {
            throw new EmptyStackException();
        }
        Slot ret = slots[top];
        slots[top] = new Slot();
        return ret;
    }

}
