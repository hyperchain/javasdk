package cn.hyperchain.sdk.common.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

    /**
     * read file content by lines with file path.
     * @param path file path
     * @return file content string
     * @throws IOException may can not find file
     */
    public static String readFile(String path) throws IOException {
        BufferedReader reader = null;
        if (Utils.isAbsolutePath(path)) {
            reader = new BufferedReader(new FileReader(path));
        } else {
            InputStream input = Utils.class.getClassLoader().getResourceAsStream(path);
            if (input == null) {
                throw new IOException("This file not exist! " + path);
            }
            reader = new BufferedReader(new InputStreamReader(input));
        }
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            // delete unused line separator
            while (stringBuilder.lastIndexOf(ls) == stringBuilder.length() - ls.length()) {
                stringBuilder.delete(stringBuilder.length() - ls.length(), stringBuilder.length());
            }
            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }
}
