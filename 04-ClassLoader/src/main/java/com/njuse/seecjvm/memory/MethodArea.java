package com.njuse.seecjvm.memory;

import com.njuse.seecjvm.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Zyi
 */
@Getter
@Setter
public class MethodArea {
    private static MethodArea methodArea = new MethodArea();

    private MethodArea() {
        // LinkedHashMap: 有序的表
        classMap = new LinkedHashMap<>();
    }

    @Setter @Getter
    /**
     * key值是class的名称，value是对应的class
     */
    private static Map<String, JClass> classMap;

    public static MethodArea getInstance() {
        return methodArea;
    }

    public JClass findClass(String className) {
        if (classMap.keySet().stream().anyMatch(name -> name.equals(className))) {
            return classMap.get(className);
        } else {
            return null;
        }
    }

    public void addClass(String className, JClass clazz) {
        classMap.put(className, clazz);
    }
}
