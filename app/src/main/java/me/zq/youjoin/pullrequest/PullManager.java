package me.zq.youjoin.pullrequest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/29.
 */
public class PullManager {

    /**开始轮询服务
     * @param context
     * @param seconds
     * @param cls
     * @param action
     */
    public static void startPullService(Context context, int seconds, Class<?> cls, String action){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtTime = SystemClock.elapsedRealtime();

        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                seconds * 1000, pendingIntent);
    }

    /**停止轮询服务
     * @param context
     * @param cls
     * @param action
     */
    public static void stopPullService(Context context, Class<?> cls, String action){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.cancel(pendingIntent);
    }
}
