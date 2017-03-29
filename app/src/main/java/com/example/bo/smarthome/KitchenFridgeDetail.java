package com.example.bo.smarthome;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class KitchenFridgeDetail extends KitchenBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_fridge_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenfridge);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

    }

}
