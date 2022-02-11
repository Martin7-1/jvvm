package edu.nju.entry;

import java.io.File;
import java.io.IOException;

public abstract class Entry {
    public final String PATH_SEPARATOR = File.pathSeparator;
    public final String FILE_SEPARATOR = File.separator;
    public String classpath;

    public Entry(String classpath){
        this.classpath = classpath;
    }

    /**
     * 根据classpath和classname来读取文件内容
     * @param className 类名
     * @return 类的内容
     * @throws IOException IO异常
     */
    public abstract byte[] readClassFile(String className) throws IOException;
}
