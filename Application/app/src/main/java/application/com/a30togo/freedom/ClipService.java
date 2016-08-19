package application.com.a30togo.freedom;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by jeromehuang on 8/19/16.
 */
public class ClipService extends IntentService {
    static Context mCtx;
    public ClipService() {
        super("ClipService");
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
                if (instagramDownloader.isIGUrl(url)) {
                    instagramDownloader.download(url,mCtx);
                }
                //Log.e("kevin", " " + item.getText());
            }

        });
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //String url = workIntent.getStringExtra("url");
    }

    public static void startMyIntentService(Context mContext ){
        mCtx = mContext;
        Intent intent = new Intent(mContext,ClipService.class);

        ActivityManager manager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("application.com.a30togo.freedom.ClipService".equals(service.service.getClassName())) {
                return ;
            }
        }

        //intent.putExtra("url",url);
        mContext.startService(intent);
    }
}