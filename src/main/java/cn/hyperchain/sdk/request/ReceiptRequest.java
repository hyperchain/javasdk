package cn.hyperchain.sdk.request;

import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.exception.RequestExceptionCode;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.Response;

public class ReceiptRequest extends Request {

    public ReceiptRequest(String method, ProviderManager providerManager, Class clazz, int... nodeIds) {
        super(method, providerManager, clazz, nodeIds);
    }

//    @Override
//    public Response send() throws RequestException {
//        Response response = null;
//
//        try {
//            response = super.send();
//            return response;
//        } catch (RequestException e) {
//            if (!e.getCode().equals(RequestExceptionCode.RECEIPT_NOT_FOUND.getCode()) ||
//                    !e.getCode().equals(RequestExceptionCode.SYSTEM_BUSY.getCode()) ||
//                    !e.getCode().equals(RequestExceptionCode.HTTP_TIME_OUT.getCode()) ||
//                    !e.getCode().equals(RequestExceptionCode.NETWORK_GETBODY_FAILED.getCode()) ||
//                    !e.getCode().equals(RequestExceptionCode.REQUEST_ERROR.getCode())
//            ) {
//                return response;
//            }
//            throw e;
//        }
//    }
}
