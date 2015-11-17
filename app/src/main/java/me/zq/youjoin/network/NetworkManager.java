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
import me.zq.youjoin.utils.Md5Utils;

/**
 * YouJoin
 * Created by ZQ on 2015/11/12.
 */
public class NetworkManager {

    /**
     * 网络接口相关常量
     */
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "user_password";
    public static final String EMAIL = "user_email";
//    public static final String BASE_API_URL = "http://192.168.0.102:8088/youjoin-server/controllers/";
    public static final String BASE_API_URL = "http://www.tekbroaden.com/youjoin-server/controllers/";

    /**
     * 网络接口URL
     */
    public static final String API_SIGN_IN = BASE_API_URL + "signin.php";
    public static final String API_SIGN_UP = BASE_API_URL + "signup.php";

    private static RequestQueue mRequestQueue ;

    /**初始化Volley 使用OkHttpStack
     * @param context 用作初始化Volley RequestQueue
     */
    public static synchronized void initialize(Context context){
        if (mRequestQueue == null){
            synchronized (NetworkManager.class){
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
     * @param username 登录用户名
     * @param password 登陆密码
     * @param listener ResponseListener
     */
    public static void postSignIn(String username, String password,
                                  ResponseListener listener){
        Map<String, String> param = new HashMap<>();
        param.put(USERNAME, username);
        param.put(PASSWORD, Md5Utils.getMd5(password));
        Request request = new PostObjectRequest(
                API_SIGN_IN,
                param,
                new TypeToken<UserInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 注册接口
     * @param username 注册用户名
     * @param password 注册密码
     * @param email 注册邮箱
     * @param listener ResponseListener
     */
    public static void postSignUp(String username, String password, String email,
                                  ResponseListener listener){
        Map<String, String> param = new HashMap<>();
        param.put(USERNAME, username);
        param.put(PASSWORD, Md5Utils.getMd5(password));
        param.put(EMAIL, email);
        Request request = new PostObjectRequest(
                API_SIGN_UP,
                param,
                new TypeToken<UserInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }
}