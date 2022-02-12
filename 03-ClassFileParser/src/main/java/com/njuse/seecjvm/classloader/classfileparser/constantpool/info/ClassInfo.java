package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;


/**
 * @author Zyi
 */
public class ClassInfo extends ConstantPoolInfo {
    /**
     * Add some codes here.
     *
     * tips:
     * step1
     *      ClassInfo need a private field, what is it?
     * step2
     *      You need to add some args in constructor
     *      and don't forget to set tag
     *
     *      super method and super key word will help you
     */

    /**
     * todo attribute of ClassInfo
     * class info的结构由tag + nameIndex构成，其中tag的值是7
     */
    private int nameIndex;

    /**
     * todo constructor of ClassInfo
      *@param constantPool 常量池
     * @param nameIndex 名称索引
     */
    public ClassInfo(ConstantPool constantPool, int nameIndex){
        super(constantPool);
        this.nameIndex = nameIndex;
        super.tag = ConstantPoolInfo.CLASS;
    }

    /**
     * todo getClassName
     * @return class name
     */
    public String getClassName() {
        /**
         * Add some codes here.
         * tips: classname is in UTF8Info
         */

        return null;
    }

    @Override
    public String toString() {
        return getClassName();
    }
}
