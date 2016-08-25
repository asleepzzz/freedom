package application.com.a30togo.freedom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jeromehuang on 8/22/16.
 */
public class RastreadorBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("kevin","RastreadorBroadcastReceiver");
        ClipMonitorService.startMyIntentService(context);
    }
}
