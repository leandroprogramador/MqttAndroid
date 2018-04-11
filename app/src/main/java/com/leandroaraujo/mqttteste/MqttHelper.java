package com.leandroaraujo.mqttteste;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 * Created by leandro.araujo2 on 10/04/2018.
 */

public class MqttHelper {

    private MqttAndroidClient mqttAndroidClient;
    private final String serverUri = "tcp://200.143.181.48:1883";
    String clientId = "daniel_saraiva";
    String pass = "123456";
    boolean push = false;
    final String subscriptionTopic = "ath/desire/luz/mesa";
    final String publishTopic = "ath/desire/luz/mesa";
    IConnectionInterface iConnectionInterface;

    int qtdChamada = 0;


    public MqttHelper(final Context context, final IConnectionInterface connInterface){

        iConnectionInterface = connInterface;
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if(reconnect){
                    Log.i("topico", "Reconnected to : " + serverURI);
                    subscriptionTopic();
                } else{
                    Log.i("topico", "Connected to: " + serverURI);

                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.i("topico", "The Connection was lost: ");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                   connInterface.onMessageArrived(message.toString(),topic, 0);

                Log.i("topico", "Incoming message: " + new String(message.getPayload()));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        setMqttOptions();
    }

    public void connect(){
        try{
            mqttAndroidClient.connect(setMqttOptions(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    while (qtdChamada ==0){
                        qtdChamada++;
                        setDisconnectedBufferOptions();
                        subscriptionTopic();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("topico", "Failed to connect to: " + serverUri);

                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void publishMessage(String publishMessage){
        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
            if(!mqttAndroidClient.isConnected()){
                Log.i("topico", "Not connected to: " + serverUri);
            }
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

    }

    private MqttConnectOptions setMqttOptions(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(clientId);
        mqttConnectOptions.setPassword(pass.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        return mqttConnectOptions;
    }

    private void subscriptionTopic() {
        try{
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("topico", "Subscribed: ");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("topico", "Failed to subscribe: ");
                }
            });
        } catch (MqttException ex){

        }
    }

    public interface IConnectionInterface{
        void onMessageArrived(String message, String topic, int channelId);
    }



}
