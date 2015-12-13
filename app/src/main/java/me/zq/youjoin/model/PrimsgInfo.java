package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/6.
 */
public class PrimsgInfo {

    /**
     * result : success
     * message : [{"prismg_id":"9","sender_id":"5","receiver_id":"6","content":"啦啦啦","receive_date":"2015-12-12 03:32:40"},{"prismg_id":"10","sender_id":"5","receiver_id":"6","content":"啦啦啦","receive_date":"2015-12-12 03:33:10"},{"prismg_id":"11","sender_id":"5","receiver_id":"6","content":"啦啦啦","receive_date":"2015-12-12 03:33:13"},{"prismg_id":"12","sender_id":"5","receiver_id":"6","content":"啦啦啦","receive_date":"2015-12-12 03:33:15"},{"prismg_id":"13","sender_id":"6","receiver_id":"5","content":"啦啦啦","receive_date":"2015-12-12 03:33:23"},{"prismg_id":"14","sender_id":"6","receiver_id":"5","content":"啦啦啦","receive_date":"2015-12-12 03:33:26"},{"prismg_id":"15","sender_id":"6","receiver_id":"5","content":"啦啦啦","receive_date":"2015-12-12 03:33:27"},{"prismg_id":"26","sender_id":"6","receiver_id":"5","content":"啦啦啦","receive_date":"2015-12-12 03:46:44"}]
     */

    private String result;
    /**
     * prismg_id : 9
     * sender_id : 5
     * receiver_id : 6
     * content : 啦啦啦
     * receive_date : 2015-12-12 03:32:40
     */

    private List<MessageEntity> message;

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessage(List<MessageEntity> message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public List<MessageEntity> getMessage() {
        return message;
    }

    public static class MessageEntity {
        private int prismg_id;
        private int sender_id;
        private int receiver_id;
        private String content;
        private String receive_date;

        public void setPrismg_id(int prismg_id) {
            this.prismg_id = prismg_id;
        }

        public void setSender_id(int sender_id) {
            this.sender_id = sender_id;
        }

        public void setReceiver_id(int receiver_id) {
            this.receiver_id = receiver_id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setReceive_date(String receive_date) {
            this.receive_date = receive_date;
        }

        public int getPrismg_id() {
            return prismg_id;
        }

        public int getSender_id() {
            return sender_id;
        }

        public int getReceiver_id() {
            return receiver_id;
        }

        public String getContent() {
            return content;
        }

        public String getReceive_date() {
            return receive_date;
        }
    }
}
