package me.zq.youjoin.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.UpdateUserInfoResult;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.utils.Md5Utils;

/**
 * YouJoin
 * 网络管理类，封装网络操作接口
 * Created by ZQ on 2015/11/12.
 */
public class NetworkManager {

    /**
     * 网络接口相关常量
     */
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "user_password";
    public static final String EMAIL = "user_email";

    /**
     * 服务器接口URL
     */
//    public static final String BASE_API_URL = "http://192.168.0.103:8088/youjoin-server/controllers/";
    public static final String BASE_API_URL = "http://www.tekbroaden.com/youjoin-server/controllers/";
    public static final String API_SIGN_IN = BASE_API_URL + "signin.php";
    public static final String API_SIGN_UP = BASE_API_URL + "signup.php";
    public static final String API_UPDATE_USERINFO = BASE_API_URL + "update_userinfo.php";
    public static final String API_SEND_TWEET = BASE_API_URL + "send_tweet.php";
    public static final String API_REQUEST_USERINFO = BASE_API_URL + "request_userinfo.php";
    public static final String API_ADD_FRIEND = BASE_API_URL + "add_friend.php";
    public static final String API_SEND_MESSAGE = BASE_API_URL + "send_message.php";
    public static final String API_RECEIVE_MESSAGE = BASE_API_URL + "receive_message.php";
    public static final String API_COMMENT_TWEET = BASE_API_URL + "comment_tweet.php";
    public static final String API_UPVOTE_TWEET = BASE_API_URL + "upvote_tweet.php";

    private static RequestQueue mRequestQueue ;


    /**
     * 发送动态接口
     * @param listener ResponseListener
     */
    public static void postSendTweet(ResponseListener listener){

    }

    /**
     * 获取私信接口
     * @param listener ResponseListener
     */
    public static void postRequestMessage(ResponseListener listener){

    }

    /**
     * 发送私信接口
     * @param receiveUserId 接收方用户id
     * @param content 私信内容
     * @param listener ResponseListener
     */
    public static void postSendMessage(String receiveUserId, String content,
                                       ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("send_userid", YouJoinApplication.getCurrUser().getId());
        params.put("receive_userid", receiveUserId);
        params.put("message_content", content);
        Request request = new PostObjectRequest(
                API_SEND_MESSAGE,
                params, new TypeToken<ResultInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 添加好友接口
     * @param friendUserId 要添加的好友id
     * @param listener ResponseListener
     */
    public static void postAddFriend(String friendUserId,
                                     ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", YouJoinApplication.getCurrUser().getId());
        params.put("friend_id", friendUserId);
        Request request = new PostObjectRequest(
                API_ADD_FRIEND,
                params, new TypeToken<ResultInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 个人资料请求（下载）接口
     * @param userId   要获取的用户id
     * @param listener ResponseListener
     */
    public static void postRequestUserInfo(String userId, ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        Request request = new PostObjectRequest(
                API_REQUEST_USERINFO,
                params,new TypeToken<UserInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 个人资料更新（上传）接口
     * @param userInfo 用户实体类
     * @param picPath 头像的本地路径
     * @param listener ResponseListener
     */
    public static void postUpdateUserInfo(UserInfo userInfo, String picPath, ResponseListener listener){
        if(userInfo.getId() == null) return;

        List<ImageInfo> imageList = new ArrayList<>();
        imageList.add(new ImageInfo(picPath));
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userInfo.getId());
        params.put("user_work", userInfo.getWork());
        params.put("user_location", userInfo.getLocation());
        params.put("user_sex", userInfo.getSex());
        params.put("user_birth", userInfo.getBirth());
        params.put("user_sign", userInfo.getUsersign());
        Request request = new PostUploadRequest(API_UPDATE_USERINFO, imageList, params,
                new TypeToken<UpdateUserInfoResult>(){}.getType(), listener);
        NetworkManager.getRequestQueue().add(request);
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
}