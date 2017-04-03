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

    private static final String ACTIVITY_NAME = "KitchenLight_";
    private LightSetting lightSetting;
    SQLiteDatabase db = null;

    private class LightSetting {
        private int id = 0;
        private boolean mainSwitch = false;
        private int dimmerLevel = 100;



        public LightSetting()
        {

        }
        public LightSetting(int id, boolean mainSwitch, int dimmerLevel)
        {   this.id = id;
            this.mainSwitch = mainSwitch;
            this.dimmerLevel = dimmerLevel;
        }
        public int getId()
        {
            return id;
        }

        public boolean getMainSwitch()
        {
            return mainSwitch;
        }
        public void setMainSwitch(boolean isChecked)
        {
            this.mainSwitch = isChecked;
        }

        public int getDimmerLevel()
        {
            return dimmerLevel;
        }

        public void setDimmerLevel(int dimmerLevel)
        {
            this.dimmerLevel = dimmerLevel;
        }
    }

    protected KitchenDatabaseHelper dbHelper;
    Cursor results;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_light_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenlight);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        KitchenLightFragment frg = new KitchenLightFragment();
        ft.replace(R.id.frmKitchenDetailPortrait, frg);
        ft.commit();
        }
}
