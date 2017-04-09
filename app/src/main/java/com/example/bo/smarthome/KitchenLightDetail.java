package com.example.bo.smarthome;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

public class KitchenLightDetail extends KitchenBase {

    /**
     * Display the help menu for the light. Shows instruction, version number and author.
     */
    @Override
    protected void showHelp()
    {
        android.app.AlertDialog.Builder kitchenbase_builder = new android.app.AlertDialog.Builder(KitchenLightDetail.this);
        kitchenbase_builder.setTitle("Welcome to Smart Home Kitchen Setting")
                .setMessage("Click the switch to turn on/off the kitchen main light. Slide or enter to set the dimmer as you wish. Version 1.0 by Qiuju Zhu.")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("No", "No");
                    }
                });
        kitchenbase_builder.create().show();

    }

    /**
     * Start the activity of Kitchen light.
     * @param savedInstanceState object of Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_light_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenlight);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        Bundle bun = new Bundle();
        bun.putInt("applianceId", extras.getInt("applianceId"));
        bun.putString("applianceName", extras.getString("applianceName"));

        KitchenLightFragment frg = new KitchenLightFragment();
        loadKitchenFragment(frg, R.id.frmKitchenDetailPortrait, bun);
    }
}
