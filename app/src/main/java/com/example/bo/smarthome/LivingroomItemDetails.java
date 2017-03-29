package com.example.bo.smarthome;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LivingroomItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livingroom_item_details);

        //Step 3, create fragment onCreation, pass data from Intent Extras to FragmentTransction
        LivingroomFragment frag = new LivingroomFragment(null);
        final Bundle bun = getIntent().getExtras();
        frag.setArguments( bun );
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.add(R.id.fragment_holder, frag).commit();
    }
}
