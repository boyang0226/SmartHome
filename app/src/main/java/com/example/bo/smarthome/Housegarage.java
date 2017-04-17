package com.example.bo.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Bo on 2017-04-03.
 *
 * this is the class to connect all the fragment, use type to
 *
 * determine with fragment should to go
 *
 *
 */

public class Housegarage extends AppCompatActivity {

//on create
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_garage_fragment);

//get the type value we put at housesetingdetail class
 String type =getIntent().getExtras().getString("type");
//if it is house weather,go to the house weather fragment
        if (type.equals( "House Weather")) {
            HouseWeather weather = new HouseWeather(null);

            getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, weather).commit();
            //if it is house garage, then go to the house garage class
        } else if (type.equals("House Garage")) {

            HouseGarageFragment garage = new HouseGarageFragment(null);
            Bundle housegarage = getIntent().getExtras();
            garage.setArguments(housegarage);
            getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, garage).commit();
            //if it is house Temperature, go to the houseTemp class
        }else if (type.equals("House Temperature")){

            HouseTemp temp = new HouseTemp(null);
            Bundle housetemp = getIntent().getExtras();
            temp.setArguments(housetemp);

            getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, temp).commit();
        }
    }

   // http://stackoverflow.com/questions/18337536/android-overriding-onbackpressed
    //this method is control the onbackpressed action for the housesetting part
    public void onBackPressed(){
        super.onBackPressed();
        Intent intentBack = new Intent(Housegarage.this, HousesettingDetail.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentBack);
        finish();
    }
}