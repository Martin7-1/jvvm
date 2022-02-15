package com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.info.MethodrefInfo;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Zyi
 */
@Getter
@Setter
public class MethodRef extends MemberRef {
    private Method method;

    public MethodRef(RuntimeConstantPool runtimeConstantPool, MethodrefInfo methodrefInfo) {
        super(runtimeConstantPool, methodrefInfo);
    }

    /**
     * 这个方法用来实现对象方法的动态查找
     * @param clazz 对象的引用
     */
    public Method resolveMethodRef(JClass clazz) {
        resolve(clazz);
        return method;
    }

    /**
     * 这个方法用来解析methodRef对应的方法
     * 与上面的动态查找相比，这里的查找始终是从这个Ref对应的class开始查找的
     */
    public Method resolveMethodRef() {
        if (method == null) {
            try {
                resolveClassRef();
                resolve(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return method;
    }

    private void resolve(JClass clazz) {
        assert clazz != null;
        Optional<Method> optionalMethod;

        for (JClass curClazz = clazz; curClazz != null; curClazz = curClazz.getSuperClass()) {
            optionalMethod = curClazz.resolveMethod(name, descriptor);
            if (optionalMethod.isPresent()) {
                method = optionalMethod.get();
                return;
            }
        }

        JClass[] tempInterfaces = clazz.getInterfaces();
        Deque<JClass> interfaces = new LinkedList<>(Arrays.asList(tempInterfaces));

        while (!interfaces.isEmpty()) {
            JClass jClass = interfaces.pop();
            optionalMethod = jClass.resolveMethod(name, descriptor);
            if (optionalMethod.isPresent()) {
                method = optionalMethod.get();
                return;
            }
            interfaces.addAll(Arrays.asList(jClass.getInterfaces()));
        }
    }


    @Override
    public String toString() {
        return "MethodRef to " + className;
    }
}
