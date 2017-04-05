package com.example.bo.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutoItemDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_item_details);

        //Step 3, create fragment onCreation, pass data from Intent Extras to FragmentTransction
        AutoTemperatureFragment frag = new AutoTemperatureFragment(null);
        Bundle bun = getIntent().getExtras();
        frag.setArguments( bun );
        getSupportFragmentManager().beginTransaction().add(R.id.auto_FragmentHolder, frag).commit();
    }
}
