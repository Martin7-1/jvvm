package edu.nju.entry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * format : dir/subdir;dir/subdir/*;dir/target.jar*
 * @author Zyi
 */
public class CompositeEntry extends Entry {
    public CompositeEntry(String classpath) {
        super(classpath);
    }

    private String[] getPath() {
        // 将复合型路径分隔开
        return this.classpath.split(PATH_SEPARATOR);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String[] paths = getPath();

        StringBuilder res = new StringBuilder();
        byte[] temp;

        for (String path : paths) {
            if (path.toLowerCase().contains(".jar")) {
                temp = new ArchivedEntry(path).readClassFile(className);
                if (temp != null) {
                    // 注意这里的temp默认是UTF-8编码的，否则需要在构造String的时候指定编码
                    res.append(new String(temp));
                }
            } else if (path.contains("*")) {
                temp = new WildEntry(path).readClassFile(className);
                if (temp != null) {
                    res.append(new String(temp));
                }
            } else {
                temp = new DirEntry(path).readClassFile(className);
                if (temp != null) {
                    res.append(new String(temp));
                }
            }
        }

        return res.toString().getBytes(StandardCharsets.UTF_8);
    }
}
