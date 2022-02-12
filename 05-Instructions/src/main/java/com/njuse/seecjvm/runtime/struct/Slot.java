package com.njuse.seecjvm.runtime.struct;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Zyi
 */
@Getter
@Setter
public class Slot {
    private JObject object;
    private Integer value;

    public Slot() {

    }

    public Slot(Integer value) {
        this.value = value;
    }

    public Slot(JObject object) {
        this.object = object;
    }

    @Override
    public Slot clone() {
        Slot toClone = new Slot();
        toClone.object = this.object;
        toClone.value = this.value;
        return toClone;
    }
}
