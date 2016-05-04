package me.zq.youjoin;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import org.greenrobot.eventbus.EventBus;

import me.zq.youjoin.event.SigninSuccessEvent;
import me.zq.youjoin.imcloud.AVImClientManager;
import me.zq.youjoin.imcloud.MessageHandler;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;

/**
 * Created by ZQ on 2015/11/12.
 */
public class YouJoinApplication extends Application {
    public static float sScale;
    public static int sHeightPix;

    private static Context context;

    private static UserInfo currUser;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();

        NetworkManager.initialize(context);

        sScale = getResources().getDisplayMetrics().density;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;

        AVOSCloud.initialize(this, "hmUYX9LRCEa7Of6kQrDVrzes-gzGzoHsz", "NdwBtQEQOmhwftwXMt0I9vn4");
        AVIMClient.setOfflineMessagePush(true);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler());
    }

    public static Context getAppContext(){
        return context;
    }

    public static UserInfo getCurrUser() {
        return currUser;
    }

    public static void setCurrUser(UserInfo currUser) {
        YouJoinApplication.currUser = currUser;

        final String from = currUser.getUsername();
        AVImClientManager.getInstance().open(from, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if(e == null){
                    EventBus.getDefault().post(new SigninSuccessEvent());
                }
            }
        });
    }
}
