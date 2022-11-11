package com.project.iotdashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

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
        parseApiData();
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

    public void parseApiData() {
        StringRequest stringRequestForActuator1 = new StringRequest(Request.Method.GET, "https://io.adafruit.com/api/v2/thaotran/feeds/actuator1", response -> {
            Log.e("Res: ", response);
            try {
                JSONObject obj = new JSONObject(response);
                String lastValue = obj.getString("last_value");
                if (lastValue.equals("1"))
                    btnPump.setChecked(true);
                else
                    btnPump.setChecked(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        StringRequest stringRequestForActuator2 = new StringRequest(Request.Method.GET, "https://io.adafruit.com/api/v2/thaotran/feeds/actuator2", response -> {
            Log.e("Res: ", response);
            try {
                JSONObject obj = new JSONObject(response);
                String lastValue = obj.getString("last_value");
                if (lastValue.equals("1"))
                    btnFan.setChecked(true);
                else
                    btnFan.setChecked(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        StringRequest stringRequestForSensor1 = new StringRequest(Request.Method.GET, "https://io.adafruit.com/api/v2/thaotran/feeds/sensor1", response -> {
            Log.e("Res: ", response);
            try {
                JSONObject obj = new JSONObject(response);
                String lastValue = obj.getString("last_value");
                txtTemp.setText(lastValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        StringRequest stringRequestForSensor2 = new StringRequest(Request.Method.GET, "https://io.adafruit.com/api/v2/thaotran/feeds/sensor2", response -> {
            Log.e("Res: ", response);
            try {
                JSONObject obj = new JSONObject(response);
                String lastValue = obj.getString("last_value");
                txtHumi.setText(lastValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequestForActuator1);
        requestQueue.add(stringRequestForActuator2);
        requestQueue.add(stringRequestForSensor1);
        requestQueue.add(stringRequestForSensor2);
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