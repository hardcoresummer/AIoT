package com.project.iotdashboard;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PlantHealthViewModel extends AndroidViewModel {
    private MutableLiveData<String> plantHealth = new MutableLiveData<String>();
    private MutableLiveData<Bitmap> plantImage= new MutableLiveData<>();


    private MQTTHelper mqttHelper;

    public PlantHealthViewModel(Application application){
        super(application);
        mqttHelper = new MQTTHelper(application.getApplicationContext(), new String[]{"thaotran/feeds/plant-image", "thaotran/feeds/plant-health"});
        //Lambda instruction or Asynchronous instruction
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {}

            @Override
            public void connectionLost(Throwable cause) {}

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (topic.contains("plant-health")) {
                    plantHealth.setValue(message.toString());
                }
                else if (topic.contains("plant-image")) {
                    String strBase64 = message.toString();
                    byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    plantImage.setValue(decodedByte);
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }

    public void onCleared(){
        Log.d("ViewModel","clear");
    }

    public MutableLiveData<Bitmap> getPlantImage() {
        return plantImage;
    }

    public MutableLiveData<String> getPlantHealth() {
        return plantHealth;
    }
}
