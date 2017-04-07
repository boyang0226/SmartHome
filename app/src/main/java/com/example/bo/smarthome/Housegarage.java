package com.example.bo.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Bo on 2017-04-03.
 *
 */

public class Housegarage extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_garage_fragment);
        Bundle bun = getIntent().getExtras();
    String   type = bun.getString("type");

        if (type == "House Weather") {
            HouseWeather weather = new HouseWeather(null);
            Bundle    houseweather = getIntent().getExtras();
            weather.setArguments(houseweather);
            getSupportFragmentManager().beginTransaction().add(R.id.house_fragholder, weather).commit();
        } else if (type == "House Garage") {

            HouseGarageFragment garage = new HouseGarageFragment(null);
            Bundle housegarage = getIntent().getExtras();
            garage.setArguments(housegarage);
            getSupportFragmentManager().beginTransaction().add(R.id.house_fragholder, garage).commit();
        }else if (type=="House Temperature"){
            HouseTemp temp = new HouseTemp(null);
            Bundle housetemp = getIntent().getExtras();
            temp.setArguments(housetemp);
            getSupportFragmentManager().beginTransaction().add(R.id.house_fragholder, temp).commit();
        }
    }
}