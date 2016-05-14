package me.zq.youjoin.event;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/5/14.
 */
public class ProfileUpdateEvent {
    public IProfile profile;
    public ProfileUpdateEvent(IProfile p){
        this.profile = p;
    }
}
