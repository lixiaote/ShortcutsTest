package com.example.shortcutstest;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;


/**
 * talon
 * 创建快捷方式
 * 2023年12月04日 5:24 PM
 */


public class ShortcutUtils {


    public static void createShortcutAboveOreo(Context context, String gameTypeId, String shortcutName, Bitmap bitmap, String uriPath) {

        ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, gameTypeId).setShortLabel(shortcutName)
                //.setLongLabel("Open the website")
                .setIcon(IconCompat.createWithBitmap(bitmap)).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(uriPath))).build();

        ShortcutManagerCompat.requestPinShortcut(context, shortcut, null);

    }

    private static void createShortcutBelowOreo(Context context, Class<?> targetActivity, String shortcutName, int iconResId) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(context, targetActivity.getName());

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconResId));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }

    /**
     * 实践一:
     */
    public void installShortCut(Context mContext) {
        Intent resultIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 快捷方式的名称
        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "嘿嘿嘿");
        resultIntent.putExtra("duplicate", false); // 不允许重复创建
        // 快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext, R.drawable.ic_navigation);
        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.addCategory("android.intent.category.LAUNCHER");// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        mContext.sendBroadcast(resultIntent);
    }


    //   创建图标 com.share.shortcut
    public static void createShortCut(Context context, String gameTypeId, String shortcutName, Bitmap bitmap, String uriPath) {

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        //    new ShortcutInfo.Builder(context, gameTypeId).setShortLabel(shortcutName)
        //            //.setLongLabel(getString(R.string.dynamic_shortcut_long_label1))
        //            .setIcon(Icon.createWithBitmap(bitmap)).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(uriPath))).build();
        //} else {

        //Intent shortcutIntent = new Intent();
        ////设置点击快捷方式时启动的Activity,因为是从Lanucher中启动，所以包名类名要写全。
        //shortcutIntent.putExtra()
        //shortcutIntent.setComponent(new ComponentName(context.getPackageName(), UriSchemeProcessActivity.class.getName()));
        //
        //shortcutIntent.putExtra("duplicate", false);
        //shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriPath));
        //设置快捷方式图标
        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        //启动的Intent
        //resultIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        //设置快捷方式的名称
        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        resultIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(resultIntent);



        //  }
    }



    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    public static void createShortcutBelowO(Context ctx, String name, Bitmap icon,String uriPath) {

        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);

        // 快捷方式的名字

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 快捷方式的bitmap尽可能小，因为广播内容超过2MB会抛出异常

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);

        // 设置是否允许重复创建快捷方式，该选项非必填，默认是允许

        shortcutIntent.putExtra("duplicate", false);

        // 快捷方式执行的intent，比如启动应用在AndroidManifest中配置的入口Activity

        Intent launchIntent = new Intent();
        //launchIntent.setData(Uri.parse(uriPath));

        launchIntent.setClass(ctx, MainActivity.class);

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);

        ctx.sendBroadcast(shortcutIntent);

    }

}


