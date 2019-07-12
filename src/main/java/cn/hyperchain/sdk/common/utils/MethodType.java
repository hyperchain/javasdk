package cn.hyperchain.sdk.common.utils;

/**
 * this class is created for sending batch transactions.
 *
 * @author Lam
 * @ClassName MethodType
 * @date 2019-07-13
 */
public enum MethodType {
    SEND_TRANSACTION("tx_sendTransaction"),
    DEPLOY_CONTRACT("contract_deployContract"),
    INVOKE_CONTRACT("contract_invokeContract"),
    MAINTAIN_CONTRACT("contract_maintainContract");

    String method;

    MethodType(String method) {
        this.method = method;
    }

    /**
     * get method type of corresponding method name.
     *
     * @param methodName method name
     * @return {@link MethodType}
     */
    public static MethodType methodType(String methodName) {
        switch (methodName) {
            case "tx_sendTransaction":
                return SEND_TRANSACTION;
            case "contract_deployContract":
                return DEPLOY_CONTRACT;
            case "contract_invokeContract":
                return INVOKE_CONTRACT;
            case "contract_maintainContract":
                return MAINTAIN_CONTRACT;
            default:
                return null;
        }
    }

    public String getMethod() {
        return method;
    }
}
