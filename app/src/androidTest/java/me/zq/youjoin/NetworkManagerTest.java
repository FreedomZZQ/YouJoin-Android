package me.zq.youjoin;

import android.test.AndroidTestCase;

import com.android.volley.VolleyError;

import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/20.
 */
public class NetworkManagerTest extends AndroidTestCase {


    /**
     * 测试个人资料更新API工作是否正常
     */
    public void testPostUploadPic(){
        UserInfo info = new UserInfo();
        info.setId("1");
        info.setBirth("950313");
        info.setLocation("qd");
        info.setWork("student");
        info.setSex("1");

        //正常情况
        NetworkManager.postUpdateUserInfo(info, "storage/emulated/0/Download/lufei.jpg",
                new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        assertEquals(true, false);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        assertEquals(info.getResult(), "success");
                    }
                });

        //无头像情况
        NetworkManager.postUpdateUserInfo(info, "",
                new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        assertEquals(true, false);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        assertEquals(info.getResult(), "success");
                    }
                });

        //无用户信息情况
        NetworkManager.postUpdateUserInfo(new UserInfo(), "storage/emulated/0/Download/lufei.jpg",
                new ResponseListener<ResultInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        assertEquals(true, false);
                    }

                    @Override
                    public void onResponse(ResultInfo info) {
                        assertEquals(info.getResult(), "success");
                    }
                });

    }
}
