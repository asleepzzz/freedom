package application.com.a30togo.freedom;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jeromehuang on 8/25/16.
 */
public class ClipMonitorService extends Service {
    static Context mCtx;
    static boolean isEverEnabled;
    static String isEnabled = "isEnabled";
    static String val = "val";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences(isEnabled, MODE_PRIVATE);
        isEverEnabled = prefs.getBoolean(val,false);

        int previousInstallIndex = instagramDownloader.getPreviousInstallCount();
        if (previousInstallIndex!= -1) {
            instagramDownloader.setCnt(mCtx,previousInstallIndex);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final ClipboardManager cb = (ClipboardManager) mCtx.getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setPrimaryClip(ClipData.newPlainText("", ""));
        cb.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
// 具体实现
                ClipData clipData = cb.getPrimaryClip();

                ClipData.Item item = clipData.getItemAt(0);
                String url  = item.getText().toString();
                if (instagramDownloader.isIGUrl(url) && isEverEnabled) {
                    instagramDownloader.download(url,mCtx);
                }
            }

        });
    }

    public static void disableMyIntentService(Context mContext ){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(isEnabled, MODE_PRIVATE).edit();
        editor.putBoolean(val,false).commit();
        isEverEnabled = false;
    }

    public static void enableMyIntentService(Context mContext ){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(isEnabled, MODE_PRIVATE).edit();
        editor.putBoolean(val,true).commit();
        isEverEnabled = true;
    }

    public static boolean isIsEverEnabled(Context mContext ){
        SharedPreferences prefs = mContext.getSharedPreferences(isEnabled, MODE_PRIVATE);
        isEverEnabled = prefs.getBoolean(val,false);
        return isEverEnabled;
    }


    public static void startMyIntentService(Context mContext ){
        mCtx = mContext;
        Intent intent = new Intent(mContext,ClipMonitorService.class);

        ActivityManager manager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("application.com.a30togo.freedom.ClipMonitorService".equals(service.service.getClassName())) {
                return ;
            }
        }

        mContext.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
