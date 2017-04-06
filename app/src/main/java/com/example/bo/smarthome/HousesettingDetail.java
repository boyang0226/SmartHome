package com.example.bo.smarthome;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;


public class HousesettingDetail extends AppCompatActivity {

    private ArrayList <String> arrayList;
    private ArrayAdapter<String> adapter ;
    private boolean isTablet;
    HouseGarageFragment houseGarageFragment;

    SQLiteDatabase db;
     String ACTIVITY_NAME = "HousesettingDetail";


    Cursor results;
    protected HouseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housesetting_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isTablet = (findViewById(R.id.house_fragholder) != null);


         dbHelper=new HouseDatabaseHelper(this);
         db = dbHelper.getWritableDatabase();




          results = db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[]{HouseDatabaseHelper.KEY_ID,
                        HouseDatabaseHelper.KEY_LightSwitch,
                        HouseDatabaseHelper.KEY_DoorSwitch,
                        HouseDatabaseHelper.KEY_Time,
                        HouseDatabaseHelper.Key_Temp},
                null, null, null, null, null, null);






        results.moveToFirst();







        ListView HouseView =(ListView)findViewById(R.id.house_listview);

        String [] item = {"House Garage", "House Temperature", "House Weather"};
        arrayList= new ArrayList<>(Arrays.asList(item));
        adapter =new ArrayAdapter<>(this,R.layout.activity_house_setting,R.id.house_view,arrayList);
        HouseView.setAdapter(adapter);


        HouseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("HouseView ", "onItemClick: " + i + " " + l);
                long id = results.getLong(results.getColumnIndex(HouseDatabaseHelper.KEY_ID));
                int LightSwitch =results.getInt(results.getColumnIndex(HouseDatabaseHelper.KEY_LightSwitch));
                int DoorSwitch =results.getInt(results.getColumnIndex(HouseDatabaseHelper.KEY_DoorSwitch));
                String Time =results.getString(results.getColumnIndex(HouseDatabaseHelper.KEY_Time));
                String Temp =results.getString(results.getColumnIndex(HouseDatabaseHelper.KEY_Time));



                Bundle bun = new Bundle();
                bun.putLong("_id", id);//l is the database ID of selected item
                bun.putBoolean("DoorSwitch",DoorSwitch!=0);
                bun.putBoolean("LightSwitch",LightSwitch!=0);
                bun.putString("Time",Time);
                bun.putString("Temp",Temp);




                if (adapterView.getItemAtPosition(i).equals("House Weather")) {


                    Intent HouseWeatherSetting = new Intent(HousesettingDetail.this, HouseWeather.class);
                    startActivityForResult(HouseWeatherSetting, 5);

                }else if (adapterView.getItemAtPosition(i).equals("House Temperature")) {


                    Intent HouseTempSetting = new Intent(HousesettingDetail.this, HouseTemp.class);
           //        HouseTempSetting.putExtra("Time",time);
            //       HouseTempSetting.putExtra("Temp",temp);
            //          HouseTempSetting.putExtras(bun);
                    startActivityForResult(HouseTempSetting, 5);

                }else  if (adapterView.getItemAtPosition(i).equals("House Garage")) {

                    if (isTablet) {

                        houseGarageFragment = new HouseGarageFragment(HousesettingDetail.this);

                        houseGarageFragment.setArguments(bun);


                        getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseGarageFragment).commit();

                    }
                    //step 3 if a phone, transition to empty Activity that has FrameLayout
                    else //isPhone
                    {
                        Intent intnt = new Intent(HousesettingDetail.this, Housegarage.class);
                        intnt.putExtra("_id", id);
                        intnt.putExtra("DoorSwitch",DoorSwitch);
                        intnt.putExtra("LightSwitch",LightSwitch);



                        startActivityForResult(intnt,5);
                    }
                }
            }
        });
    }


    public void updateGarage(long id, boolean door, boolean light) {

        ContentValues values = new ContentValues();
        values.put(HouseDatabaseHelper.KEY_DoorSwitch, door ? 1:0);
        values.put(HouseDatabaseHelper.KEY_LightSwitch,light ? 1:0);
        db.update(HouseDatabaseHelper.DATABASE_NAME, values, HouseDatabaseHelper.KEY_ID + "=" + id, null);
        refreshDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Bundle bun = data.getExtras();
        Long id = bun.getLong("_id");
        boolean DoorSwitch = bun.getBoolean("DoorSwitch");
        boolean LightSwitch = bun.getBoolean("LightSwitch");
        String toastText = bun.getString("Response");

        if (requestCode==5 && resultCode == 0) {
            updateGarage(id, DoorSwitch,LightSwitch);

            Toast toast = Toast.makeText(HousesettingDetail.this, toastText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void refreshDB() {
        dbHelper = new HouseDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        results = db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[] {  HouseDatabaseHelper.KEY_ID,
                        HouseDatabaseHelper.KEY_LightSwitch,
                        HouseDatabaseHelper.KEY_DoorSwitch,
                        HouseDatabaseHelper.KEY_Time,
                        HouseDatabaseHelper.Key_Temp  },
                null, null, null, null, null, null);

        int rows = results.getCount(); //number of rows returned
        results.moveToFirst(); //move to first result
    }




    public void removeFragment() {
        getSupportFragmentManager().beginTransaction().remove(houseGarageFragment).commit();
    }

    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
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
                Intent livingIntro = new Intent(this, LivingroomList.class);
                startActivityForResult(livingIntro, 1);

                break;
            case R.id.kitchen:

                Intent itntKitchenMain = new Intent(this, KitchenMain.class);
                startActivityForResult(itntKitchenMain, 5);
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

                startActivity(new Intent(this, AutoListView.class));

                break;
        }
        return true;
    }
}
