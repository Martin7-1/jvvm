package com.njuse.seecjvm.instructions.conversion;

/**
 * 转化接口
 * @author Zyi
 */
public interface Convertable {

    /**
     * 将value转化
     * @param value 需要进行转化的值
     * @return 转化后的结果，压到操作数栈中
     */
    int convert(int value);
}
