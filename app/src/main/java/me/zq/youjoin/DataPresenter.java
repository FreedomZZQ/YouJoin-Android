package me.zq.youjoin;

import android.util.Log;

import com.android.volley.VolleyError;

import java.util.List;

import me.zq.youjoin.db.DatabaseManager;
import me.zq.youjoin.model.CommentInfo;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.model.NewPrimsgInfo;
import me.zq.youjoin.model.PluginInfo;
import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.model.UpdateUserInfoResult;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.JsonSyntaxError;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;

/**用于封装所有除图片加载外的数据请求(数据库和网络)，将结果提供给UI显示
 * YouJoin-Android
 * Created by ZQ on 2015/12/13.
 */
public class DataPresenter {

    public static final String TAG = "YouJoin";



    public static UserInfo requestUserInfoFromCache(int userId){
        return DatabaseManager.getUserInfoById(userId);
    }

    public static UserInfo requestUserInfoFromCache(String username){
        return DatabaseManager.getUserInfoByUserName(username);
    }

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

        // TODO: 2016/5/1 这里有严重bug！！！
//        FriendsInfo cookieInfo = DatabaseManager.getFriendList(userId);
//        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
//            q.onGetFriendList(cookieInfo);
//        }

        NetworkManager.postRequestFriendList(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                new ResponseListener<FriendsInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(volleyError.getClass() == JsonSyntaxError.class) return;
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

    public static void sendTweet(int userId, String content, List<ImageInfo> images,
                                 final SendTweet q){
        NetworkManager.postSendTweet(Integer.toString(userId), content, images,
                new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onSendTweet(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onSendTweet(info);
                    }
                });
    }

    public static void signIn(String username, String password, final SignIn q){
        NetworkManager.postSignIn(username, password, new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Signin error! " + volleyError.toString());
                UserInfo info = new UserInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onSign(info);
            }

            @Override
            public void onResponse(UserInfo userInfo) {
                q.onSign(userInfo);
            }
        });
    }

    public static void signUp(String username, String password, String email, final SignUp q){
        NetworkManager.postSignUp(username, password, email, new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Signup error!" + volleyError.toString());
                UserInfo info = new UserInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onSign(info);
            }

            @Override
            public void onResponse(UserInfo userInfo) {
                q.onSign(userInfo);
            }
        });
    }

    public static void updateUserInfo(final UserInfo userInfo, String picPath, final UpdateUserInfo q){
        NetworkManager.postUpdateUserInfo(userInfo, picPath, new ResponseListener<UpdateUserInfoResult>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                UpdateUserInfoResult result = new UpdateUserInfoResult();
                result.setResult(NetworkManager.FAILURE);
                q.onUpdateUserInfo(result);
            }

            @Override
            public void onResponse(UpdateUserInfoResult result) {
                q.onUpdateUserInfo(result);
                DatabaseManager.addUserInfo(userInfo);
            }
        });
    }

    public static void sendPrimsg(int receiverId, String content, final SendPrimsg q){

        NetworkManager.postSendMessage(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                Integer.toString(receiverId), content,
                new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onSendPrimsg(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onSendPrimsg(info);
                    }
                });

    }

    public static void addFriend(final int friendId, final AddFriend q){
        NetworkManager.postAddFriend(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                Integer.toString(friendId), new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onAddFriend(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onAddFriend(info);
                        if(info.getResult().equals(NetworkManager.SUCCESS)){
                            DatabaseManager.addFriendInfo(friendId);
                        }
                    }
                });
    }

    public static void getPrimsgList(int friendId, final GetPrimsgList q){
        PrimsgInfo cookieInfo = DatabaseManager.getPrimsgList(friendId);
        if(cookieInfo.getResult().equals(NetworkManager.SUCCESS)){
            q.onGetPrimsgList(cookieInfo);
        }

        NetworkManager.postRequestPrimsg(Integer.toString(YouJoinApplication.getCurrUser().getId()),
                Integer.toString(friendId), NetworkManager.TIME_OLD, "99999999",
                new ResponseListener<PrimsgInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        PrimsgInfo info = new PrimsgInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onGetPrimsgList(info);
                    }

                    @Override
                    public void onResponse(PrimsgInfo info) {
                        q.onGetPrimsgList(info);
                        if(info.getResult().equals(NetworkManager.SUCCESS)){
                            DatabaseManager.addPrimsgList(info);
                        }
                    }
                });
    }

    public static void getCommentList(int tweetId, final GetCommentList q){

        NetworkManager.postRequestComments(Integer.toString(tweetId),
                new ResponseListener<CommentInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
                CommentInfo info = new CommentInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onGetCommentList(info);
            }

            @Override
            public void onResponse(CommentInfo info) {
                q.onGetCommentList(info);
            }
        });
    }

    public static void sendComment(int userId, int tweetId, String commentContent, final SendComment q){

        NetworkManager.postCommentTweet(Integer.toString(userId), Integer.toString(tweetId),
                commentContent, new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        ResultInfo info = new ResultInfo();
                        info.setResult(NetworkManager.FAILURE);
                        q.onSendComment(info);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        q.onSendComment(info);
                    }
                });
    }

    public static void requestNewPrimsg(int userId, final GetNewPrimsg q){
        NetworkManager.postRequestMessage(Integer.toString(userId), new ResponseListener<NewPrimsgInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NewPrimsgInfo info = new NewPrimsgInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onGetNewPrimsg(info);
            }

            @Override
            public void onResponse(NewPrimsgInfo info) {
                q.onGetNewPrimsg(info);
            }
        });
    }

    public static void requestPluginList(int userId, final GetPluginList q){

        NetworkManager.postRequestPlugin(Integer.toString(userId), new ResponseListener<PluginInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                PluginInfo info = new PluginInfo();
                info.setResult(NetworkManager.FAILURE);
                q.onGetPluginList(info);
            }

            @Override
            public void onResponse(PluginInfo info) {
                q.onGetPluginList(info);
            }
        });

    }

    public interface GetPluginList{
        void onGetPluginList(PluginInfo info);
    }

    public interface GetNewPrimsg{
        void onGetNewPrimsg(NewPrimsgInfo info);
    }

    public interface SendComment{
        void onSendComment(ResultInfo info);
    }

    public interface GetCommentList{
        void onGetCommentList(CommentInfo info);
    }

    public interface GetPrimsgList{
        void onGetPrimsgList(PrimsgInfo info);
    }

    public interface AddFriend{
        void onAddFriend(ResultInfo info);
    }

    public interface SendPrimsg{
        void onSendPrimsg(ResultInfo info);
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

    public interface SendTweet{
        void onSendTweet(ResultInfo info);
    }

    public interface SignIn{
        void onSign(UserInfo info);
    }

    public interface SignUp{
        void onSign(UserInfo info);
    }

    public interface UpdateUserInfo{
        void onUpdateUserInfo(UpdateUserInfoResult info);
    }

}
