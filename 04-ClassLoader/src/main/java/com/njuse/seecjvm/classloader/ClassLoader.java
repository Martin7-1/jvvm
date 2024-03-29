package com.njuse.seecjvm.classloader;

import com.njuse.seecjvm.classloader.classfileparser.ClassFile;
import com.njuse.seecjvm.classloader.classfilereader.ClassFileReader;
import com.njuse.seecjvm.classloader.classfilereader.classpath.EntryType;
import com.njuse.seecjvm.memory.MethodArea;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.NullObject;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

/**
 * @author Zyi
 */
public class ClassLoader {
    private static ClassLoader classLoader = new ClassLoader();
    private ClassFileReader classFileReader;
    private MethodArea methodArea;

    private ClassLoader() {
        classFileReader = ClassFileReader.getInstance();
        methodArea = MethodArea.getInstance();
    }

    public static ClassLoader getInstance() {
        return classLoader;
    }

    /**
     * load phase
     *
     * @param className       name of class
     * @param initiatingEntry null value represents load MainClass
     */
    public JClass loadClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        if ((ret = methodArea.findClass(className)) == null) {
            return loadNonArrayClass(className, initiatingEntry);
        }
        return ret;
    }

    private JClass loadNonArrayClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        try {
            Pair<byte[], Integer> res = classFileReader.readClassFile(className, initiatingEntry);
            byte[] data = res.getKey();
            EntryType definingEntry = new EntryType(res.getValue());
            //define class
            JClass clazz = defineClass(data, definingEntry);
            //link class
            linkClass(clazz);
            return clazz;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    /**
     *
     * define class
     * @param data binary of class file
     * @param definingEntry defining loader of class
     */
    private JClass defineClass(byte[] data, EntryType definingEntry) throws ClassNotFoundException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        clazz.setLoadEntryType(definingEntry);
        this.resolveSuperClass(clazz);
        this.resolveInterfaces(clazz);
        // add to method area
        this.methodArea.addClass(clazz.getName(), clazz);
        return clazz;
    }

    /**
     * load superclass before add to method area
     */
    private void resolveSuperClass(JClass clazz) throws ClassNotFoundException {
        // 递归加载父类, 注意只有java.lang.Object是没有父类的
        if (!JClass.OBJECT_CLASS_NAME.equals(clazz.getName())) {
            String superClassName = clazz.getSuperClassName();
            EntryType initiatingEntry = clazz.getLoadEntryType();
            clazz.setSuperClass(loadClass(superClassName, initiatingEntry));
        }

    }

    /**
     * load interfaces before add to method area
     */
    private void resolveInterfaces(JClass clazz) throws ClassNotFoundException {
        String[] interfacesName = clazz.getInterfaceNames();
        EntryType initiatingEntry = clazz.getLoadEntryType();
        int len = interfacesName.length;
        JClass[] interfaces = new JClass[len];
        for (int i = 0; i < len; i++) {
            interfaces[i] = this.loadClass(interfacesName[i], initiatingEntry);
        }
        clazz.setInterfaces(interfaces);
    }

    /**
     * link phase
     */
    private void linkClass(JClass clazz) {
        verify(clazz);
        prepare(clazz);
    }

    /**
     * You don't need to write any code here.
     */
    private void verify(JClass clazz) {
        //do nothing
    }

    private void prepare(JClass clazz) {
        /**
         * Add some codes here.
         *
         * step1 (We do it for you here)
         *      count the fields slot id in instance
         *      count the fields slot id in class
         * step2
         *      alloc memory for fields(We do it for you here) and init static vars
         * step3
         *      set the init state
         */
        // step1
        calInstanceFieldSlotIDs(clazz);
        calStaticFieldSlotIDs(clazz);
        // step2
        allocAndInitStaticVars(clazz);
        // step3
        clazz.setInitState(InitState.PREPARED);
    }

    /**
     * count the number of field slots in instance
     * long and double takes two slots
     * the field is not static
     */
    private void calInstanceFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        if (clazz.getSuperClass() != null) {
            slotID = clazz.getSuperClass().getInstanceSlotCount();
        }
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (!f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) {
                    slotID++;
                }
            }
        }
        clazz.setInstanceSlotCount(slotID);
    }

    /**
     * count the number of field slots in class
     * long and double takes two slots
     * the field is static
     */
    private void calStaticFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) {
                    slotID++;
                }
            }
        }
        clazz.setStaticSlotCount(slotID);
    }

    /**
     * primitive type is set to 0
     * ref type is set to null
     */
    private void initDefaultValue(JClass clazz, Field field) {
        /**
         * Add some codes here.
         * step 1
         *      get static vars of class
         * step 2
         *      get the slotID index of field
         * step 3
         *      switch by descriptor or some part of descriptor
         *      Handle basic type ZBCSIJDF and references (with new NullObject())
         */
        Vars vars = clazz.getStaticVars();
        int slotID = field.getSlotID();
        String descriptor = field.getDescriptor();

        // 通过descriptor确定这个字段的类型，然后设置默认值
        // 默认值设置如下:
        //   1. 整数类型: byte/short/int/long/char 默认值为0，其中char也可以表示为'\u0000'
        //   2. 浮点类型: float/double 默认值为正数0，即0.0f和0.0d
        //   3. 布尔类型: boolean 默认值为false
        switch (descriptor.charAt(0)) {
            case 'I':
            case 'S':
            case 'C':
            case 'B':
            case 'Z':
                vars.setInt(slotID, 0);
                break;
            case 'J':
                vars.setLong(slotID, 0L);
                break;
            case 'F':
                vars.setFloat(slotID, 0.0f);
                break;
            case 'D':
                vars.setDouble(slotID, 0.0d);
                break;
            default:
                vars.setObjectRef(slotID, new NullObject());
        }
    }

    /**
     * load const value from runtimeConstantPool for primitive type
     * String is not support now
     */
    private void loadValueFromRTCP(JClass clazz, Field field) {
        /**
         * Add some codes here.
         *
         * step 1
         *      get static vars and runtime constant pool of class
         * step 2
         *      get the slotID and constantValue index of field
         * step 3
         *      switch by descriptor or some part of descriptor
         *      just handle basic type ZBCSIJDF, you don't have to throw any exception
         *      use wrapper to get value
         *
         *  Example
         *      long longVal = ((LongWrapper) runtimeConstantPool.getConstant(constantPoolIndex)).getValue();
         */
        Vars vars = clazz.getStaticVars();
        RuntimeConstantPool runtimeConstantPool = clazz.getRuntimeConstantPool();
        int slotID = field.getSlotID();
        int constantValueIndex = field.getConstValueIndex();
        String descriptor = field.getDescriptor();

        switch (descriptor.charAt(0)) {
            case 'I':
            case 'S':
            case 'C':
            case 'B':
            case 'Z':
                int intVal = ((IntWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                vars.setInt(slotID, intVal);
                break;
            case 'J':
                long longVal = ((LongWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                vars.setLong(slotID, longVal);
                break;
            case 'F':
                float floatVal = ((FloatWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                vars.setFloat(slotID, floatVal);
                break;
            case 'D':
                double doubleVal = ((DoubleWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                vars.setDouble(slotID, 0.0d);
                break;
            default:
        }
    }

    /**
     * the value of static final field is in com.njuse.seecjvm.runtime constant pool
     * others will be set to default value
     */
    private void allocAndInitStaticVars(JClass clazz) {
        clazz.setStaticVars(new Vars(clazz.getStaticSlotCount()));
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            /**
             * Add some codes here.
             *
             * Refer to manual for details.
             */
            // 注意常量会在准备阶段就被初始化为初始值，且该初始值是从运行时常量池(Runtime Constant Pool)中读取的
            if (f.isStatic() && f.isFinal()) {
                loadValueFromRTCP(clazz, f);
            } else {
                initDefaultValue(clazz, f);
            }
        }
    }
}
