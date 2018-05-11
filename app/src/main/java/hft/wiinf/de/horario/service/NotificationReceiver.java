package hft.wiinf.de.horario.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import hft.wiinf.de.horario.R;
import hft.wiinf.de.horario.TabActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        addNotification(context, intent);
    }

    private void addNotification(Context context, Intent intent) {
        String msg = "Erinnerung an \"" + intent.getStringExtra("Event") + "\" um " + intent.getIntExtra("Hour", 0) + ":" + intent.getStringExtra("Minute") + " Uhr";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "")
                        .setSmallIcon(R.drawable.ic_android_black2_24dp)
                        .setContentTitle("Terminerinnerung")
                        .setContentText(msg);

        Intent notificationIntent = new Intent(context, TabActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, intent.getIntExtra("ID", 0), notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(intent.getIntExtra("ID", 0), builder.build());
    }
}