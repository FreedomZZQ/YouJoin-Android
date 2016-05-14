package me.zq.youjoin.event;

import me.zq.youjoin.model.UserInfo;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/5/14.
 */
public class UserInfoUpdateEvent {
    public UserInfo userInfo;
    public UserInfoUpdateEvent(UserInfo userInfo){
        this.userInfo = userInfo;
    }
}
