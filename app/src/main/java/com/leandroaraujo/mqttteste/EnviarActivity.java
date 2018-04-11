package com.leandroaraujo.mqttteste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class EnviarActivity extends AppCompatActivity implements MqttHelper.IConnectionInterface{

    MqttHelper mqttHelper;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        editText = findViewById(R.id.editText);
        mqttHelper = new MqttHelper(this, this);
        mqttHelper.connect();
    }

    public void enviar(View view) {
        String message = editText.getText().toString();
        mqttHelper.publishMessage(message);
        finish();
    }

    @Override
    public void onMessageArrived(String message, String topic, int channelId) {
        Log.i(topic, message);
    }
}
