package me.zq.youjoin.imcloud;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/4/20.
 */
public class AVImClientManager {
    private static AVImClientManager imClientManager;
    private AVIMClient avimClient;
    private String clientId;

    public static AVImClientManager getInstance(){
        if(imClientManager == null){
            synchronized (AVImClientManager.class){
                if(imClientManager == null){
                    imClientManager = new AVImClientManager();
                }
            }
        }
        return imClientManager;
    }

    private AVImClientManager(){}

    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(callback);
    }

    public AVIMClient getClient() {
        return avimClient;
    }

    public String getClientId() {
        if(TextUtils.isEmpty(clientId)){
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }
}
