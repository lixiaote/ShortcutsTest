package com.example.shortcutstest;

import static com.example.shortcutstest.ShortcutPermissionChecker.checkOnEMUI;
import static com.example.shortcutstest.ShortcutPermissionChecker.checkOnMIUI;
import static com.example.shortcutstest.ShortcutPermissionChecker.checkOnOPPO;
import static com.example.shortcutstest.ShortcutPermissionChecker.checkOnVIVO;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ZP on 2019-12-02.
 */
public class ShortcutPermission {

    private static final String TAG = "ShortcutPermission";

    @IntDef(value = {
            PERMISSION_GRANTED,
            PERMISSION_DENIED,
            PERMISSION_ASK,
            PERMISSION_UNKNOWN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionResult {}

    /**
     * Permission check result: this is returned by {@link #check(Context)}
     * if the permission has been granted to the given package.
     */
    public static final int PERMISSION_GRANTED = 0;

    /**
     * Permission check result: this is returned by {@link #check(Context)}
     * if the permission has not been granted to the given package.
     */
    public static final int PERMISSION_DENIED = -1;

    public static final int PERMISSION_ASK = 1;

    public static final int PERMISSION_UNKNOWN = 2;

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    @PermissionResult
    public static int check(Context context) {
        int result = PERMISSION_UNKNOWN;
        if (MARK.contains("huawei")) {
            result = checkOnEMUI(context);
        } else if (MARK.contains("xiaomi")) {
            result = checkOnMIUI(context);
        } else if (MARK.contains("oppo")) {
            result = checkOnOPPO(context);
        } else if (MARK.contains("vivo")) {
            result = checkOnVIVO(context);
        } else if (MARK.contains("samsung") || MARK.contains("meizu")) {
            result = PERMISSION_GRANTED;
        }
        return result;
    }

}
