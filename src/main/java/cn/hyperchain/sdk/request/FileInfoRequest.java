package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.filemgr.FileUpdateResponse;
import cn.hyperchain.sdk.transaction.Transaction;

public class FileInfoRequest extends Request {
    public FileInfoRequest(String method, ProviderManager providerManager, Class clazz, String jsonRpc, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
        this.setJsonrpc(jsonRpc);
    }

    public FileInfoRequest(String method, ProviderManager providerManager, Class clazz, String jsonRpc, Transaction transaction, int... nodeIds) {
        super(method, providerManager, clazz, transaction, nodeIds);
        this.setJsonrpc(jsonRpc);
    }

    @Override
    public Response send() throws RequestException {
        Response response = super.send();
        if (response instanceof FileUpdateResponse) {
            FileUpdateResponse fileUpdateResponse = (FileUpdateResponse) response;
            fileUpdateResponse.setTranRequest(this);
            fileUpdateResponse.setNodeIds(this.nodeIds);
            fileUpdateResponse.setProviderManager(this.providerManager);
            return fileUpdateResponse;
        }
        return response;
    }
}
