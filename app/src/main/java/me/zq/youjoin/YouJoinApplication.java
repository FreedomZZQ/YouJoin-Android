package me.zq.youjoin;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getAppContext(){
        return context;
    }

    public static UserInfo getCurrUser() {
        return currUser;
    }

    public static void setCurrUser(UserInfo currUser) {
        YouJoinApplication.currUser = currUser;
    }
}
