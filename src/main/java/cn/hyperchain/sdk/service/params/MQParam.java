package cn.hyperchain.sdk.service.params;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MQParam {
    private Map<String, Object> metas;

    MQParam() {
        this.metas = new HashMap<>();
        // queue info
        this.metas.put("routingKeys", new ArrayList<String>());
        this.metas.put("queueName", "");
        // registrant info
        this.metas.put("from", "");
        this.metas.put("signature", "");
        // blockEv related
        this.metas.put("isVerbose", false);
        // logEv related
        this.metas.put("fromBlock", "");
        this.metas.put("toBlock", "");
        this.metas.put("addresses", new ArrayList<String>());
        this.metas.put("topics", new ArrayList<String[]>());
        // delay push
        this.metas.put("delay", false);
    }

    public Map<String, Object> getMetas() {
        return this.metas;
    }


    @SuppressWarnings("uncheck")
    public static class Builder {
        private MQParam param;

        public Builder() {
            param = new MQParam();
        }

        /**
         * set queue name.
         *
         * @param qName queue name
         * @return Builder instance
         */
        public Builder queueName(String qName) {
            param.metas.put("queueName", qName);
            return this;
        }

        /**
         * set msg types.
         *
         * @param msgTypes msg types
         * @return Builder instance
         */
        public Builder msgTypes(ArrayList<String> msgTypes) {
            param.metas.put("routingKeys", msgTypes);
            return this;
        }

        /**
         * set registrant.
         *
         * @param from registrant address
         * @return Builder instance
         */
        public Builder registrant(String from) {
            param.metas.put("from", from);
            return this;
        }

        /**
         * set delay mode
         *
         * @param delay if delay is true, the correctness and order of the messages are guaranteed,
         *              but block and log message won't be pushed until checkpoint happen
         * @return
         */
        public Builder delayMode(boolean delay) {
            param.metas.put("delay", delay);
            return this;
        }

        /**
         * set block verbose.
         *
         * @param isVerbose block is verbose or not
         * @return Builder instance
         */
        public Builder blockVerbose(boolean isVerbose) {
            param.metas.put("isVerbose", isVerbose);
            return this;
        }

        /**
         * set log from block.
         *
         * @param from from block
         * @return Builder instance
         */
        public Builder logFromBlock(String from) {
            param.metas.put("fromBlock", from);
            return this;
        }

        /**
         * set log to block.
         *
         * @param to to block
         * @return Builder instance
         */
        public Builder logToBlock(String to) {
            param.metas.put("toBlock", to);
            return this;
        }

        /**
         * set log address.
         *
         * @param address log address
         * @return Builder instance
         */
        public Builder logAddress(String address) {
            List<String> addresses = (List<String>) param.metas.get("addresses");
            if (addresses != null) {
                addresses.add(address);
            } else {
                addresses = new ArrayList<String>();
                addresses.add(address);
                param.metas.put("addresses", addresses);
            }
            return this;
        }

        /**
         * set log topics.
         *
         * @param topic log topic
         * @return Builder instance
         */
        public Builder logTopics(String[] topic) {
            List<String[]> topics = (List<String[]>) param.metas.get("topics");
            if (topics != null) {
                topics.add(topic);
            } else {
                topics = new ArrayList<String[]>();
                topics.add(topic);
                param.metas.put("topics", topics);
            }
            return this;
        }

        /**
         * build mq param.
         *
         * @return MQParam instance
         */
        public MQParam build() {
            return param;
        }
    }

}