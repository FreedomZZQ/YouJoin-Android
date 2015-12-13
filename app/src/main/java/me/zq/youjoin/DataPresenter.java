package me.zq.youjoin;

import com.android.volley.VolleyError;

import me.zq.youjoin.db.DatabaseManager;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;

/**用于封装所有除图片加载外的数据请求(数据库和网络)，将结果提供给UI显示
 * YouJoin-Android
 * Created by ZQ on 2015/12/13.
 */
public class DataPresenter {

    public static final String TAG = "YouJoin";

    public static void requestUserInfoAuto(String param, final GetUserInfo q){
        UserInfo cookieInfo = DatabaseManager.getUserInfoAuto(param);
        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
            q.onGetUserInfo(cookieInfo);
        }
        NetworkManager.postRequestUserInfo(param,
                new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        UserInfo info = new UserInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetUserInfo(info);
                    }

                    @Override
                    public void onResponse(UserInfo info) {
                        if (info.getResult().equals(NetworkManager.SUCCESS)) {
                            q.onGetUserInfo(info);
                            DatabaseManager.addUserInfo(info);
                        }
                    }
                });
    }

    public static void requestUserInfoById(int userId, final GetUserInfo q){
        UserInfo cookieInfo = DatabaseManager.getUserInfoById(userId);
        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
            q.onGetUserInfo(cookieInfo);
        }

        NetworkManager.postRequestUserInfo(Integer.toString(userId),
                new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        UserInfo info = new UserInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetUserInfo(info);
                    }

                    @Override
                    public void onResponse(UserInfo info) {
                        if (info.getResult().equals(NetworkManager.SUCCESS)) {
                            q.onGetUserInfo(info);
                            DatabaseManager.addUserInfo(info);
                        }
                    }
                });
    }

    public static void requestFriendList(int userId, final GetFriendList q){

        FriendsInfo cookieInfo = DatabaseManager.getFriendList(userId);
        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
            q.onGetFriendList(cookieInfo);
        }

        NetworkManager.postRequestFriendList(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                new ResponseListener<FriendsInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        FriendsInfo info = new FriendsInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetFriendList(info);
                    }

                    @Override
                    public void onResponse(FriendsInfo info) {
                        q.onGetFriendList(info);
                    }
                });
    }

    public static void requestTweets(int userId, String tweetId, String timeType, final GetTweets q){
        NetworkManager.postRequestTweets(Integer.toString(userId), tweetId, timeType
                , new ResponseListener<TweetInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        TweetInfo info = new TweetInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetTweets(info);
                    }

                    @Override
                    public void onResponse(TweetInfo info) {
                        q.onGetTweets(info);
                    }
                });
    }

    public interface GetUserInfo{
        void onGetUserInfo(UserInfo info);
    }

    public interface GetFriendList{
        void onGetFriendList(FriendsInfo info);
    }

    public interface GetTweets{
        void onGetTweets(TweetInfo info);
    }

}
