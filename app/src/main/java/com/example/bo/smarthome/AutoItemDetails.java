package com.example.bo.smarthome;
// author  Zhen Qu
// student number 040587623
//http://stackoverflow.com/questions/18337536/android-overriding-onbackpressed

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutoItemDetails extends AppCompatActivity {  //activity used to tell the phone which fragment should be opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_item_details);
        String function = getIntent().getExtras().getString("Function"); // tell which function is called ( auto temp, auto lights, auto gps, auto radio

        switch (function){
            case "temperature":    //open the auto temp fragment with bundle data
                AutoTemperatureFragment fragtemp = new AutoTemperatureFragment(null);
                Bundle buntemp = getIntent().getExtras();
                fragtemp.setArguments(buntemp);
                getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, fragtemp).commit();
                break;

            case "lights":     //open the auto lights fragment with bundle data
                AutoLightsFragment fraglights = new AutoLightsFragment(null);
                Bundle bunlights = getIntent().getExtras();
                fraglights.setArguments(bunlights);
                getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, fraglights).commit();
                break;

            case "gps":     //open the auto gps fragment with bundle data
                AutoGPSFragment fraggps = new AutoGPSFragment(null);
                Bundle bungps = getIntent().getExtras();
                fraggps.setArguments(bungps);
                getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, fraggps).commit();
                break;

            case"radio":    //open the auto radio fragment with bundle data
                AutoRadioFragment fragradio = new AutoRadioFragment(null);
                Bundle bunradio = getIntent().getExtras();
                fragradio.setArguments(bunradio);
                getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, fragradio).commit();
                break;
        }


    }

    public void onBackPressed(){ //when click back button(back button on phone), return to listview page
        super.onBackPressed();
        Intent intentBack = new Intent(AutoItemDetails.this, AutoListView.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentBack);
        finish();
    }
}


