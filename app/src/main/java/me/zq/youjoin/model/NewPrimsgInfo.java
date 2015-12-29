package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/29.
 */
public class NewPrimsgInfo {
    /**
     * result : success
     * message : ["16","16"]
     */

    private String result;
    private List<Integer> message;

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessage(List<Integer> message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public List<Integer> getMessage() {
        return message;
    }
}
