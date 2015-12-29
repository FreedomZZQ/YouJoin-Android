package me.zq.youjoin.pullrequest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.activity.MainActivity;
import me.zq.youjoin.model.NewPrimsgInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.LogUtils;

public class PullService extends Service implements DataPresenter.GetNewPrimsg{

    public static final String ACTION = "me.zq.youjoin.pullrequest.PullService";
    public static final String TAG = "YouJoinPullService";


    /**
     * 轮询时间间隔
     */
    public static final int PULL_TIME = 20;

    private Notification notification;
    private NotificationManager manager;

    public PullService() {
    }

    @Override
    public void onCreate(){
        initNotifiManager();
        new PullThread().start();
    }

    private void initNotifiManager(){
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new Notification.Builder(this)
                .setContentTitle("YouJoin")
                .setContentText("New Message")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void showNotification(){
        notification.when = System.currentTimeMillis();
        manager.notify(0, notification);
    }

    @Override
    public void onGetNewPrimsg(NewPrimsgInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            HashSet<Integer> h = new HashSet<>(info.getMessage());
            List<Integer> msgFrom = new ArrayList<>();
            msgFrom.addAll(h);
            for(Integer i : msgFrom){
                LogUtils.d(TAG, "New Primsg from: userid = " + i.toString());
            }
        }
    }

    class PullThread extends Thread {
        @Override
        public void run(){
            for(int i = 0;; i++){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(i % PULL_TIME == 0){
                    LogUtils.d(TAG, "Pulling...");
                    DataPresenter.requestNewPrimsg(YouJoinApplication.getCurrUser().getId(),
                            PullService.this);
                }
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
