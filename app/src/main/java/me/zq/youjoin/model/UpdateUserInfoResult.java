package me.zq.youjoin.model;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/20.
 */
public class UpdateUserInfoResult {

    String result;
    /**
     * img_url : http://localhost/youjoin/youjoin_server/upload/1/20151121101522_482f42c5fa484a8abe99ec527fb59b38.jpg
     */

    private String img_url;


    public String getResult() {
        return result;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
