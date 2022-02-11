package edu.nju.entry;

import edu.nju.util.IOUtil;

import java.io.*;

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
        if (classpath == null || className == null) {
            return null;
        }
        String newClasspath = IOUtil.transform(classpath);
        InputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(newClasspath + FILE_SEPARATOR + className);
        } catch (FileNotFoundException e) {
            return null;
        }

        return IOUtil.readFileByBytes(fileInputStream);
    }
}
