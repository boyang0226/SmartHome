package com.example.bo.smarthome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class KitchenMain extends KitchenBase {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenmain);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

        populateListView(logTag);

        handleListViewItemClick(logTag);

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

    private void handleListViewItemClick(String logTag) {


        final ListView applianceListView = (ListView) findViewById(R.id.lvKitchenAppliance);
        applianceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String tag =ACTIVITY_NAME + "ListView Clicked.";
                //Bundle bun = new Bundle();
                //bun.putLong("ID", id );
                //String msg = chatList.getItemAtPosition(position).toString();
                //bun.putString("Msg", msg );

                String selectedAppliance = ((TextView)view).getText().toString();

                switch(selectedAppliance) {
                    case "Microwave":
                        Log.d(tag, "Clicked Microwave.");
                        callActivity(KitchenMicrowaveDetail.class);
                        break;
                    case "Fridge":
                        Log.d(tag, "Clicked Fridge.");
                        callActivity(KitchenFridgeDetail.class);
                        break;
                    case "Main light":
                        Log.d(tag, "Clicked Main light.");
                        callActivity(KitchenLightDetail.class);
                        break;
                    // case R.id.action_settings:
                    //   Log.d("Toolbar", "Version 1.0, by Qiuju Zhu");
                    //   break;
                    default:
                        break;
                }

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

                String logTag1 = ADDAPPLIANCEBTN;
                Log.d(logTag1, "Add Appliance button clicked.");

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




}
