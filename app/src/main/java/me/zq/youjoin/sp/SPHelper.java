package me.zq.youjoin.sp;

import android.content.Context;
import android.content.SharedPreferences;

import me.zq.youjoin.YouJoinApplication;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/5/26.
 */
public class SPHelper {
    private static final String SP_NAME = "YouJoinApp";

    /**
     * 更新某会话的未读消息数
     * @param conversationId 会话Id
     * @param count 未读消息数
     */
    public static void setUnReadMsgCount(String conversationId, int count){
        SharedPreferences.Editor editor =
                YouJoinApplication.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(conversationId, count);
        editor.apply();
    }

    /**
     * 获取某会话的未读消息数
     * @param conversationId 会话Id
     * @return 未读消息数
     */
    public static int getUnReadMsgCount(String conversationId){
        SharedPreferences sp = YouJoinApplication.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(conversationId, 0);
    }
}
