package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.MethodInfo;
import com.njuse.seecjvm.classloader.classfileparser.attribute.CodeAttribute;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Method extends ClassMember {
    private int maxStack;
    private int maxLocal;
    private int argc;
    private byte[] code;

    public Method(MethodInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();

        CodeAttribute codeAttribute = info.getCodeAttribute();
        if (codeAttribute != null) {
            maxLocal = codeAttribute.getMaxLocal();
            maxStack = codeAttribute.getMaxStack();
            code = codeAttribute.getCode();
        }
        argc = calculateArgcFromDescriptor(descriptor);
    }

    /**
     * todo calculateArgcFromDescriptor
     * @param descriptor 方法描述符
     * @return 参数列表长度 argc
     */
    private int calculateArgcFromDescriptor(String descriptor) {
        /**
         * Add some codes here.
         * Here are some examples in README!!!
         *
         * You should refer to JVM specification for more details
         *
         * Beware of long and double type
         */
        // 首先我们需要得到参数列表的范围
        int startIndex = 0;
        while (descriptor.charAt(startIndex) != '(') {
            startIndex++;
        }
        int endIndex = startIndex + 1;
        while (descriptor.charAt(endIndex) != ')') {
            endIndex++;
        }
        int argc = 0;
        int index = startIndex;

        while (index < endIndex) {
            switch (descriptor.charAt(index)) {
                case 'B':
                case 'C':
                case 'F':
                case 'I':
                case 'S':
                case 'Z':
                    argc += 1;
                    break;
                case 'D':
                case 'J':
                    if (descriptor.charAt(index + 1) != '[') {
                        argc += 2;
                    } else {
                        argc += 1;
                    }
                    break;
                case 'L':
                    // todo: 让index指向分号
                    argc += 1;
                    index = calIndex(descriptor, index);
                default:
            }
            index++;
        }

        return argc;
    }

    private int calIndex(String descriptor, int index) {
        int newIndex = index;
        while (descriptor.charAt(newIndex) != ';') {
            newIndex++;
        }

        return newIndex;
    }
}
