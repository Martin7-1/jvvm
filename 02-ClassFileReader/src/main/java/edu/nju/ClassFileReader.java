package edu.nju;

import edu.nju.entry.*;
import edu.nju.util.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Zyi
 */
public class ClassFileReader {
    private static final String FILE_SEPARATOR = File.separator;
    private static final String PATH_SEPARATOR = File.pathSeparator;

    private static Entry bootStrapReader;

    /**
     * tips:
     *      Every case can correspond to a class
     *      These cases are disordered
     *      You should take care of the order of if-else
     * case 1 classpath with wildcard
     * case 2 normal dir path
     * case 3 archived file
     * case 4 classpath with path separator
     *
     * @param classpath 文件路径
     * @return 文件路径对应的entry，工厂模式
     */
    public static Entry chooseEntryType(String classpath) {
        if (classpath.contains(PATH_SEPARATOR)) {
            // 首先判断classpath是不是复合型
            return new CompositeEntry(classpath);
        } else if (classpath.contains("*")) {
            // 通配符型
            return new WildEntry(classpath);
        } else if (classpath.toLowerCase().contains(".jar") || classpath.toLowerCase().contains(".zip")) {
            // 归档型
            return new ArchivedEntry(classpath);
        } else {
            // 相对路径型
            return new DirEntry(classpath);
        }
    }

    /**
     *
     * @param classpath where to find target class
     * @param className format: /package/.../class
     * @return content of classfile
     */
    public static byte[] readClassFile(String classpath,String className) throws ClassNotFoundException {
        className = IOUtil.transform(className);
        className += ".class";
        bootStrapReader = chooseEntryType(classpath);
        byte[] ret = new byte[0];
        try {
            ret = bootStrapReader.readClassFile(className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ret == null) {
            throw new ClassNotFoundException();
        }
        return ret;
    }
}
