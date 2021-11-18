package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.common.hvm.HVMBeanAbi;
import cn.hyperchain.sdk.common.hvm.HVMType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * this class is used to build param for directly invoke hvm contract by abi .
 *
 * @author houcc
 */
public class InvokeHVMAbiParams {

    private String params;

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public static class ParamBuilder {
        private Gson gson = new Gson();
        private InvokeHVMAbiParams invokeParams;
        private HVMBeanAbi beanAbi;
        private List<InputParam> inputsList;

        /**
         * create params instance.
         *
         * @param hvmAbiJson the hvm contract abi of json
         * @param beanType   the type of beanName
         * @param beanName   name of beanName you want to invoke
         */
        public ParamBuilder(String hvmAbiJson, HVMBeanAbi.BeanType beanType, String beanName) {
            try {
                List<HVMBeanAbi> beanAbiList = gson.fromJson(hvmAbiJson, new TypeToken<List<HVMBeanAbi>>() {
                }.getType());
                beanAbi = getBeanAbi(beanAbiList, beanType, beanName);
                byte[] classBytes = ByteUtil.fromHex(beanAbi.getClassBytes());
                if (classBytes.length > 0xffff) {
                    throw new IOException("the bean class is too large"); // 64k
                }
                invokeParams = new InvokeHVMAbiParams();
                inputsList = Lists.newArrayList();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /**
         * get target hvmBeanAbi by beanName, only support invokeBean.
         *
         * @param abiList  the input bean-abi list
         * @param beanType type of beanName
         * @param beanName name of beanName you want to invoke
         * @return HVMBeanAbi instance
         */
        private HVMBeanAbi getBeanAbi(List<HVMBeanAbi> abiList, HVMBeanAbi.BeanType beanType, String beanName) {
            for (HVMBeanAbi hvmBeanAbi : abiList) {
                if (hvmBeanAbi.getBeanName().equals(beanName)
                        && hvmBeanAbi.getBeanType().equals(beanType)) {
                    return hvmBeanAbi;
                }
            }
            throw new RuntimeException("can not find target beanName of " + beanName);
        }

        /**
         * add common type param , not support parameterized object.
         *
         * @param value input value
         * @return {@link ParamBuilder}
         */
        public ParamBuilder addParam(Object value) {
            Class<?> clazz = value.getClass();
            if (Boolean.class.isAssignableFrom(clazz)
                    || Byte.class.isAssignableFrom(clazz)
                    || Short.class.isAssignableFrom(clazz)
                    || Integer.class.isAssignableFrom(clazz)
                    || Long.class.isAssignableFrom(clazz)
                    || Double.class.isAssignableFrom(clazz)
                    || Float.class.isAssignableFrom(clazz)) {
                inputsList.add(new InputParam(clazz.getName(), String.valueOf(value)));
                return this;
            }

            if (Character.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz)) {
                if (beanAbi.getBeanType() == HVMBeanAbi.BeanType.InvokeBean) {
                    inputsList.add(new InputParam(clazz.getName(), "\"" + value + "\""));
                } else {
                    inputsList.add(new InputParam(clazz.getName(), String.valueOf(value)));
                }
                return this;
            }
            //not support parameterized type
            if (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
                throw new RuntimeException(" addParam method not support add Parameterized object," +
                        " please use addParamizedObject method ");
            }
            // common object
            String param = gson.toJson(value);
            inputsList.add(new InputParam(clazz.getName(), param));
            return this;
        }

        /**
         * this method if for add generic object.
         *
         * @param classes index 0 for collection
         * @param obj     the argument you want to pass to contract method invoke
         * @return {@link ParamBuilder}
         */
        public ParamBuilder addParamizedObject(Class<?>[] classes, Object obj) {
            if (classes.length <= 1) {
                throw new RuntimeException("can not detect generic type");
            } else if (!(Map.class.isAssignableFrom(classes[0]) || Collection.class.isAssignableFrom(classes[0]))) {
                throw new RuntimeException(classes[0].getName() + " does not implements Collection or Map interface, we can only accpect class which is subclass of interface Map or Collection!");
            }

            StringBuilder paramizedTypeSB = new StringBuilder();
            int paramNum = 1;
            if (Map.class.isAssignableFrom(classes[0])) {
                paramNum += 2;
            } else {
                paramNum += 1;
            }

            paramizedTypeSB.append(classes[0].getName() + "<");
            for (int i = 1; i < paramNum; i++) {
                paramizedTypeSB.append(classes[i].getName());
                if (i < paramNum - 1) {
                    paramizedTypeSB.append(", ");
                }
            }
            paramizedTypeSB.append(">");
            String paramizedTypeName = paramizedTypeSB.toString();

            inputsList.add(new InputParam(paramizedTypeName, gson.toJson(obj)));
            return this;
        }

        /**
         * build param instance.
         *
         * @return {@link ParamBuilder}
         */
        public InvokeHVMAbiParams build() {
            if (beanAbi.getInputs().size() != inputsList.size()) {
                throw new RuntimeException("param count is not match with input");
            }
            checkParamTypeMatch();
            String payload;
            if (beanAbi.getBeanType() == HVMBeanAbi.BeanType.InvokeBean) {
                payload = buildInvokeBeanPayload();
            } else {
                payload = buildMethodBeanPayload();
            }
            invokeParams.setParams(payload);
            return invokeParams;
        }

        /**
         * check input param’s type is match with target input type.
         */
        private void checkParamTypeMatch() {
            for (int i = 0; i < beanAbi.getInputs().size(); i++) {
                HVMBeanAbi.Entry entry = beanAbi.getInputs().get(i);
                InputParam inputParam = inputsList.get(i);
                if (StringUtils.isNotEmpty(entry.getStructName())
                        && !HVMType.checkTypeMatch(entry.getType(), inputParam.getClazzName())) {
                    throw new RuntimeException(
                            String.format("the param[%d] type  not match , require %s but find %s",
                                    i, entry.getStructName(), inputParam.getClazzName())
                    );
                }
            }
        }

        /**
         * build methodBean payload.
         *
         * @return methodBean payload value
         */
        private String buildMethodBeanPayload() {
            StringBuilder sb = new StringBuilder();
            byte[] beanNameBytes = beanAbi.getBeanName().getBytes();
            sb.append(InvokeDirectlyParams.ParamBuilder.MAGIC);
            sb.append(ByteUtil.toHex(ByteUtil.shortToBytes((short) beanNameBytes.length)));
            sb.append(ByteUtil.toHex(beanNameBytes));
            for (InputParam input : inputsList) {
                byte[] clazzNameBytes = input.getClazzName().getBytes();
                byte[] paramBytes = input.getParam().getBytes();
                sb.append(ByteUtil.toHex(ByteUtil.shortToBytes((short) clazzNameBytes.length)));
                sb.append(ByteUtil.toHex(ByteUtil.intToBytes(paramBytes.length)));
                sb.append(ByteUtil.toHex(clazzNameBytes));
                sb.append(ByteUtil.toHex(paramBytes));
            }
            return sb.toString();
        }


        /**
         * build invokeBean payload.
         *
         * @return invokeBean payload value
         */
        private String buildInvokeBeanPayload() {
            String paramJson = toJson();
            StringBuilder sb = new StringBuilder();
            byte[] classBytes = ByteUtil.fromHex(beanAbi.getClassBytes());
            byte[] beanNameBytes = beanAbi.getBeanName().getBytes();
            sb.append(ByteUtil.toHex(ByteUtil.intToByteArray(classBytes.length)));
            sb.append(ByteUtil.toHex(ByteUtil.shortToBytes((short) beanNameBytes.length)));
            sb.append(ByteUtil.toHex(classBytes));
            sb.append(ByteUtil.toHex(beanNameBytes));
            sb.append(ByteUtil.toHex(paramJson.getBytes()));
            return sb.toString();
        }

        /**
         * convert to json value.
         *
         * @return json string
         */
        private String toJson() {
            StringBuilder builder = new StringBuilder("{");
            for (int i = 0; i < beanAbi.getInputs().size(); i++) {
                HVMBeanAbi.Entry entry = beanAbi.getInputs().get(i);
                builder.append("\"").append(entry.getName()).append("\":");
                builder.append(inputsList.get(i).getParam());
                builder.append(",");
            }
            if (builder.toString().endsWith(",")) {
                builder.deleteCharAt(builder.length() - 1);
            }
            builder.append("}");
            return builder.toString();
        }
    }

    private static class InputParam {
        private String clazzName;
        private String param;

        public InputParam(String clazzName, String param) {
            this.clazzName = clazzName;
            this.param = param;
        }

        public String getClazzName() {
            return clazzName;
        }

        public void setClazzName(String clazzName) {
            this.clazzName = clazzName;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }

}
