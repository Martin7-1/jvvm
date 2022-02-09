package edu.nju;

import java.io.IOException;

/**
 * format : dir/subdir;dir/subdir/*;dir/target.jar*
 */
public class CompositeEntry extends Entry{
    public CompositeEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        return null;
    }
}
