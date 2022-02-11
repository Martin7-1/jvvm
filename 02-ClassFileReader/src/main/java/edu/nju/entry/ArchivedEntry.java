package edu.nju.entry;

import edu.nju.util.IOUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
        String newClasspath = IOUtil.transform(this.classpath);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(newClasspath + FILE_SEPARATOR + className);
        } catch (FileNotFoundException e) {
            return null;
        }

        return IOUtil.readFileByBytes(fileInputStream);
    }
}
