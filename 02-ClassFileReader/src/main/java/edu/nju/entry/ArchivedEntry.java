package edu.nju.entry;

import java.io.IOException;

/**
 * format : dir/subdir/target.jar
 * @author Zyi
 */
public class ArchivedEntry extends Entry {
    public ArchivedEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        return null;
    }
}
