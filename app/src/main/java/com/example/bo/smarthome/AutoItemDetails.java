package com.example.bo.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutoItemDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_item_details);
        //Bundle bun = getIntent().getExtras();
        String function = getIntent().getExtras().getString("Function");

        switch (function){
            case "temperature":
                AutoTemperatureFragment fragtemp = new AutoTemperatureFragment(null);
                Bundle buntemp = getIntent().getExtras();
                fragtemp.setArguments(buntemp);
                getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, fragtemp).commit();
                break;

            case "lights":

                break;

            case "gps":
                AutoGPSFragment fraggps = new AutoGPSFragment(null);
                Bundle bungps = getIntent().getExtras();
                fraggps.setArguments(bungps);
                getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, fraggps).commit();
                break;

            case"radio":

                break;
        }

    }
}


