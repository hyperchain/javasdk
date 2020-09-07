package cn.hyperchain.sdk.response;

import cn.hyperchain.sdk.exception.RequestException;

import java.util.ArrayList;

/**
 * this classes represents tx hashes response.
 *
 * @author Lam
 * @ClassName TxHashesResponse
 * @date 2019-07-11
 */
public class TxHashesResponse extends Response {
    private String method;
    private ArrayList<TxHashResponse> responses;

    public TxHashesResponse() {
        responses = new ArrayList<>();
    }

    public ArrayList<TxHashResponse> getResponses() {
        return responses;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void addResponse(TxHashResponse response) {
        responses.add(response);
    }

    /**
     * polling to get receipt.
     *
     * @return {@link ArrayList} of {@link ReceiptResponse}
     * @throws RequestException may throw exception
     */
    public ArrayList<ReceiptResponse> polling() throws RequestException {
        ArrayList<ReceiptResponse> receiptResponses = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).code == 0) {
                ReceiptResponse receiptResponse = responses.get(i).polling();
                receiptResponses.add(receiptResponse);
            } else {
                Response response = (Response) responses.get(i);
                ReceiptResponse receiptResponse = new ReceiptResponse(response, null);
                receiptResponses.add(receiptResponse);
            }
        }
        return receiptResponses;
    }
}
