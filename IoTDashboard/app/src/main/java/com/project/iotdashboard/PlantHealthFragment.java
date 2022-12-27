package com.project.iotdashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlantHealthFragment extends Fragment {




    TextView healthPlant;
    ImageView imgPlant;
    PlantHealthViewModel model;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(PlantHealthViewModel.class);

        model.getPlantHealth().observe(this, data->{
            healthPlant.setText(data);
        });
        model.getPlantImage().observe(this,data->{
            imgPlant.setImageBitmap(data);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_health, container, false);
    }
    @Override
    public  void onViewCreated(View view,Bundle bundle){
        imgPlant = view.findViewById(R.id.imageView6);
        healthPlant = view.findViewById(R.id.textView5);
    }
}