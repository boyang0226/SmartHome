package com.example.bo.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HouseSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_setting);

        Toolbar tb2 =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb2);

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

            case R.id.person:

                Intent person = new Intent(HouseSetting.this, person.class);
                startActivityForResult(person, 5);

                break;
            case R.id.living:
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();

                break;
            case R.id.kitchen:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
            case R.id.house:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();


                break;
            case R.id.car:

                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}

