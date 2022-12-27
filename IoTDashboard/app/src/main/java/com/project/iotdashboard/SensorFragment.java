package com.project.iotdashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {


    TextView txtTemp, txtHumi;
    ToggleButton btnPump, btnFan;
    ShimmerFrameLayout container_temp ;

    ShimmerFrameLayout container_humid ;


    SensorViewModel model;
    public SensorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sensor.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance(String param1, String param2) {
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        seem like the lifecycle of Fragment is not long enough it must use the outer activity
        model = new ViewModelProvider(getActivity()).get(SensorViewModel.class);

//        initially there is no data (even null data) so this will not get called
        model.getFanOn().observe(this, fan_cond->{
            btnFan.setChecked(fan_cond);
        });
        model.getPumpOn().observe(this,pump_cond->{
            btnPump.setChecked(pump_cond);
        });
        model.getTempData().observe(this, data->{
            Log.d("APP","data is"+data.toString());
            txtTemp.setText(data);
        });
        model.getHumidData().observe(this, data->{
            txtHumi.setText(data);
        });

    }

    @Override
    public void onViewCreated(View view,Bundle bundle){
        txtTemp = view.findViewById(R.id.txtTemperature);
        txtHumi = view.findViewById(R.id.txtHumidity);
        btnPump = view.findViewById(R.id.btnPUMP);
        btnFan = view.findViewById(R.id.btnFAN);
        btnPump.setOnCheckedChangeListener((compoundButton, b) -> {
            model.setPumpOn(b);
        });

        btnFan.setOnCheckedChangeListener((compoundButton, b) -> {
            model.setFanOn(b);
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Sensor","destroyed");
    }
}