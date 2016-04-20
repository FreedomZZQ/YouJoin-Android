package me.zq.youjoin.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/4/20.
 */
public class ImTypeMessageEvent {
    public AVIMTypedMessage message;
    public AVIMConversation conversation;
}
