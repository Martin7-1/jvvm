package edu.nju.entry;

import edu.nju.entry.Entry;
import edu.nju.util.IOUtil;

import java.io.*;

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
        if (classpath == null || className == null) {
            return null;
        }
        // 不需要递归考虑
        String newClasspath = IOUtil.transform(this.classpath);
        // 去掉通配符和通配符前面的路径分隔符
        newClasspath = newClasspath.substring(0, newClasspath.length() - 2);

        File file = new File(newClasspath);
        newClasspath += FILE_SEPARATOR;

        StringBuilder ansClasspath = new StringBuilder();
        if (file.isDirectory()) {
            // 读取该目录下的所有jar文件并组合成第四类
            String[] fileList = file.list();
            if (fileList == null) {
                // 该目录为空
                return null;
            }

            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].toLowerCase().contains(".jar")) {
                    ansClasspath.append(newClasspath).append(fileList[i]);
                }
                if (i != fileList.length - 1) {
                    ansClasspath.append(PATH_SEPARATOR);
                }
            }
        }

        return new CompositeEntry(ansClasspath.toString()).readClassFile(className);
    }
}
