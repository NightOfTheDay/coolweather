package com.coolweather.android;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理器
 * Created by Administrator on 2017/8/5.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    //添加一个新活动
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //移除一个活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //关闭所有活动
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
