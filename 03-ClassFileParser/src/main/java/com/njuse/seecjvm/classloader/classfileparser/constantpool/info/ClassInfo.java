package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;


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

    //todo attibute of ClassInfo


    //todo constructor of ClassInfo
    public ClassInfo(ConstantPool constantPool, int nameIndex){

        super(constantPool);
    }

    //todo getClassName
    public String getClassName() {
        /**
         * Add some codes here.
         * tips: classname is in UTF8Info
         */
        return "";
    }

    @Override
    public String toString() {

        return getClassName();
    }
}
