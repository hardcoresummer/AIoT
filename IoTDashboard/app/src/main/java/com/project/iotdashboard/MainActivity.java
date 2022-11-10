package com.project.iotdashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumi;
    ToggleButton btnPump, btnFan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMQTT();

        txtTemp = findViewById(R.id.txtTemperature);
        txtHumi = findViewById(R.id.txtHmidity);
        btnPump = findViewById(R.id.btnPUMP);
        btnFan = findViewById(R.id.btnFAN);
        btnPump.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sendDataMQTT("thaotran/feeds/actuator1", "1");
            }
            else {
                sendDataMQTT("thaotran/feeds/actuator1", "0");
            }
        });

        btnFan.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sendDataMQTT("thaotran/feeds/actuator2", "1");
            }
            else {
                sendDataMQTT("thaotran/feeds/actuator2", "0");
            }
        });
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){

        }

    }

    public void startMQTT() {
        mqttHelper = new MQTTHelper(this);
                //Lambda instruction or Asynchronous instruction


        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + " *** " + message.toString());
                if (topic.contains("sensor1")) {
                    txtTemp.setText(message.toString());
                }
                else if (topic.contains("sensor2")) {
                    txtHumi.setText(message.toString());
                }
                else if (topic.contains("actuator1")) {
                    if(message.toString().equals("1")) {
                        btnPump.setChecked(true);
                    }
                    else
                        btnPump.setChecked(false);
                }
                else if (topic.contains("actuator2")) {
                    if(message.toString().equals("1")) {
                        btnFan.setChecked(true);
                    }
                    else
                        btnFan.setChecked(false);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}