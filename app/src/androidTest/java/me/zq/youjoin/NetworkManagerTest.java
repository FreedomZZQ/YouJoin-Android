package me.zq.youjoin;

import android.test.AndroidTestCase;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.model.UpdateUserInfoResult;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/20.
 */
public class NetworkManagerTest extends AndroidTestCase {

    public static final String testId = "16";
    public static final String testId2 = "17";
    public static final String testTweetId = "14";
    public static final String picPath = "/storage/emulated/0/Download/lufei.jpg";
    public static final String TAG = "YouJoinTest";
    public static final String SUCCESS = "success";

    /**
     * 测试个人资料更新API工作是否正常
     */
    public void testPostUpdateUserInfo(){
        UserInfo info = new UserInfo();
        info.setId(testId);
        info.setBirth("950313");
        info.setLocation("qd");
        info.setWork("学生");
        info.setSex("1");
        info.setUsersign("mzz");
        info.setNickname("吃桔子的攻城狮");

        //正常情况
        NetworkManager.postUpdateUserInfo(info, picPath,
                new ResponseListener<UpdateUserInfoResult>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        assertEquals(true, false);
                    }

                    @Override
                    public void onResponse(UpdateUserInfoResult info) {
                        assertEquals(info.getResult(), "success");
                    }
                });

        //无头像情况
//        NetworkManager.postUpdateUserInfo(info, "",
//                new ResponseListener<UpdateUserInfoResult>() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        assertEquals(true, false);
//                    }
//
//                    @Override
//                    public void onResponse(UpdateUserInfoResult info) {
//                        assertEquals(info.getResult(), "success");
//                    }
//                });

        //无用户信息情况
//        NetworkManager.postUpdateUserInfo(new UserInfo(), "storage/emulated/0/Download/lufei.jpg",
//                new ResponseListener<UpdateUserInfoResult>() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        assertEquals(true, false);
//                    }
//
//                    @Override
//                    public void onResponse(UpdateUserInfoResult info) {
//                        assertEquals(info.getResult(), "success");
//
//                    }
//                });

    }

    /**
     * 测试注册API工作是否正常
     */
    public void testSignUp(){

        ArrayList<UserInfo> mData = new ArrayList<>();

        for(int i = 0; i < 26; i++){

            for(int j = 0; j < 6; j++){
                UserInfo info = new UserInfo();
                char [] name = {(char)('A' + i), (char)('A' + i), (char)('1' + j)};
                info.setUsername(new String(name));
                info.setEmail(new String(name) + "@test.com");
                mData.add(info);
            }

        }

        for(UserInfo userInfo : mData){
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            NetworkManager.postSignUp(userInfo.getUsername(), "123", userInfo.getEmail()
                    , new ResponseListener<UserInfo>() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtils.d(TAG, "Signup error!" + volleyError.toString());
                }

                @Override
                public void onResponse(UserInfo userInfo) {
                    assertEquals(userInfo.getResult(), SUCCESS);
                }
            });
        }

    }

    public void testRequestUserInfo(){
        NetworkManager.postRequestUserInfo(testId, new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
                assertEquals(true, false);
            }

            @Override
            public void onResponse(UserInfo info) {
                LogUtils.d(TAG, info.getUsername() + info.getNickname());
                assertEquals(info.getUsername(), "zzq");
            }
        });
    }

    public void testSendPrivateMsg(){
        NetworkManager.postSendMessage(testId, testId2, "first msg", new ResponseListener<ResultInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

            @Override
            public void onResponse(ResultInfo info) {
                assertEquals(info.getResult(), SUCCESS);
            }
        });
    }

    public void testReceivePrivatemsg(){
        NetworkManager.postRequestMessage(testId2, new ResponseListener<PrimsgInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(PrimsgInfo info) {
                assertEquals(info.getResult(), SUCCESS);
                List<PrimsgInfo.MessegeEntity> infos = info.getMessege();
                for( PrimsgInfo.MessegeEntity i : infos){
                    LogUtils.d(TAG, i.getContent());
                }

            }
        });
    }

    public void testAddFriend(){

        NetworkManager.postAddFriend(testId, testId2, new ResponseListener<ResultInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(ResultInfo info) {
//                assertEquals(info.getResult(), SUCCESS);
            }
        });

        for(int i = 17; i < 150; i++){
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NetworkManager.postAddFriend(testId, Integer.toString(i), new ResponseListener<ResultInfo>() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtils.e(TAG, volleyError.toString());
                }

                @Override
                public void onResponse(ResultInfo info) {
//                    assertEquals(info.getResult(), SUCCESS);
                }
            });
        }

    }

    public void testSendTweet(){
        List<ImageInfo> images = new ArrayList<>();
        images.add(new ImageInfo(picPath));
        NetworkManager.postSendTweet(testId, "haha i love mzz!", images, new ResponseListener<ResultInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(ResultInfo info) {
                assertEquals(info.getResult(), SUCCESS);
            }
        });
    }

    public void testRequestTweets(){
        NetworkManager.postRequestTweets(testId2, testTweetId, "1", new ResponseListener<TweetInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(TweetInfo info) {
                List<TweetInfo.TweetsEntity> infos = info.getTweets();
                for(TweetInfo.TweetsEntity i : infos){
                    LogUtils.d(TAG, i.getTweets_img());
                }

            }
        });
    }

    public void testUpvoteTweet(){
        NetworkManager.postUpvoteTweet(testId2, testTweetId, new ResponseListener<ResultInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(ResultInfo info) {
                LogUtils.d(TAG, info.getResult());
            }
        });
    }

    public void testCommentTweet(){
        NetworkManager.postCommentTweet(testId2, testTweetId, "nice!", new ResponseListener<ResultInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(ResultInfo info) {
                LogUtils.d(TAG, info.getResult());
            }
        });
    }

    public void testRequestFriendList() {
        NetworkManager.postRequestFriendList(testId2, new ResponseListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(Object o) {

            }
        });
    }

}
