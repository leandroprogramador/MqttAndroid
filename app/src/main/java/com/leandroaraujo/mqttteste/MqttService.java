package com.leandroaraujo.mqttteste;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by leandro.araujo2 on 10/04/2018.
 */

public class MqttService extends Service implements MqttHelper.IConnectionInterface{

    @Override
    public void onCreate() {
        super.onCreate();
        MqttHelper mqttHelper = new MqttHelper(this, this);
        mqttHelper.connect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent serviceIntent = new Intent(this, MqttHelper.class);
        startService(serviceIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DESTROY", "In onDestroy");
    }

    @Override
    public void onMessageArrived(String message, String topic, int channelId) {
        NotificationHelper notificationHelper = new NotificationHelper();
        notificationHelper.postNotification(this, message.toString(), topic, 1);
    }
}
