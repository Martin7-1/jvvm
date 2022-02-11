package edu.nju.entry;

import java.io.IOException;

/**
 * format : dir/subdir/.../
 * @author Zyi
 */
public class DirEntry extends Entry {
    public DirEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        return null;
    }
}
