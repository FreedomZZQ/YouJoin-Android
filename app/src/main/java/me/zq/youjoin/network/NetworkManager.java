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
import me.zq.youjoin.model.CommentInfo;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.model.NewPrimsgInfo;
import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.TweetInfo;
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
    public static final String USER_NICKNAME = "user_nickname";

    public static final String TWEETS_CONTNET = "tweets_content";
    public static final String TWEET_ID = "tweet_id";
    public static final String TWEET_COMMENT = "comment_content";
    public static final int UPVOTE_STATUS_YES = 1;
    public static final int UPVOTE_STATUS_NO = 0;

    public static final String FRIEND_ID = "friend_id";

    public static final String PARAM_TYPE = "type";
    public static final String PARAM = "param";
    public static final String PARAM_TYPE_USER_ID = "1";
    public static final String PARAM_TYPE_USER_NAME = "2";
    public static final String PARAM_TYPE_USER_EMAIL = "3";

    public static final String SEND_USERID = "send_userid";
    public static final String RECEIVE_USERID = "receive_userid";
    public static final String MESSAGE_CONTENT = "message_content";
    public static final String TIME_OLD = "0";
    public static final String TIME_NEW = "1";
    public static final String TIME_TYPE = "time";

    public static final String PRIMSG_ID = "primsg_id";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String FAILURE_SERVER_BUSY = "";

    /**
     * 服务器接口URL
     */
    public static final String BASE_API_URL = "http://192.168.0.102:8088/youjoin-server/controllers/";
