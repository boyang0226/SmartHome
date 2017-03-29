package com.example.bo.smarthome;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class KitchenMicrowaveDetail extends KitchenBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_microwave_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenmicrowave);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

    }

}
