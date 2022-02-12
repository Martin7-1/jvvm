package com.njuse.seecjvm.runtime;

import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Zyi
 */
@Getter
@Setter
public class Vars {
    private Slot[] varSlots;
    private int maxSize;

    public Vars(int maxVarSize) {
        assert maxVarSize >= 0;
        maxSize = maxVarSize;
        varSlots = new Slot[maxVarSize];
        for (int i = 0; i < maxVarSize; i++) {
            varSlots[i] = new Slot();
        }
    }

    public void setInt(int index, int value) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        varSlots[index].setValue(value);
    }

    public int getInt(int index) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        return varSlots[index].getValue();
    }

    public void setFloat(int index, float value) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        varSlots[index].setValue(Float.floatToIntBits(value));
    }

    public float getFloat(int index) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        return Float.intBitsToFloat(varSlots[index].getValue());
    }

    /**
     * @param index 变量的起始下标
     * @param value 变量的值
     */
    public void setLong(int index, long value) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        // 设置Long时，要注意此时需要用到两格slot
        // 低4字节存储在局部变量表中较低的slot中
        int high4Bytes = (int) (value >> 32);
        int low4Bytes = (int) value;
        varSlots[index].setValue(low4Bytes);
        varSlots[index + 1].setValue(high4Bytes);
    }

    /**
     * @param index 变量的起始下标
     * @return 变量的值
     */
    public long getLong(int index) {
        int low4Bytes = varSlots[index].getValue();
        int high4Bytes = varSlots[index + 1].getValue();

        return (((long) high4Bytes) << 32) + low4Bytes;
    }

    public void setDouble(int index, double value) {
        if (index < 0 || index + 1 >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        setLong(index, Double.doubleToLongBits(value));
    }

    public double getDouble(int index) {
        if (index < 0 || index + 1 >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        return Double.longBitsToDouble(getLong(index));
    }

    public void setObjectRef(int index, JObject ref) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        varSlots[index].setObject(ref);
    }

    public JObject getObjectRef(int index) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        return varSlots[index].getObject();
    }

    public void setSlot(int index, Slot slot) {
        if (index < 0 || index >= maxSize) {
            throw new IndexOutOfBoundsException();
        }
        varSlots[index] = slot;
    }

}
