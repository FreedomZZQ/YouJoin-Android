package me.zq.youjoin;

import android.test.AndroidTestCase;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.ResultInfo;
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


    /**
     * 测试个人资料更新API工作是否正常
     */
    public void testPostUpdateUserInfo(){
        UserInfo info = new UserInfo();
        info.setId("1");
        info.setBirth("950313");
        info.setLocation("qd");
        info.setWork("student");
        info.setSex("1");

        //正常情况
        NetworkManager.postUpdateUserInfo(info, "/storage/emulated/0/Download/lufei.jpg",
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
        NetworkManager.postUpdateUserInfo(info, "",
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
                    LogUtils.d("Signup error!" + volleyError.toString());
                }

                @Override
                public void onResponse(UserInfo userInfo) {
                    if (userInfo.getResult().equals("success")) {
                        LogUtils.d("Signup Success! username is : " + userInfo.getUsername());
                    } else {
                        LogUtils.d("Signup Failure! username is : " + userInfo.getUsername());
                    }
                }
            });
        }

    }


    public void testSearchUser(){
        NetworkManager.postRequestUserInfo("3", new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e("YouJoin", volleyError.toString());
                assertEquals(true, false);
            }

            @Override
            public void onResponse(UserInfo info) {
                LogUtils.d("YouJoin", info.getUsername());
                assertEquals(info.getUsername(), "zzq");
            }
        });
    }

    public void testSendPrimsg(){
        NetworkManager.postSendMessage("4", "first msg", new ResponseListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

            @Override
            public void onResponse(Object o) {

            }
        });
    }

    public void testReceivePrimsg(){
        NetworkManager.postRequestMessage("2015-12-5", "2015-12-6 15:25:00", new ResponseListener<PrimsgInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

            @Override
            public void onResponse(PrimsgInfo info) {
                List<PrimsgInfo.MessegeEntity> infos = info.getMessege();
                for( PrimsgInfo.MessegeEntity i : infos){
                    LogUtils.d("YouJoin", i.getContent());
                }

            }
        });
    }

    public void testAddFriend(){
        NetworkManager.postAddFriend("4", new ResponseListener<ResultInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

            @Override
            public void onResponse(ResultInfo info) {
                LogUtils.d("YouJoin", info.getResult());
            }
        });
    }
}
