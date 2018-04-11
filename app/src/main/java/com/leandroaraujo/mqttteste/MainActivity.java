package com.leandroaraujo.mqttteste;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MqttHelper.IConnectionInterface{


    Button btnAbrir;
    TextView txtData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MqttService.class);
        startService(intent);

        txtData = findViewById(R.id.textView);
        btnAbrir = findViewById(R.id.button);

        MqttHelper mqttHelper = new MqttHelper(this, this);
        mqttHelper.connect();

    }

    public void enviar(View view) {
        Intent intent = new Intent(this, EnviarActivity.class);
        startActivity(intent);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    public void onMessageArrived(String message, String topic, int channelId) {
        txtData.setText(topic + ": " + message);
    }
}