//    public static final String BASE_API_URL = "http://www.tekbroaden.com/youjoin-server/controllers/";
//    public static final String BASE_API_URL = "http://110.65.7.154:8088/youjoin-server/controllers/";
    public static final String API_SIGN_IN = BASE_API_URL + "signin.php";
    public static final String API_SIGN_UP = BASE_API_URL + "signup.php";
    public static final String API_UPDATE_USERINFO = BASE_API_URL + "update_userinfo.php";
    public static final String API_REQUEST_USERINFO = BASE_API_URL + "request_userinfo.php";
    public static final String API_ADD_FRIEND = BASE_API_URL + "add_friend.php";
    public static final String API_SEND_MESSAGE = BASE_API_URL + "send_message.php";
    public static final String API_RECEIVE_MESSAGE = BASE_API_URL + "receive_message.php";
    public static final String API_SEND_TWEET = BASE_API_URL + "send_tweet.php";
    public static final String API_REQUEST_TWEETS = BASE_API_URL + "get_tweets.php";
    public static final String API_COMMENT_TWEET = BASE_API_URL + "comment_tweet.php";
    public static final String API_UPVOTE_TWEET = BASE_API_URL + "upvote_tweet.php";
    public static final String API_REQUEST_FRIEND_LIST = BASE_API_URL + "get_friendlist.php";
    public static final String API_REQUEST_COMMENTS = BASE_API_URL + "get_comments.php";
    public static final String API_REQUEST_PRIMSG_LOG = BASE_API_URL + "chat_log.php";

    private static RequestQueue mRequestQueue ;

    public static final String TAG = "YouJoin_Network";

    /**获取与某用户的私信列表
     * @param userId 当前用户id
     * @param friendId 对方id
     * @param type 时间类型，1表示比primsgId新，0表示比较旧
     * @param primsgId 基准私信id
     * @param listener ResponseListener
     */
    public static void postRequestPrimsg(String userId, String friendId, String type, String primsgId,
                                         ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, userId);
        params.put(FRIEND_ID, friendId);
        params.put(TIME_TYPE, type);
        params.put(PRIMSG_ID, primsgId);
        Request request = new PostObjectRequest(API_REQUEST_PRIMSG_LOG,
                params, new TypeToken<PrimsgInfo>(){}.getType(), listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**获取某条动态的评论列表
     * @param tweetId 该条动态的id
     * @param listener ResponseListener
     */
    public static void postRequestComments(String tweetId, ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(TWEET_ID, tweetId);
        Request request = new PostObjectRequest(API_REQUEST_COMMENTS,
                params, new TypeToken<CommentInfo>(){}.getType(), listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**获取当前用户的关注列表
     * @param userId 当前登录用户id
     * @param listener ResponseListener
     */
    public static void postRequestFriendList(String userId, ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, userId);
        Request request = new PostObjectRequest(API_REQUEST_FRIEND_LIST,
                params, new TypeToken<FriendsInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 动态评论接口
     * @param userId 当前登录用户id
     * @param tweetId 所评论的动态id
     * @param commentContent 评论内容
     * @param listener ResponseListener
     */
    public static void postCommentTweet(String userId, String tweetId, String commentContent,
                                        ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, userId);
        params.put(TWEET_ID, tweetId);
        params.put(TWEET_COMMENT, commentContent);
        Request request = new PostObjectRequest(API_COMMENT_TWEET,
                params, new TypeToken<ResultInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 动态点赞接口
     * @param userId 当前登录用户id
     * @param tweetId 所点赞的动态id
     * @param listener ResponseListener
     */
    public static void postUpvoteTweet(String userId, String tweetId, ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, userId);
        params.put(TWEET_ID, tweetId);
        Request request = new PostObjectRequest(API_UPVOTE_TWEET,
                params, new TypeToken<ResultInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }


    /**
     * 获取动态列表接口
     * @param userId 当前登录用户id
     * @param tweetId 基准动态id（以此条动态作为基准，向前或向后获取）
     * @param listener ResponseListener
     */
    public static void postRequestTweets(String userId, String tweetId, String timeType,
                                         ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(TWEET_ID, tweetId);
        params.put(USER_ID, userId);
        params.put(TIME_TYPE, timeType);
        Request request = new PostObjectRequest(API_REQUEST_TWEETS,
                params, new TypeToken<TweetInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }


    /**
     * 发送动态接口
     * @param listener ResponseListener
     */
    public static void postSendTweet(String userId, String content, List<ImageInfo> images,
                                     ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, userId);
        params.put(TWEETS_CONTNET, content);
        Request request = new PostUploadRequest(API_SEND_TWEET, images, params,
                new TypeToken<ResultInfo>(){}.getType(), listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 获取私信接口
     * @param listener ResponseListener
     */
    public static void postRequestMessage(String userId, ResponseListener listener){
        Map<String, String> params = new HashMap<>();
//        params.put(USER_ID, YouJoinApplication.getCurrUser().getId());
        params.put(USER_ID, userId);
//        params.put(FROM_TIME, fromTime);
//        params.put(TO_TIME, toTime);
        Request request = new PostObjectRequest(
                API_RECEIVE_MESSAGE,
                params, new TypeToken<NewPrimsgInfo>(){}.getType(),
                listener);
        NetworkManager.getRequestQueue().add(request);
    }

    /**
     * 发送私信接口
     * @param receiveUserId 接收方用户id
     * @param content 私信内容
     * @param listener ResponseListener
     */
    public static void postSendMessage(String userId, String receiveUserId, String content,
                                       ResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put(SEND_USERID, userId);
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
     * @param userId 当前用户id
     * @param param 获取用户资料的参数，收到后自动判断并填充type。type取值1表示id，2表示name，3表示email)
     * @param listener ResponseListener
     */
    public static void postAddFriend(String userId, String param,
                                     ResponseListener listener){
        String type = StringUtils.getParamType(param);
        if(type.equals("invalid")){
            LogUtils.e(TAG, "param invalid!");
            return;
        }
        Map<String, String> params = new HashMap<>();
//        params.put(USER_ID, YouJoinApplication.getCurrUser().getId());
        params.put(USER_ID, userId);
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

    /**
     * 个人资料更新（上传）接口
     * @param userInfo 用户实体类
     * @param picPath 头像的本地路径
     * @param listener ResponseListener
     */
    public static void postUpdateUserInfo(UserInfo userInfo, String picPath, ResponseListener listener){
        if(userInfo.getId() < 0) return;

        List<ImageInfo> imageList = new ArrayList<>();
        if(picPath != null){
            imageList.add(new ImageInfo(picPath));
        }
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, Integer.toString(userInfo.getId()));
        params.put(USER_WORK, userInfo.getWork());
        params.put(USER_LOCATION, userInfo.getLocation());
        params.put(USER_SEX, userInfo.getSex());
        params.put(USER_BIRTH, userInfo.getBirth());
        params.put(USER_SIGN, userInfo.getUsersign());
        params.put(USER_NICKNAME, userInfo.getNickname());
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
        if (mRequestQueue == null){
            initialize(YouJoinApplication.getAppContext());
        }
            //throw new RuntimeException("请先初始化mRequestQueue") ;
        return mRequestQueue ;
    }
}