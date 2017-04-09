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

        HouseGarageFragment frag = new HouseGarageFragment(null);
        Bundle bun = getIntent().getExtras();
        frag.setArguments( bun );
        getSupportFragmentManager().beginTransaction().add(R.id.house_fragholder, frag).commit();
    }
}
