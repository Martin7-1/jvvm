package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


/**
 * @author Zyi
 */
@Getter
public class UTF8Info extends ConstantPoolInfo {
    /**
     * Add some codes here.
     *
     * tips:
     * step1
     *      UTF8Info need some fields, what are they?
     * step2
     *      You need to add some args in constructor
     *      and don't forget to set tag
     *
     *      super method and super key word will help you
     *
     * step3
     *      The length of String is unknown for getConstantPoolInfo method
     *      How to return the instance with its length?
     *
     *      return a Pair<UTF8Info,Integer> or get the length of string in UTF8Info?
     *
     */

    /**
     * todo attributes of UTF8Info
     */
    private int length;
    private byte[] bytes;
    private String myString;

    /**
     * todo constructor of UTF8Info
     * @param constantPool 常量池
     * @param length UTF8中的bytes数组的长度
     * @param bytes 字符串值
     */
    public UTF8Info(ConstantPool constantPool, int length, byte[] bytes){
        super(constantPool);
        this.length = length;
        this.bytes = bytes;
        setString();
        super.tag = ConstantPoolInfo.UTF8;
    }

    private void setString() {
        // 将bytes数组变成一个String
        this.myString = new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Add some codes here.
     * return the string of UTF8Info
     */
    // todo getInstance
    static Pair<UTF8Info, Integer> getInstance(ConstantPool constantPool, byte[] in, int offset) {
        // 获得一个UTF8Info和对应的string长度，我们可以直接通过length来得到，即getU2()
        return null;
    }

    /**
     * todo return string
     * @return string
     */
    public String getString() {
        return this.myString;
    }
}
