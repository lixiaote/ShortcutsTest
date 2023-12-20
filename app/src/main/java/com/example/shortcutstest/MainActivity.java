package com.example.shortcutstest;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCreateDynamicShortcut;
    private Button btnUpdateShortcut;
    private Button btnRemoveShortcut;
    private Button btnDisableShortcut;
    private Button btnCreatePinnedShortcut;
    private ShortcutManager mShortcutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateDynamicShortcut = findViewById(R.id.btn_create_dynamic_shortcut);
        btnCreateDynamicShortcut.setOnClickListener(this);

        btnUpdateShortcut = findViewById(R.id.btn_update_shortcut);
        btnUpdateShortcut.setOnClickListener(this);

        btnRemoveShortcut = findViewById(R.id.btn_remove_shortcut);
        btnRemoveShortcut.setOnClickListener(this);

        btnDisableShortcut = findViewById(R.id.btn_disable_shortcut);
        btnDisableShortcut.setOnClickListener(this);

        btnCreatePinnedShortcut = findViewById(R.id.btn_create_pinned_shortcut);
        btnCreatePinnedShortcut.setOnClickListener(this);
        findViewById(R.id.bit1).setOnClickListener(this);
    }

    /**
     * 创建动态快捷方式
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void createDynamicShortcut() {
        if (mShortcutManager == null) {
            mShortcutManager = getSystemService(ShortcutManager.class);
        }
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setAction(Intent.ACTION_MAIN);
        Intent callIntent = new Intent(this, SettingActivity.class);
        callIntent.setAction("com.example.shortcutstest.CALL");
        ShortcutInfo callShortcut = new ShortcutInfo.Builder(this, "call")
                .setShortLabel("电话")
                .setLongLabel("电话")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_call))
                .setIntents(new Intent[]{mainIntent, callIntent})
                .build();
        mShortcutManager.setDynamicShortcuts(Arrays.asList(callShortcut));
    }

    /**
     * 更新快捷方式
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void updateShortcut() {
        if (mShortcutManager == null) {
            mShortcutManager = getSystemService(ShortcutManager.class);
        }
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setAction("com.example.shortcutstest.CALL");
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "call")
                .setShortLabel("拨打电话")
                .setLongLabel("拨打电话")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_call))
                .setIntent(intent)
                .build();
        mShortcutManager.updateShortcuts(Arrays.asList(shortcutInfo));
    }

    /**
     * 删除快捷方式
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void removeShortcut() {
        if (mShortcutManager == null) {
            mShortcutManager = getSystemService(ShortcutManager.class);
        }
        mShortcutManager.removeDynamicShortcuts(Arrays.asList("call"));
        //        mShortcutManager.removeAllDynamicShortcuts();
        // 获得所有的固定快捷方式
        List<ShortcutInfo> pinnedShortcutList = mShortcutManager.getPinnedShortcuts();
        for (ShortcutInfo shortcutInfo : pinnedShortcutList) {
            if (shortcutInfo.getId().equals("call")) {
                // 禁用被删除的快捷方式
                mShortcutManager.disableShortcuts(Arrays.asList("call"), "该快捷方式已被删除");
            }
        }
    }

    /**
     * 禁用快捷方式
     */
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void disableShortcut() {
        if (mShortcutManager == null) {
            mShortcutManager = getSystemService(ShortcutManager.class);
        }
        mShortcutManager.disableShortcuts(Arrays.asList("call"), "该快捷方式已被禁用");
    }

    /**
     * 创建固定快捷方式
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createPinnedShortcut() {
        if (mShortcutManager == null) {
            mShortcutManager = getSystemService(ShortcutManager.class);
        }
        if (mShortcutManager.isRequestPinShortcutSupported()) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setAction("com.example.shortcutstest.NAVIGATION");
            ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "navigation")
                    .setShortLabel("导航")
                    .setLongLabel("导航")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_navigation))
                    .setIntent(intent)
                    .build();
            // 注册固定快捷方式成功广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.example.shortcutstest.PINNED_BROADCAST");
            PinnedReceiver receiver = new PinnedReceiver();
            registerReceiver(receiver, intentFilter);

            Intent pinnedShortcutCallbackIntent = new Intent("com.example.shortcutstest.PINNED_BROADCAST");
            PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                    pinnedShortcutCallbackIntent, 0);
            mShortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
        }
    }
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    private void ste() {
        Drawable drawable = MainActivity.this.getResources().getDrawable(R.drawable.ic_call);
        Bitmap bitmap=drawable2Bitmap(drawable);
        //if (drawable instanceof BitmapDrawable) {
        //    bitmap = ((BitmapDrawable) drawable).getBitmap();
        //} else {
        //    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        //    Canvas canvas = new Canvas(bitmap);
        //    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        //    drawable.draw(canvas);
        //}
        String gameType = "212";
        String shortName = "shddfdf";
        String uriPath = "https://lkme.cc/UwC/scYqJ8kiX";
        Log.e("xiaote", "ShortcutPermission.check(this) ::::" + ShortcutPermission.check(this));
        //Log.e("xiaote", "ShortcutPermission.check(this) :::+~~~~~~~~1~~~~~~~~:" + hasShortcut(this));
        //Log.e("xiaote", "ShortcutPermission.check(this) :::+~~~~~~~~2~~~~~~~~:" + hasShortcut());
        Log.e("xiaote", "是否支持创建～～～～～" + ShortcutManagerCompat.isRequestPinShortcutSupported(this));


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Log.e("xiaote", "ShortcutPermission.check(this) :::+~~~~~~~~~~~~~~~~:" + checkSelfPermission(Manifest.permission.INSTALL_SHORTCUT));
        }
        //if (true) {
        //    //RuntimeSettingPage runtimeSettingPage = new RuntimeSettingPage(this);
        //    //runtimeSettingPage.start();
        //    ShortcutUtils.createShortcutAboveOreo(this, gameType, shortName, bitmap, uriPath);
        //    return;
        //}

        if (ShortcutPermission.check(this) == ShortcutPermission.PERMISSION_DENIED) {
            //Shortcut.Companion.getSingleInstance().openSetting(mContext);
            RuntimeSettingPage runtimeSettingPage = new RuntimeSettingPage(this);
            runtimeSettingPage.start();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // ToastManagerWrapper.show("android 8以上  ");

                ShortcutUtils.createShortcutAboveOreo(this, gameType, shortName, bitmap, uriPath);
            } else {
                Log.e("xiaote", "是否支持创建～～～～～" + ShortcutManagerCompat.isRequestPinShortcutSupported(this));
               // ShortcutUtils.createShortCut(this, gameType, shortName, bitmap, uriPath);
                ShortcutUtils.createShortcutBelowO(this,shortName,bitmap,uriPath);
                //ShortcutUtils.createShortCut(getActivity());
            }
        }

    }

    public static boolean hasShortcut(Context cx) {
        boolean result = false;
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(cx.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }

        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,
                "title=?", new String[]{title}, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }

    private boolean hasShortcut() {
        boolean isInstallShortcut = false;
        final ContentResolver cr = MainActivity.this.getContentResolver();
        final String AUTHORITY = "com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                new String[]{MainActivity.this.getString(R.string.app_name).trim()}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_dynamic_shortcut:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    // 创建动态快捷方式
                    //createDynamicShortcut();
                    Drawable drawable = getDrawable(R.drawable.ic_navigation);
                    Bitmap bitmap;
                    if (drawable instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                    } else {
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                    }
                    Context context = this;
                    ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "2121")
                            .setShortLabel("hsa")
                            //.setLongLabel("Open the website")
                            .setIcon(IconCompat.createWithBitmap(bitmap))
                            .setIntent(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.baidu.com")))
                            .build();

                    ShortcutManagerCompat.requestPinShortcut(context, shortcut, null);
                }
                break;
            case R.id.btn_update_shortcut:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    // 更新快捷方式
                    updateShortcut();
                }
                break;
            case R.id.btn_remove_shortcut:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    // 删除快捷方式
                    removeShortcut();
                }
                break;
            case R.id.btn_disable_shortcut:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    // 禁用快捷方式
                    disableShortcut();
                }
                break;
            case R.id.btn_create_pinned_shortcut:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 创建固定快捷方式
                    createPinnedShortcut();
                }
                break;
            case R.id.bit1:
                ste();
                break;
            default:
                break;
        }
    }
}
