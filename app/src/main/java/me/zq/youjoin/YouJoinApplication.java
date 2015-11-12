package me.zq.youjoin;

import android.app.Application;
import android.content.Context;

import me.zq.youjoin.network.NetworkUtils;

/**
 * Created by ZQ on 2015/11/12.
 */
public class YouJoinApplication extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        NetworkUtils.initialize(context);
    }

    public static Context getAppContext(){
        return context;
    }



}
