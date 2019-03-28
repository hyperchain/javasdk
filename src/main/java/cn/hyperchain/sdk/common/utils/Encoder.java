package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.contract.BaseInvoke;
import com.google.gson.Gson;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassLoaderRepository;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Encoder {

    private static final Logger logger = Logger.getLogger(Encoder.class);

    private static boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.startsWith("file:/") || path.contains(":\\");
    }

    /**
     * get deploy payload.
     *
     * @param path contract jar path
     * @return payload
     */
    public static String encodeDeployJar(String path) {
        JarFile jar = null;
        InputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            if (isAbsolutePath(path)) {
                jar = new JarFile(path, true);
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
                    jar = new JarFile(new File(url.toURI()));
                    fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                }
            }
            if (jar != null && jar.getManifest().getMainAttributes().getValue("Main-Class") == null) {
                throw new IOException("the path does not point to a contract jar");
            }

            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                // 如果有数据的话，就把数据添加到输出流
                baos.write(buf, 0, len);
            }
            byte[] buffer = baos.toByteArray();
            if (buffer.length > 1024 * 64) {
                throw new IOException("the contract jar should not be larger than 64KB");
            }
            return ByteUtil.toHex(buffer);
        } catch (IOException | URISyntaxException e ) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                logger.error("close stream fail, " + e.getMessage());
            }
        }
    }

    /**
     * get hvm invoke payload.
     *
     * @param bean invoke bean
     * @return payload
     */
    public static String encodeInvokeBeanJava(BaseInvoke bean) {
        try {
            //1. get the bean class bytes
            ClassLoaderRepository repository = new ClassLoaderRepository(Thread.currentThread().getContextClassLoader());
            JavaClass beanClass = repository.loadClass(bean.getClass());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            beanClass.dump(baos);
            byte[] clazz = baos.toByteArray();
            if (clazz.length > 0xffff) {
                throw new IOException("the bean class is too large");
            }
            //2. get the bean class name
            byte[] clzName = bean.getClass().getCanonicalName().getBytes();
            if (clzName.length > 0xffff) {
                throw new IOException("the bean class name is too large");
            }
            //3. get the bin of bean
            Gson gson = new Gson();
            byte[] beanBin = gson.toJson(bean).getBytes();
            if (beanBin.length > 0xffff) {
                throw new IOException("the bean Object is too large");
            }
            //4. accumulate: | class length(2B) | name length(2B) | class | class name | bin |
            //               | len(txHash)      | len("__txHash__")| txHash | "__txHash__" | bin |
            StringBuilder sb = new StringBuilder();
            sb.append(ByteUtil.toHex(ByteUtil.shortToBytes((short) clazz.length)));
            sb.append(ByteUtil.toHex(ByteUtil.shortToBytes((short) clzName.length)));
            //NOTE: name不用给了
            sb.append(ByteUtil.toHex(clazz));
            sb.append(ByteUtil.toHex(clzName));
            sb.append(ByteUtil.toHex(beanBin));
            return sb.toString();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
