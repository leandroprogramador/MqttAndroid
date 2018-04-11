package com.leandroaraujo.mqttteste;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by leandro.araujo2 on 10/04/2018.
 */

public class NotificationHelper {

    NotificationCompat.Builder notificationBuilder;
    PendingIntent resultPendingIntent;

    public void postNotification(Context context, String notifMessage, String notifChannel, int notifId){
        Intent resultIntent;
        if(notifId == 0){
            resultIntent = new Intent(context, MainActivity.class);
        } else{
            resultIntent = new Intent(context, EnviarActivity.class);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createNotificationChannel(notifChannel,notifMessage));

        }
        notificationManager.notify(notifId, createNotificationBuilder(context,notifChannel,notifMessage).build());

    }


    private NotificationChannel createNotificationChannel(String notifChannel, String notifString) {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(notifChannel, notifChannel, NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription(notifString);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[]{0, 1000});
            notificationChannel.enableVibration(true);
        }
        return notificationChannel;

    }

    private NotificationCompat.Builder createNotificationBuilder(Context context, String notifMessage, String notifChannel) {
       notificationBuilder =  new NotificationCompat.Builder(context, notifChannel)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notifChannel)
                .setContentText(notifMessage)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(notifChannel)
                .setVibrate(new long[]{0, 1000})
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(resultPendingIntent);

       return notificationBuilder;
    }

}
