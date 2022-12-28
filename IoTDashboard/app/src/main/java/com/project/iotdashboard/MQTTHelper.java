package com.project.iotdashboard;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.Charset;


public class MQTTHelper {
    public MqttAndroidClient mqttAndroidClient;
    private String[] arrayTopics = null;

    final String clientId = "12312122";
    final String serverUri = "tcp://io.adafruit.com:1883";
    final String username = "thaotran";
    final String password = "aio_JayG32iUQD4wmHRX68fpLmHWEd8T";


    public MQTTHelper(Context context,String[] topics) {
        arrayTopics = topics;
//        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        connect();
    }


    public void setCallback(MqttCallbackExtended callback) {
       mqttAndroidClient.setCallback(callback);
    }

    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
             mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTT","connect successfully");
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("MQTT", "Failed to connect to: " + serverUri + exception.toString());
                }
            });

        } catch (MqttException ex){
            Log.e("MQTT","error connecting?");
            ex.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        for(int i = 0; i < arrayTopics.length; i++) {
            try {
                int finalI = i;
                mqttAndroidClient.subscribe(arrayTopics[i], 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
//                        Log.d("TEST", "Subscribed!");
                        try {
                            //  get the first message
                            mqttAndroidClient.publish(arrayTopics[finalI].concat("/get"),new MqttMessage());
//                            Log.d("MQTT",arrayTopics[finalI].concat("/get"));
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d("TEST", "Subscribed fail!");
                    }
                });

            } catch (MqttException ex) {
                System.err.println("Exceptions subscribing");
                ex.printStackTrace();
            }
        }
//        initData();

    }
    public void publish(String topic, String value)  {
        MqttMessage msg = new MqttMessage();
//        msg.setId(1234);
        msg.setQos(2);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);
        try {
            mqttAndroidClient.publish(topic,msg);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
