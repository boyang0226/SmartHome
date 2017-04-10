package com.example.bo.smarthome;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;


public class HousesettingDetail extends AppCompatActivity {
    Context ctx;
    private ArrayList <String> arrayList;
    private ArrayAdapter<String> adapter ;
    private boolean isTablet;
    HouseGarageFragment houseGarageFragment;
    HouseWeather houseWeather;
    HouseTemp houseTemp;

    SQLiteDatabase db;
     String ACTIVITY_NAME = "HousesettingDetail";


    Cursor results;
    protected HouseDatabaseHelper dbHelper;

    long id ;
    int LightSwitch ;
    int DoorSwitch ;
  //  String Schedule ;
    String Time;
    String Temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housesetting_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx=this;


        ListView HouseView =(ListView)findViewById(R.id.house_listview);
       //String [] item = {"House Garage", "House Temperature", "House Weather"};
      //  String[]item = R.array.house_spinner_add;
        String [] item = {getString(R.string.house_garage), getString(R.string.house_temp), getString(R.string.house_weather)};

        arrayList= new ArrayList<>(Arrays.asList(item));
        adapter =new ArrayAdapter<>(this,R.layout.activity_house_setting,R.id.house_view,arrayList);
        HouseView.setAdapter(adapter);

        isTablet = (findViewById(R.id.house_fragholder) != null);


         dbHelper=new HouseDatabaseHelper(this);
         db = dbHelper.getWritableDatabase();


          results = db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[]{HouseDatabaseHelper.KEY_ID,
                        HouseDatabaseHelper.KEY_LightSwitch,
                        HouseDatabaseHelper.KEY_DoorSwitch,
        //              HouseDatabaseHelper.KEY_Schedule
                        HouseDatabaseHelper.KEY_Time,
                        HouseDatabaseHelper.Key_Temp
          },
                null, null, null, null, null, null);

              results.moveToFirst();


      HouseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("HouseView ", "onItemClick: " + i + " " + l);


                Bundle bun = new Bundle();
                while(!results.isAfterLast()) {
                    id = results.getLong(results.getColumnIndex(HouseDatabaseHelper.KEY_ID));
                     LightSwitch = results.getInt(results.getColumnIndex(HouseDatabaseHelper.KEY_LightSwitch));
                     DoorSwitch = results.getInt(results.getColumnIndex(HouseDatabaseHelper.KEY_DoorSwitch));
   //                  Schedule = results.getString(results.getColumnIndex(HouseDatabaseHelper.KEY_Schedule));
                    Time=results.getString(results.getColumnIndex(HouseDatabaseHelper.KEY_Time));
                    Temp=results.getString(results.getColumnIndex(HouseDatabaseHelper.Key_Temp));
                    results.moveToNext();
                }
                    bun.putLong("_id", id);//l is the database ID of selected item
                    bun.putBoolean("DoorSwitch", DoorSwitch != 0);
                    bun.putBoolean("LightSwitch", LightSwitch != 0);
                    bun.putString("Time",Time);
                    bun.putString("Temp",Temp);
