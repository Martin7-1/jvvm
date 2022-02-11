package edu.nju.entry;

import edu.nju.util.IOUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        if (classpath == null || className == null) {
            return null;
        }
        String newClasspath = IOUtil.transform(this.classpath);
        // 获取jar文件
        JarFile jarFile = new JarFile(newClasspath);
        // 获得jar文件的一个迭代器
        Enumeration<JarEntry> enumeration = jarFile.entries();
        InputStream fileInputStream = null;

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            String name = jarEntry.getName().replace('/', '\\');
            if (name.equals(className)) {
                fileInputStream = jarFile.getInputStream(jarEntry);
                break;
            }
        }

        if (fileInputStream == null) {
            return null;
        }

        return IOUtil.readFileByBytes(fileInputStream);
    }
}
