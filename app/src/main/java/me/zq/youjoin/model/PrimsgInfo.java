package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/6.
 */
public class PrimsgInfo {
    /**
     * num : 3
     * result : success
     * messege : [{"id":"3","name":"zzq","content":"first msg"},{"id":"3","name":"zzq","content":"first msg"},{"id":"3","name":"zzq","content":"first msg"}]
     */

    private int num;
    private String result;
    /**
     * id : 3
     * name : zzq
     * content : first msg
     */

    private List<MessegeEntity> messege;

    public void setNum(int num) {
        this.num = num;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessege(List<MessegeEntity> messege) {
        this.messege = messege;
    }

    public int getNum() {
        return num;
    }

    public String getResult() {
        return result;
    }

    public List<MessegeEntity> getMessege() {
        return messege;
    }

    public static class MessegeEntity {
        private String id;
        private String name;
        private String content;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }
    }
}
