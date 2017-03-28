package com.example.bo.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id)
        {

            case R.id.living:


                break;
            case R.id.kitchen:
                Intent itntKitchenMain = new Intent(MainActivity.this, KitchenMain.class);
                startActivityForResult(itntKitchenMain, 5);
                break;
            case R.id.house:


                Intent HouseSetting = new Intent(MainActivity.this, HouseSetting.class);
                startActivityForResult(HouseSetting, 5);

                break;
            case R.id.car:

//                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}
