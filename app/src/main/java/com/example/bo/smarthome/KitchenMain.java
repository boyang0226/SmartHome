package com.example.bo.smarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class KitchenMain extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "KitchenMain_";
    protected static final String TOOLBAR_AREA = "Toolbar";
    protected static final String ONCREATE = "onCreate";
    protected static final String ADDAPPLIANCEBTN = "AddApplianceButton";

    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String logTag = ACTIVITY_NAME + ONCREATE;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateListView(logTag);
        addAppliance(logTag);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void populateListView(String logTag)   {

        Log.d(logTag, "Populating appliance list view.");

        ListView kitchenListview  = (ListView) findViewById(R.id.lvKitchenAppliance );

        //dummy data
        String[] planets = new String[] { "Microwave", "Fridge", "Main light"};
        ArrayList<String> applianceList = new ArrayList<>();
        applianceList.addAll( Arrays.asList(planets) );
        listAdapter = new ArrayAdapter<>(this, R.layout.kitchen_main_listview_row, applianceList);
        kitchenListview.setAdapter( listAdapter );
    }

    private void addAppliance(String logTag)
    {
        Log.d(logTag, "Add appliance button event handler.");

        Button btnAddAppliance = (Button) findViewById(R.id.btnKitchenMainAdd);
        btnAddAppliance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String logTag = ADDAPPLIANCEBTN;

                Log.d(logTag, "Add Appliance button clicked.");

                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(KitchenMain.this);
                LayoutInflater inflater = KitchenMain.this.getLayoutInflater();

                final View dlgView = inflater.inflate(R.layout.kitchen_main_dialog_custom, null);
                dlgBuilder.setView(dlgView)
                        .setPositiveButton(R.string.dialog_positive_text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                               Spinner spnAppliance = (Spinner)dlgView.findViewById(R.id.spnKitchenAddAppliance);
                                String newAppliance = spnAppliance.getSelectedItem().toString();

                                listAdapter.remove(newAppliance);
                                listAdapter.add(newAppliance);
                            }
                        }).setNegativeButton(R.string.dialog_negative_text_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dlgAddAppliance = dlgBuilder.create();
                dlgAddAppliance.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.kitchen_toolbar_menu, m );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {

        String logTag = ACTIVITY_NAME + TOOLBAR_AREA;

        int id;
        id = mi.getItemId();

        switch(id) {
            case R.id.kitchen_car:
                Log.d(logTag, "Switch to Automobile from Kitchen.");
                //callActivity(Automobile.class);
                break;
            case R.id.kitchen_house:
                Log.d(logTag, "Switch to House Setting from Kitchen.");
                callActivity(HousesettingDetail.class);
                break;
            case R.id.kitchen_living:
                Log.d(logTag, "Switch to Living Room from Kitchen.");
                //callActivity(LivingRoom.class);
                break;
           // case R.id.action_settings:
             //   Log.d("Toolbar", "Version 1.0, by Qiuju Zhu");
             //   break;
            default:
                break;
        }
        return true;
    }

    private void callActivity(Class<?> cls)
    {
        Intent intent = new Intent(KitchenMain.this, cls);
        startActivity(intent);
    }


}
