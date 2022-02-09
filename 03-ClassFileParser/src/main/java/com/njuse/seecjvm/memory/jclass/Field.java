package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.FieldInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field extends ClassMember {
    public Field(FieldInfo info, JClass clazz) {
        //todo initilize the attributes from info
        /**
         * tips: refer to constructor of Method
         */

    }
}
