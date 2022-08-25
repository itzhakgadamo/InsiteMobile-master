package elfar.insitemobile;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static elfar.insitemobile.App.CHANNEL_ID;

public class AppService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent i = new Intent(this, MainActivity.class);
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        try {
            startActivity(i);
            Log.i("InsiteMobileLog", "Service Started");
            AppManager.appendLogz("Service Started");
            Log.i("InsiteMobileLog","Activity started by service");
        } catch (Exception e) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("InsiteMobile Service")
                .setContentText("Run the app by clicking here")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        startService();
        super.onDestroy();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, AppService.class);
        serviceIntent.putExtra("inputExtra", "InsiteMobileService");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
