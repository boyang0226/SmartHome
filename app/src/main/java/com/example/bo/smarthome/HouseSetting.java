package com.example.bo.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HouseSetting extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_setting);

        Toolbar tb2 =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb2);


        TextView tv = (TextView)findViewById(R.id.HouseTitle);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setText("\nWelcome to Smart house setting \n\n\nAuthor: Bo Yang" +
                   "\n\n This app is about the house setting, it will blabla");


        Button start =(Button)findViewById(R.id.HouseStart);
        start.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {



                Intent HouseSetting = new Intent(HouseSetting.this, HousesettingDetail.class);
                startActivityForResult(HouseSetting, 5);
            }
        });

}



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
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();

                break;
            case R.id.kitchen:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
            case R.id.house:

                Intent HouseSetting = new Intent(HouseSetting.this, HouseSetting.class);
                startActivityForResult(HouseSetting, 5);


                break;
            case R.id.car:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }





}

