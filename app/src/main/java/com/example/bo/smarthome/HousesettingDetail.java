package com.example.bo.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HousesettingDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housesetting_detail);

        Toolbar tb =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);


        Button outsideWeather = (Button)findViewById(R.id.houseweather);

        outsideWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Weather = new Intent(HousesettingDetail.this, HouseWeather.class);
                startActivityForResult(Weather, 5);

            }
        });

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.house_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id)
        {



            case R.id.living:
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();

                break;
            case R.id.kitchen:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
            case R.id.househelp:

                Intent HouseSetting = new Intent(HousesettingDetail.this, HouseSetting.class);
                startActivityForResult(HouseSetting, 5);

                break;
            case R.id.car:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }




}
