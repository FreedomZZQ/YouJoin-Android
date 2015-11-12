package me.zq.youjoin.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

import me.zq.youjoin.model.UserInfo;

/**
 * YouJoin
 * Created by ZQ on 2015/11/12.
 */
public class NetworkUtils {

    /**
     * 网络接口相关常量
     */
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "user_password";


    /**
     * 网络接口URL
     */
    public static final String URL_SIGN_IN =
            "http://192.168.0.102:8088/youjoin-server/Controllers/signin.php";
    public static final String URL_SIGN_UP =
            "http://192.168.0.102:8088/youjoin-server/Controllers/signup.php";


    private static RequestQueue mRequestQueue ;

    /**初始化Volley 使用OkHttpStack
     * @param context 用作初始化Volley RequestQueue
     */
    public static synchronized void initialize(Context context){
        if (mRequestQueue == null){
            synchronized (NetworkUtils.class){
                if (mRequestQueue == null){
                    mRequestQueue =
                            Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
                }
            }
        }
        mRequestQueue.start();
    }


    /**获取RequestQueue实例
     * @return 返回RequestQueue实例
     */
    public static RequestQueue getRequestQueue(){
        if (mRequestQueue == null)
            throw new RuntimeException("请先初始化mRequestQueue") ;
        return mRequestQueue ;
    }

    /**
     * 登陆接口
     * @param listener ResponseListener
     * @param username 登录用户名
     * @param password 登陆密码
     */
    public static void postSignIn(ResponseListener listener,
                                  String username, String password){
        Map<String, String> param = new HashMap<>();
        param.put(USERNAME, username);
        param.put(PASSWORD, password);
        Request request = new PostObjectRequest(
                URL_SIGN_IN,
                param,
                new TypeToken<UserInfo>(){}.getType(),
                listener);
        NetworkUtils.getRequestQueue().add(request);
    }
}