package com.example.bo.smarthome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * This class creates the microwave user interface
 * Assignment: Project SmartHome
 * Professor: Eric Torunski
 * author: Qiuju Zhu
 */

public class KitchenMicrowaveDetail extends KitchenBase {

    /**
     * Display help Menu "About" message. Shows the instruction, version number and author.
     */
    @Override
    protected void showHelp()
    {
        android.app.AlertDialog.Builder kitchenbase_builder = new android.app.AlertDialog.Builder(KitchenMicrowaveDetail.this);
        kitchenbase_builder.setTitle(R.string.kitchen_toobar_welcome_text)
                .setMessage(R.string.kitchen_microwave_instruction)
                .setNegativeButton(R.string.kitchen_toolbar_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("No", "No");
                    }
                });
        kitchenbase_builder.create().show();

    }

    /**
     * Start the Microwave Android activity.
     * @param savedInstanceState object of Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_microwave_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenmicrowave);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        Bundle bun = new Bundle();
        bun.putInt("applianceId", extras.getInt("applianceId"));
        bun.putString("applianceName", extras.getString("applianceName"));

        KitchenMicrowaveFragment fmg = new KitchenMicrowaveFragment();
        loadKitchenFragment(fmg, R.id.frmKitchenMicrowaveDetailPortrait, bun);

    }



}
