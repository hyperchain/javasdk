package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.contract.BaseInvoke;
import cn.hyperchain.sdk.crypto.HashUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassLoaderRepository;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Encoder {

    private static final Logger logger = Logger.getLogger(Encoder.class);

    /**
     * get deploy payload.
     *
     * @param fis FileinputStream for the given jar file
     * @return payload
     */
    public static String encodeDeployJar(InputStream fis) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;

        try {
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            byte[] buffer = baos.toByteArray();
            if (buffer.length > 1024 * 512) {
                throw new IOException("the contract jar should not be larger than 512KB");
            }

            return ByteUtil.toHex(buffer);
        } catch (IOException e) {
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
                throw new IOException("the bean class is too large"); // 64k
            }
            //2. get the bean class name
            byte[] clzName = bean.getClass().getCanonicalName().getBytes(Utils.DEFAULT_CHARSET);
            if (clzName.length > 0xffff) {
                throw new IOException("the bean class name is too large"); // 64k
            }
            //3. get the bin of bean
            Gson gson = new Gson();
            byte[] beanBin = gson.toJson(bean).getBytes(Utils.DEFAULT_CHARSET);
            //4. accumulate: | class length(4B) | name length(2B) | class | class name | bin |
            //               | len(txHash)      | len("__txHash__")| txHash | "__txHash__" | bin |
            StringBuilder sb = new StringBuilder();
            sb.append(ByteUtil.toHex(ByteUtil.intToByteArray(clazz.length)));
            sb.append(ByteUtil.toHex(ByteUtil.shortToBytes((short) clzName.length)));

            sb.append(ByteUtil.toHex(clazz));
            sb.append(ByteUtil.toHex(clzName));
            sb.append(ByteUtil.toHex(beanBin));
            return sb.toString();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get bvm invoke payload.
     *
     * @param methodName method name
     * @param params     invoke params
     * @return payload
     */
    public static String encodeBVM(String methodName, String... params) {
        int allLen = 0;
        allLen += 8;
        allLen += methodName.length();
        for (String param : params) {
            allLen += param.length();
            allLen += 4;
        }
        byte[] payload = new byte[allLen];

        int start = 0;
        System.arraycopy(ByteUtil.intToBytes(methodName.length()), 0, payload, start, 4);
        start += 4;
        System.arraycopy(methodName.getBytes(), 0, payload, start, methodName.getBytes().length);
        start += methodName.getBytes().length;
        System.arraycopy(ByteUtil.intToBytes(params.length), 0, payload, start, 4);
        start += 4;

        for (String param : params) {
            System.arraycopy(ByteUtil.intToBytes(param.getBytes().length), 0, payload, start, 4);
            start += 4;
            System.arraycopy(param.getBytes(), 0, payload, start, param.getBytes().length);
            start += param.getBytes().length;
        }

        return ByteUtil.toHex(payload);
    }

    /**
     * encode contract event to sha3 to get event topics.
     *
     * @param abi abi
     * @return event hash
     */
    public static HashMap<String, String> encodeEVMEvent(String abi) {
        HashMap<String, String> eventMap = new HashMap<String, String>();
        JsonArray abiArray = new JsonParser().parse(abi).getAsJsonArray();

        // get events abi prepare to decode the event
        for (int i = 0; i < abiArray.size(); i++) {
            //solve the event data in abi
            JsonObject methodBody = abiArray.get(i).getAsJsonObject();
            if (methodBody.has("name") && methodBody.get("type").getAsString().equals("event") && !methodBody.get("anonymous").getAsBoolean()) {
                String eventName = methodBody.get("name").getAsString();
                JsonArray list = methodBody.get("inputs").getAsJsonArray();
                StringBuilder sb = new StringBuilder("(");
                for (JsonElement object : list) {
                    JsonObject eventBody = object.getAsJsonObject();
                    sb.append(eventBody.get("type").getAsString()).append(",");
                }
                sb.setCharAt(sb.length() - 1, ')');
                String event = eventName + sb.toString();
                String eventId = "0x" + ByteUtil.toHex(HashUtil.sha3(event.getBytes()));
                eventMap.put(eventName, eventId);


            }
        }
        return eventMap;
    }
}
