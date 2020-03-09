package cn.hyperchain.sdk.common.utils;

import java.util.Set;

public class HVMPayload {
    private String invokeBeanName;
    private String invokeArgs;
    private Set<String> invokeMethods;

    /**
     * generate a HVMPayload.
     *
     * @param invokeBeanName invoke bean name
     * @param invokeArgs invoke args
     * @param invokeMethods invoke bean call methods
     */
    public HVMPayload(String invokeBeanName, String invokeArgs, Set<String> invokeMethods) {
        this.invokeBeanName = invokeBeanName;
        this.invokeArgs = invokeArgs;
        this.invokeMethods = invokeMethods;
    }

    public String getInvokeBeanName() {
        return invokeBeanName;
    }

    public String getInvokeArgs() {
        return invokeArgs;
    }

    public Set<String> getInvokeMethods() {
        return invokeMethods;
    }
}
