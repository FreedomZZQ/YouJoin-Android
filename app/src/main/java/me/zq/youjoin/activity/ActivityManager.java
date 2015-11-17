package me.zq.youjoin.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/11/17.
 */
public class ActivityManager {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
