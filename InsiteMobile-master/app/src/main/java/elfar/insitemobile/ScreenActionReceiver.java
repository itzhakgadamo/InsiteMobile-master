package elfar.insitemobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenActionReceiver extends BroadcastReceiver {

    private String TAG = "ScreenActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();


        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.d(TAG, "screen is on...");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.d(TAG, "screen is off...");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (sharedPref.getBoolean(context.getString(R.string.key_keep_app_open), false)) {
                Intent intentf = new Intent(context, MainActivity.class);
                intentf.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intentf.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentf.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                intentf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentf);
            }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            Log.d(TAG, "screen is unlock...");
        }

    }

    public IntentFilter getFilter() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        return filter;
    }
}

