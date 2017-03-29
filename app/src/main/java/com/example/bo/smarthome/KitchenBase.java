package com.example.bo.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class KitchenBase extends AppCompatActivity {

    private String logTag = "KitchenBase";

    protected void setLogTag(String logTag)
    {
        this.logTag = logTag;
    }

    protected void setToolbarColor(Toolbar tbar)
    {
        tbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorSHNavigationBar));

    }

    protected void callActivity(Class<?> cls)    {

        Intent intent = new Intent(getBaseContext(), cls);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.kitchen_toolbar_menu, m );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {

        int id;
        id = mi.getItemId();

        switch(id) {
            case R.id.kitchen_car:
                Log.d(logTag, "Switch to Automobile from Kitchen.");
                //callActivity(Automobile.class);
                break;
            case R.id.kitchen_house:
                Log.d(logTag, "Switch to House Setting from Kitchen.");
                callActivity(HouseSetting.class);
                break;
            case R.id.kitchen_living:
                Log.d(logTag, "Switch to Living Room from Kitchen.");
                //callActivity(LivingRoom.class);
                break;
            // case R.id.action_settings:
            //   Log.d("Toolbar", "Version 1.0, by Qiuju Zhu");
            //   break;
            default:
                break;
        }
        return true;
    }

}