//                    bun.putString("type", arrayList.get(i));

                if (adapterView.getItemAtPosition(i).equals(getString(R.string.house_weather))) {
                   if (isTablet) {
                        houseWeather = new HouseWeather(HousesettingDetail.this);
                        houseWeather.setArguments(bun);


                       getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseWeather).commit();
                   }else {
                       Intent HouseWeatherSetting = new Intent(HousesettingDetail.this, Housegarage.class);

                       HouseWeatherSetting.putExtra("type", "House Weather");
                       HouseWeatherSetting.putExtras(bun);
                       startActivityForResult(HouseWeatherSetting, 5);
                   }

                }else if (adapterView.getItemAtPosition(i).equals(getString(R.string.house_temp))) {

                    if (isTablet) {

                        houseTemp = new HouseTemp(HousesettingDetail.this);
                        houseTemp.setArguments(bun);
                       getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseTemp).commit();

                    }else {
                        Intent HouseTempSetting = new Intent(HousesettingDetail.this, Housegarage.class);
                        HouseTempSetting.putExtras(bun);
                        HouseTempSetting.putExtra("type", "House Temperature");
                        startActivityForResult(HouseTempSetting, 5);
                    }
                }else  if (adapterView.getItemAtPosition(i).equals(getString(R.string.house_garage)) ){

                    if (isTablet) {
                        houseGarageFragment = new HouseGarageFragment(HousesettingDetail.this);
                        houseGarageFragment.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseGarageFragment).commit();

                    }
                    //step 3 if a phone, transition to empty Activity that has FrameLayout
                    else //isPhone
                    {
                        Intent HouseGarageSetting = new Intent(HousesettingDetail.this, Housegarage.class);
//
                        HouseGarageSetting.putExtras(bun);
                        HouseGarageSetting.putExtra("type", "House Garage");
                        startActivityForResult(HouseGarageSetting,5);
                    }
                }
            }
        });
    }


    public void deleteschedule(long id){

        db.delete(HouseDatabaseHelper.DATABASE_NAME, "_id=" + id , null);
        refreshDB();
    }

    public void updateGarage(long id, boolean door, boolean light) {

        ContentValues values = new ContentValues();
        values.put(HouseDatabaseHelper.KEY_DoorSwitch, door ? 1:0);
        values.put(HouseDatabaseHelper.KEY_LightSwitch,light ? 1:0);
        db.update(HouseDatabaseHelper.DATABASE_NAME, values, HouseDatabaseHelper.KEY_ID + "=" + id, null);
        refreshDB();
    }

    public void updateTemp(long id,  String Time, String Temp) {

        ContentValues values = new ContentValues();
      //  values.put(HouseDatabaseHelper.KEY_Schedule,schedule);
        values.put(HouseDatabaseHelper.KEY_Time,Time);
        values.put(HouseDatabaseHelper.Key_Temp,Temp);
        db.insert(HouseDatabaseHelper.DATABASE_NAME, null, values);
        refreshDB();
    }

    public void EditTemp(long id,  String Temp) {

        ContentValues values = new ContentValues();
        //  values.put(HouseDatabaseHelper.KEY_Schedule,schedule);
    //    values.put(HouseDatabaseHelper.KEY_Time,Time);
        values.put(HouseDatabaseHelper.Key_Temp,Temp);
        db.update(HouseDatabaseHelper.DATABASE_NAME,  values,HouseDatabaseHelper.KEY_ID + "=" + id, null);
        refreshDB();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Bundle bun = data.getExtras();



        if (requestCode==5 && resultCode == 0) {
            Long id = bun.getLong("_id");
            boolean DoorSwitch = bun.getBoolean("DoorSwitch");
            boolean LightSwitch = bun.getBoolean("LightSwitch");
            updateGarage(id, DoorSwitch,LightSwitch);


        }

        if (requestCode==5 && resultCode == 1) {
            Long id = bun.getLong("_id");
            String times =bun.getString("Time");
            String temps =bun.getString("Temp");
            updateTemp(id,  times,temps);
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
                        HouseDatabaseHelper.Key_Temp
                },
                null, null, null, null, null, null);

        int rows = results.getCount(); //number of rows returned
        results.moveToFirst(); //move to first result
    }



    public void removetempFragment() {
        getSupportFragmentManager().beginTransaction().remove(houseTemp).commit();
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


//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(R.string.house_introduction)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//
//                            }
//                        });
//                builder.create().show();

                Dialog  dialog=new Dialog(this);
                dialog.setTitle(getString(R.string.house_introduction));
                dialog.setContentView(R.layout.activity_house_help_dialog);
                TextView editText =(TextView)dialog.findViewById(R.id.house_view2);
                editText.setText(R.string.house_introductiondetail);

                dialog.show();

                break;

            case R.id.car:

                startActivity(new Intent(this, AutoListView.class));

                break;
        }
        return true;
    }
}
