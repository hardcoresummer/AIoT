package com.project.iotdashboard;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SensorViewModel extends AndroidViewModel {
    private MutableLiveData<String> tempData = new MutableLiveData<String>();
    private MutableLiveData<String> humidData= new MutableLiveData<String>();
    private MutableLiveData<Boolean> fanOn = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> pumpOn = new MutableLiveData<Boolean>();

    private MQTTHelper mqttHelper;

    public SensorViewModel(Application application){
        super(application);
        mqttHelper = new MQTTHelper(application.getApplicationContext(), new String[]{"thaotran/feeds/sensor1", "thaotran/feeds/sensor2", "thaotran/feeds/actuator1", "thaotran/feeds/actuator2"});
        //Lambda instruction or Asynchronous instruction
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {}

            @Override
            public void connectionLost(Throwable cause) {}

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + " *** " + message.toString()+"***"+mqttHelper.mqttAndroidClient.toString());
                if (topic.contains("sensor1")) {
                    tempData.setValue(message.toString());
//                    container_temp.stopShimmer();
//                    container_temp.setVisibility(View.GONE);
                }
                else if (topic.contains("sensor2")) {
                    humidData.setValue(message.toString());
                }
                else if (topic.contains("actuator1")) {
                    if(message.toString().equals("1")) {
                        pumpOn.setValue(true);
                    }
                    else
                        pumpOn.setValue(false);                }
                else if (topic.contains("actuator2")) {
                    if(message.toString().equals("1")) {
                        fanOn.setValue(true);
                    }
                    else
                        fanOn.setValue(false);
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }


    @Override
    public void onCleared(){
        super.onCleared();
    }
    public MutableLiveData<String> getTempData() {
        return tempData;
    }

    public MutableLiveData<Boolean> getPumpOn() {
        return pumpOn;
    }

    public MutableLiveData<Boolean> getFanOn() {
        return fanOn;
    }

    public MutableLiveData<String> getHumidData() {
        return humidData;
    }

    public void setFanOn(boolean fanOn) {
//        this.fanOn.setValue(fanOn);
        if (fanOn) {
            mqttHelper.publish("thaotran/feeds/actuator2", "1");
        }
        else {
            mqttHelper.publish("thaotran/feeds/actuator2", "0");
        }
    }
    public void setPumpOn(boolean pumpOn) {
//        this.pumpOn.setValue(pumpOn);
        if (pumpOn) {
            mqttHelper.publish("thaotran/feeds/actuator1", "1");
        }
        else {
            mqttHelper.publish("thaotran/feeds/actuator1", "0");
        }
    }
}

