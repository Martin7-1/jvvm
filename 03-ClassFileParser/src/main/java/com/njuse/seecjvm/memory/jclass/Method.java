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
        int startIndex = 0;
        int len = descriptor.length();
        int argc = 0;

        while (startIndex < len) {
            switch (descriptor.charAt(startIndex)) {
                case 'B':
                case 'C':
                case 'F':
                case 'I':
                case 'L':
                case 'S':
                case 'Z':
                    argc += 1;
                    startIndex++;
                    break;
                case 'D':
                case 'J':
                    if (descriptor.charAt(startIndex + 1) != '[') {
                        argc += 2;
                        startIndex++;
                    } else {
                        argc += 1;
                        startIndex += 2;
                    }
                    break;
                default:
            }
        }

        return argc;
    }
}
