package com.baswarajmamidgi.notemaker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by baswarajmamidgi on 20/07/16.
 */
public class notificationreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("message","Broadcastreceiver");
        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent i=new Intent(context,Dataloader.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1000,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_assignment);
        builder.setContentTitle("Notes Remainder");
        builder.setContentText("Hey ,it's time to make notes.");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        manager.notify(1000,builder.build());

    }
}
