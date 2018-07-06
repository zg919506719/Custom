package com.luying.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class ActivityUtils {
    public static final String CHARGE_NUMBER = "charge_number";


    //用于模块间跳转
    public static Map<String,String> sActivitys = new HashMap<>();

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (FragmentManager fragmentManager,
                                              Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragement(FragmentManager fragmentManager,
                                        Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    public static void init(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activities = packageInfo.activities;
            for(int i = 0 ; i < activities.length ; i++) {
                ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(new ComponentName(context,activities[i].name),PackageManager.GET_META_DATA);
                Bundle metaData = activityInfo.metaData;
                if(metaData != null) {
                    String flag = metaData.getString("act_flag");
                    sActivitys.put(flag,activityInfo.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Intent buildIntent(Context context, String flagName) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context,sActivitys.get(flagName)));
        return intent;
    }

}
