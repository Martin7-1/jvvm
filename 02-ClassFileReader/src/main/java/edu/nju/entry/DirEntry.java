package edu.nju.entry;

import edu.nju.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        String newClasspath = IOUtil.transform(classpath);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(newClasspath + FILE_SEPARATOR + className);
        } catch (FileNotFoundException e) {
            return null;
        }

        return IOUtil.readFileByBytes(fileInputStream);
    }
}
