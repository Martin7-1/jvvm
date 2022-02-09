package edu.nju;

import java.io.IOException;

/**
 * format : dir/.../*
 */
public class WildEntry extends Entry{
    public WildEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        return null;
    }
}
