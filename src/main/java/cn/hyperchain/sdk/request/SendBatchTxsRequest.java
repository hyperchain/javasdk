package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.response.TxHashesResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * this class represents sending batch transactions request.
 *
 * @author Lam
 * @ClassName SendBatchTxsRequest
 * @date 2019-07-11
 */
public class SendBatchTxsRequest extends Request {
    private ArrayList<Request> requests;
    private TxHashesResponse responses;
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * constructor.
     *
     * @param method          method
     * @param providerManager provider manager
     * @param clazz           class
     * @param requests        request
     * @param nodeIds         specific ids
     */
    public SendBatchTxsRequest(String method, ProviderManager providerManager, Class clazz, ArrayList<Request> requests, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
        this.requests = requests;
        this.responses = new TxHashesResponse();
    }

    @Override
    public Response send() throws RequestException {
        String result = this.providerManager.send(this, this.nodeIds);

        Type responsesType = new TypeToken<ArrayList<TxHashResponse>>() {
        }.getType();

        if (!result.startsWith("[")) {
            result = "[" + result + "]";
        }

        ArrayList<TxHashResponse> txHashResponses = gson.fromJson(result, responsesType);

        for (int i = 0; i < txHashResponses.size(); i++) {
            TxHashResponse txHashResponse = txHashResponses.get(i);
            txHashResponse.setTranRequest(this.requests.get(i));
            txHashResponse.setNodeIds(nodeIds);
            txHashResponse.setProviderManager(this.providerManager);
            responses.addResponse(txHashResponse);
        }

        return responses;
    }

    @Override
    public String requestBody() {
        return this.gson.toJson(requests);
    }
}
