package me.zq.youjoin.utils;

import me.zq.youjoin.YouJoinApplication;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/29.
 */
public class GlobalUtils {
    public static int dpToPx(int dpValue) {
        return (int) (dpValue * YouJoinApplication.sScale + 0.5f);
    }
}
