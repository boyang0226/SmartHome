package com.example.bo.smarthome;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.FragmentTransaction;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class HousesettingDetail extends AppCompatActivity {

   private ArrayList <String> arrayList;
    private ArrayAdapter<String> adapter ;
    private HouseWeather weatherFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housesetting_detail);



        Toolbar tb =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);


        Button add =(Button)findViewById(R.id.house_add) ;

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(HousesettingDetail.this);

                LayoutInflater inflater = HousesettingDetail.this.getLayoutInflater();

                final View dlgView = inflater.inflate(R.layout.house_add_dialog, null);
                dlgBuilder.setView(dlgView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Spinner spnAppliance = (Spinner)dlgView.findViewById(R.id.house_add_spinner);
                                String newAppliance = spnAppliance.getSelectedItem().toString();

                                adapter.remove(newAppliance);
                                adapter.add(newAppliance);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dlgAddAppliance = dlgBuilder.create();
                dlgAddAppliance.show();
            }
        });


        ListView HouseView =(ListView)findViewById(R.id.house_listview);

        String [] item = {"House Garage", "House Temperature", "House Weather"};
        arrayList= new ArrayList<>(Arrays.asList(item));
        adapter =new ArrayAdapter<>(this,R.layout.house_setting,R.id.house_view,arrayList);
        HouseView.setAdapter(adapter);

        weatherFragment = new HouseWeather();


        HouseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (adapterView.getItemAtPosition(i).equals("Weather")) {
                    Intent intent = new Intent(HousesettingDetail.this, HouseWeather.class);
                    startActivityForResult(intent, 5);

                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.house_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id)
        {

            case R.id.living:


                break;
            case R.id.kitchen:


                break;
            case R.id.househelp:


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.house_introduction)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                builder.create().show();
                break;
            case R.id.car:

//
                break;
        }
        return true;
    }
}

