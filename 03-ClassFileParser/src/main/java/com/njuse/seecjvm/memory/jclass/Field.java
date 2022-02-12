package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.FieldInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Zyi
 */
@Getter
@Setter
public class Field extends ClassMember {
    public Field(FieldInfo info, JClass clazz) {
        // todo initialize the attributes from info
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();
    }
}
