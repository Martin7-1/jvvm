package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Zyi
 */
public abstract class MemberRefInfo extends ConstantPoolInfo {

    public MemberRefInfo(ConstantPool myCP) {
        super(myCP);
    }

    /**
     * 获得类名
     * @return class name
     */
    public abstract String getClassName();

    protected String getClassName(int idx) {
        return ((ClassInfo) myCP.get(idx)).getClassName();
    }

    /**
     * 获得类的名字和描述
     * @return name and descriptor
     */
    public abstract Pair<String, String> getNameAndDescriptor();
}
