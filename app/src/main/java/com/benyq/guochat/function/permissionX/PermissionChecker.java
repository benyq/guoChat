package com.benyq.guochat.function.permissionX;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author benyq
 * @time 2020/7/30
 * @e-mail 1520063035@qq.com
 * @note
 */
public class PermissionChecker {

    public static final String TAG = "PermissionCheckerFragment";

    public static void request(Context activity , PermissionCheckCallBack callBack, String... permissions) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            PermissionFragment existFragment = (PermissionFragment) fragmentManager.findFragmentByTag(TAG);
            if (existFragment == null) {
                existFragment = new PermissionFragment();
                fragmentManager.beginTransaction().add(existFragment, TAG).commitNow();
            }
            existFragment.requestNow(callBack, permissions);
        }else {
            throw new RuntimeException("Context must be FragmentActivity");
        }
    }

    public static void request2(Context activity , PermissionCheckCallBack callBack, String... permissions) {

    }

    public static void request3(Context activity, PermissionCheckCallBack callBack) {
        System.out.println("吃屎了你啊2");
    }
}
