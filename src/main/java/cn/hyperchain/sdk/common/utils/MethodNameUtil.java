package cn.hyperchain.sdk.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameUtil {
    /**
     * get normalized method name,
     * for example:
     * if we have "testIntAndUint(int,uint,uint256,int[2])" for input,
     * we will get "testIntAndUint(int256,uint256,uint256,int256[2])" for output
     * @param methodName
     * @return normalized methodName
     */
    public static String getNormalizedMethodName(String methodName) {
        int firstBracketIndex = methodName.indexOf("(");
        String stringPrefix = methodName.substring(0,firstBracketIndex);
        Pattern p = Pattern.compile("int[^0-9]");
        Matcher m = p.matcher(methodName.substring(firstBracketIndex));
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            m.appendReplacement(sb, "int256" + m.group().substring(3));
        }
        m.appendTail(sb);
        return stringPrefix + sb.toString();
    }
}
