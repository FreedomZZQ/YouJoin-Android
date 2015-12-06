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
import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.UpdateUserInfoResult;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.Md5Utils;
import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin
 * 网络管理类，封装网络操作接口
 * Created by ZQ on 2015/11/12.
 */
public class NetworkManager {

    /**
     * 网络接口相关常量
     */
    public static final String USER_USERNAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_ID = "user_id";
    public static final String USER_WORK = "user_work";
    public static final String USER_BIRTH = "user_birth";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_LOCATION = "user_location";
    public static final String USER_SEX = "user_sex";

    public static final String TWEETS_CONTNET = "tweets_content";

    public static final String FRIEND_ID = "friend_id";

    public static final String PARAM_TYPE = "type";
    public static final String PARAM = "param";

    public static final String SEND_USERID = "send_userid";
    public static final String RECEIVE_USERID = "receive_userid";
    public static final String MESSAGE_CONTENT = "message_content";
    public static final String FROM_TIME = "from_time";
    public static final String TO_TIME = "to_time";


    /**
     * 服务器接口URL
     */
//    public static final String BASE_API_URL = "http://192.168.0.103:8088/youjoin-server/controllers/";
//    public static final String BASE_API_URL = "http://www.tekbroaden.com/youjoin-server/controllers/";
    public static final String BASE_API_URL = "http://110.65.7.55:8088/youjoin-server/controllers/";
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

    public static final String TAG = "YouJoin_Network";


    /**
     * 发送动态接口
     * @param listener ResponseListener
     */
    public static void postSendTweet(String content, List<ImageInfo> images,
                                     ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, YouJoinApplication.getCurrUser().getId());
        params.put(TWEETS_CONTNET, content);
        Request request = new PostUploadRequest(API_SEND_TWEET, images, params,
                new TypeToken<ResultInfo>(){}.getType(), listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 获取私信接口
     * @param listener ResponseListener
     */
    public static void postRequestMessage(String fromTime, String toTime, ResponseListener listener){
        Map<String, String> params = new HashMap<>();
//        params.put(USER_ID, YouJoinApplication.getCurrUser().getId());
        params.put(USER_ID, "4"); //for test
        params.put(FROM_TIME, fromTime);
        params.put(TO_TIME, toTime);
        Request request = new PostObjectRequest(
                API_RECEIVE_MESSAGE,
                params, new TypeToken<PrimsgInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
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
//        params.put(SEND_USERID, YouJoinApplication.getCurrUser().getId());
        params.put(SEND_USERID, "3"); //for test
        params.put(RECEIVE_USERID, receiveUserId);
        params.put(MESSAGE_CONTENT, content);
        Request request = new PostObjectRequest(
                API_SEND_MESSAGE,
                params, new TypeToken<ResultInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 添加好友接口
     * @param param 获取用户资料的参数，收到后自动判断并填充type。type取值1表示id，2表示name，3表示email)
     * @param listener ResponseListener
     */
    public static void postAddFriend(String param,
                                     ResponseListener listener){
        String type = StringUtils.getParamType(param);
        if(type.equals("invalid")){
            LogUtils.e(TAG, "param invalid!");
            return;
        }
        Map<String, String> params = new HashMap<>();
//        params.put(USER_ID, YouJoinApplication.getCurrUser().getId());
        params.put(USER_ID, "6"); //for test
        params.put(PARAM, param);
        params.put(PARAM_TYPE, type);
        Request request = new PostObjectRequest(
                API_ADD_FRIEND,
                params, new TypeToken<ResultInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 个人资料请求（下载）接口
     * @param param   获取用户资料的参数，收到后自动判断并填充type。type取值1表示id，2表示name，3表示email)
     * @param listener ResponseListener
     */
    public static void postRequestUserInfo(String param, ResponseListener listener){
        String type = StringUtils.getParamType(param);
        if(type.equals("invalid")) {
            LogUtils.e(TAG, "param invalid!");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(PARAM, param);
        params.put(PARAM_TYPE, type);
        Request request = new PostObjectRequest(
                API_REQUEST_USERINFO,
                params,new TypeToken<UserInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

//    public static void postRequestUserInfo(String param, ResponseListener listener){
//        String type = StringUtils.getParamType(param);
//        if(type.equals("invalid")) {
//            LogUtils.e(TAG, "param invalid!");
//            return;
//        }
//        Map<String, String> params = new HashMap<>();
//        params.put(USER_ID, param);
//        Request request = new PostObjectRequest(
//                API_REQUEST_USERINFO,
//                params,new TypeToken<UserInfo>(){}.getType(),
//                listener);
//        NetworkManager.getRequestQueue().add(request);
//    }

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
        params.put(USER_ID, userInfo.getId());
        params.put(USER_WORK, userInfo.getWork());
        params.put(USER_LOCATION, userInfo.getLocation());
        params.put(USER_SEX, userInfo.getSex());
        params.put(USER_BIRTH, userInfo.getBirth());
        params.put(USER_SIGN, userInfo.getUsersign());
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
        param.put(USER_USERNAME, username);
        param.put(USER_PASSWORD, Md5Utils.getMd5(password));
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
        param.put(USER_USERNAME, username);
        param.put(USER_PASSWORD, Md5Utils.getMd5(password));
        param.put(USER_EMAIL, email);
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