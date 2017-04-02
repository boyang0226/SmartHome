package com.example.bo.smarthome;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AutoListView extends AppCompatActivity {
    Context ctx;
    ListView listView;
    EditText editText;
    Spinner spinner;
    Button addButton;
    ArrayList<String> autoUnit = new ArrayList<String>();
    ArrayAdapter<String> listAdapter;
    protected static final String ACTIVITY_NAME = "AutoListView";
    protected AutoDatabaseHelper dbHelper;
    protected Boolean isTablet;
    Cursor results;
    //MessageFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.auto_Toolbar);
        setSupportActionBar(toolbar);
        ctx = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.auto_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView autoListView =(ListView)findViewById(R.id.auto_ListView);

        String [] item = {"Auto Temp", "Auto Normal Light", "Auto GPS"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(item));
        listAdapter =new ArrayAdapter <> (this,R.layout.activity_auto_list_view_item,R.id.auto_AddedListView,arrayList);
        autoListView.setAdapter(listAdapter);

        addButton = (Button) findViewById(R.id.autoListViewAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ctx);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();
                final View v = inflater.inflate(R.layout.activity_auto_add_new_unit, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder2.setView(inflater.inflate(R.layout.activity_auto_add_new_unit, null))
                        // Add action buttons
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
                                Spinner spinner = (Spinner) v.findViewById(R.id.auto_AddNewUnitSpinner);
                                Log.i("Text is:", spinner.getSelectedItem().toString());
                                String newAutoUnit = spinner.getSelectedItem().toString();

                                listAdapter.remove(newAutoUnit);
                                listAdapter.add(newAutoUnit);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        });
                builder2.setView(v);
                builder2.create().show();
            }
        });

        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (adapterView.getItemAtPosition(i).equals("Auto GPS")) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=city+hall, Ottawa, ON");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu m) { //put the menu in the toolbar

        getMenuInflater().inflate(R.menu.auto_menu,  m); //makes m look like toolbar_menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {       //when an item was clicked
        // Handle presses on the action bar items
        switch (mi.getItemId()) {
            case R.id.auto_living:
                Log.i("Navigation", "1");
                Intent livingIntro = new Intent(this, LivingroomList.class);
                startActivityForResult(livingIntro, 1);

                break;

            case R.id.auto_kitchen:
                Log.i("Navigation", "2");
                Intent itntKitchenMain = new Intent(this, KitchenMain.class);
                startActivityForResult(itntKitchenMain, 5);
                break;

            case R.id.auto_house:
                Log.i("Navigation", "3");
                startActivity(new Intent(AutoListView.this, HousesettingDetail.class));
                break;

            case R.id.auto_helpMenu:
                Log.i("Navigation", "4");
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Welcome to Smart Home Auto Setting").setMessage("This is the Auto Setting part of the Smart Home App. You can use 'Add' button create your new component of a car. Version 1.0 by Zhen Qu.")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        });
                builder.create().show();
                break;
        }
        return true;
    }
}