package edu.nju.entry;

import edu.nju.entry.Entry;

import java.io.IOException;

/**
 * format : dir/.../*
 * @author Zyi
 */
public class WildEntry extends Entry {
    public WildEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        return null;
    }
}
