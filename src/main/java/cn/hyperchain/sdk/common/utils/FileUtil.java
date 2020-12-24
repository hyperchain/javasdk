package cn.hyperchain.sdk.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {

    /**
     * read file content by lines with file path.
     * @param input file input stream
     * @return file content string
     * @throws IOException may not find file
     */
    public static String readFile(InputStream input) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(input));

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

    /**
     * get InputStream of the file for the given path.
     * @param path jar path
     * @return input stream for the jar path
     * @throws IOException may not find file
     */
    public static InputStream readFileAsStream(String path) throws IOException {
        InputStream fis = null;
        if (Utils.isAbsolutePath(path)) {
            fis = new FileInputStream(path);
        } else {
            URL url = Thread.currentThread().getContextClassLoader().getResource(path);
            if (url == null) {
                throw new IOException("Jar: " + path + " not found.");
            }

            if (url.toString().startsWith("jar")) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                JarFile jarFile = connection.getJarFile();
                Enumeration enu = jarFile.entries();
                while (enu.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) enu.nextElement();
                    String name = jarEntry.getName();
                    if (name.startsWith(path)) {
                        if (name.endsWith(".jar")) {
                            fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
                        }
                    }
                }
            } else {
                fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            }
        }
        return fis;

    }

    /**
     * get getStringStream of the stream for the given string.
     * @param sInputString given string
     * @return input stream for the given string
     * @throws Exception may not find file
     */
    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                if (tInputStringStream.toString() == null) {
                    throw new Exception(sInputString + "is null");
                }
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
