package com.example.bo.smarthome;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Author: Bo yang
 *  Created at Apr 10 2017
 *
 * This class is the houseSetting main class for different functions
 */
public class HousesettingDetail extends AppCompatActivity {


    private ArrayList <String> arrayList;
    private ArrayAdapter<String> adapter ;
    private boolean isTablet;
    HouseGarageFragment houseGarageFragment;
    HouseWeather houseWeather;
    HouseTemp houseTemp;

    SQLiteDatabase db;
     String ACTIVITY_NAME = "HousesettingDetail";

     Context ctx;
    Cursor results;
    protected HouseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housesetting_detail);

      //  define toolbar and set supportaction
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
ctx=this;


        ListView HouseView =(ListView)findViewById(R.id.house_listview);
        String [] item = {getString(R.string.house_garage), getString(R.string.house_temp), getString(R.string.house_weather)};
      //put the string array into the ArrayList
        arrayList= new ArrayList<>(Arrays.asList(item));
        //load the ArrayList into ArrayAdapter
        adapter =new ArrayAdapter<>(this,R.layout.activity_house_setting,R.id.house_view,arrayList);
        //set ArrayAdapter
        HouseView.setAdapter(adapter);

        // is talblet of phone
        isTablet = (findViewById(R.id.house_fragholder) != null);

        //instantiate database and set the database be writable
         dbHelper=new HouseDatabaseHelper(this);
         db = dbHelper.getWritableDatabase();

              // get the Cursor with query
          results = db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[]{HouseDatabaseHelper.KEY_ID,
                        HouseDatabaseHelper.KEY_LightSwitch,
                        HouseDatabaseHelper.KEY_DoorSwitch,
                        HouseDatabaseHelper.KEY_Time,
                        HouseDatabaseHelper.Key_Temp
          },
                null, null, null, null, null, null);

              results.moveToLast();

      //ListView click listener
      HouseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("HouseView ", "onItemClick: " + i + " " + l);


                Bundle bun = new Bundle();

                    //get the database data
                    long id = results.getLong(results.getColumnIndex(HouseDatabaseHelper.KEY_ID));
                    int LightSwitch = results.getInt(results.getColumnIndex(HouseDatabaseHelper.KEY_LightSwitch));
                    int DoorSwitch = results.getInt(results.getColumnIndex(HouseDatabaseHelper.KEY_DoorSwitch));
                    String Time = results.getString(results.getColumnIndex(HouseDatabaseHelper.KEY_Time));
                    String Temp = results.getString(results.getColumnIndex(HouseDatabaseHelper.Key_Temp));

                    //id is the database _id
                    bun.putLong("_id", id);
                    // boolean DoorSwitch
                    bun.putBoolean("DoorSwitch", DoorSwitch != 0);
                    // Boolean LightSwitch
                    bun.putBoolean("LightSwitch", LightSwitch != 0);
                    //String Time
                    bun.putString("Time", Time);
                    // String temp
                    bun.putString("Temp", Temp);



                    // the listView get click, if is house wather
                    if (adapterView.getItemAtPosition(i).equals(getString(R.string.house_weather))) {

                        //if tablet
                        if (isTablet) {
                            //setting fragment for tablet
                            houseWeather = new HouseWeather(HousesettingDetail.this);
                            houseWeather.setArguments(bun);
                            getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseWeather).commit();
                        } else {

                            //if is phone,
                            Intent HouseWeatherSetting = new Intent(HousesettingDetail.this, Housegarage.class);
                            HouseWeatherSetting.putExtra("type", "House Weather");
                            startActivityForResult(HouseWeatherSetting, 5);
                        }
                        //if  house temp get clicked
                    } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.house_temp))) {
                        //if is tablet
                        if (isTablet) {
                            //fragment setting for house Temp
                            houseTemp = new HouseTemp(HousesettingDetail.this);
                            houseTemp.setArguments(bun);
                            getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseTemp).commit();

                        } else {
                            //if is phone
                            Intent HouseTempSetting = new Intent(HousesettingDetail.this, Housegarage.class);
                            HouseTempSetting.putExtras(bun);
                            HouseTempSetting.putExtra("type", "House Temperature");
                            startActivityForResult(HouseTempSetting, 5);
                        }
                        // if house garage is clicked
                    } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.house_garage))) {
                        //if is tablet
                        if (isTablet) {
                            //set fragment for house garage
                            houseGarageFragment = new HouseGarageFragment(HousesettingDetail.this);
                            houseGarageFragment.setArguments(bun);
                            getSupportFragmentManager().beginTransaction().replace(R.id.house_fragholder, houseGarageFragment).commit();

                        }
                        // if a phone, transition to empty Activity that has FrameLayout
                        else //isPhone
                        {

                            Intent HouseGarageSetting = new Intent(HousesettingDetail.this, Housegarage.class);
                            HouseGarageSetting.putExtras(bun);
                            HouseGarageSetting.putExtra("type", "House Garage");
                            startActivityForResult(HouseGarageSetting, 5);
                        }
                    }

            }
        });
    }


    //delete the time and temp for the house setting
    public void deleteschedule(long id){
//use id to delete the row of the database
        db.delete(HouseDatabaseHelper.DATABASE_NAME, "_id=" + id , null);
        //refresh database
        refreshDB();
    }

    // use to update the garage setting
    public void updateGarage(long id, boolean door, boolean light) {
//same id , to update the doorswitch and lightswitch
        ContentValues values = new ContentValues();
        values.put(HouseDatabaseHelper.KEY_DoorSwitch, door ? 1:0);
        values.put(HouseDatabaseHelper.KEY_LightSwitch,light ? 1:0);
        db.update(HouseDatabaseHelper.DATABASE_NAME, values, HouseDatabaseHelper.KEY_ID + "=" + id, null);
        refreshDB();
    }

//update new time and temperature
    public void updateTemp(long id,  String Time, String Temp) {

        ContentValues values = new ContentValues();
        values.put(HouseDatabaseHelper.KEY_Time,Time);
        values.put(HouseDatabaseHelper.Key_Temp,Temp);

        db.insert(HouseDatabaseHelper.DATABASE_NAME, null, values);
        refreshDB();
    }
// update temp only with the same time
    public void EditTemp(long id,  String Temp) {

        ContentValues values = new ContentValues();
        values.put(HouseDatabaseHelper.Key_Temp,Temp);
        db.update(HouseDatabaseHelper.DATABASE_NAME,  values,HouseDatabaseHelper.KEY_ID + "=" + id, null);
        refreshDB();
    }


//use for phone to update
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Bundle bun = data.getExtras();


//results  code 0 from garage setting
        if (requestCode==5 && resultCode == 0) {
            Long id = bun.getLong("_id");
            boolean DoorSwitch = bun.getBoolean("DoorSwitch");
            boolean LightSwitch = bun.getBoolean("LightSwitch");
            updateGarage(id, DoorSwitch,LightSwitch);


        }

        //ressult code i from house Temp setting for update time and temp

        if (requestCode==5 && resultCode == 1) {
            Long id = bun.getLong("_id");
            String times =bun.getString("Time");
            String temps =bun.getString("Temp");
            updateTemp(id,  times,temps);

        }

        //result code 2 from house temp setting for delete time and temp
        if (requestCode==5 && resultCode == 2) {
            Long id = bun.getLong("_id");
                deleteschedule(id);
        }
 //result code 3 from house temp setting for update only temp with the same time
        if (requestCode==5 && resultCode == 3) {
            Long id = bun.getLong("_id");
            String temps = bun.getString("Temp");
            EditTemp(id, temps);
        }
        }

        //refresh the database
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
        results.moveToLast(); //move to first result
    }


//remove the house temp fragment
    public void removetempFragment() {
        getSupportFragmentManager().beginTransaction().remove(houseTemp).commit();
    }
//remove garageFragement
    public void removeFragment() {
        getSupportFragmentManager().beginTransaction().remove(houseGarageFragment).commit();
    }




//close the database
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

// create the menu for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.house_menu, menu);
        return true;

    }

    //option selected
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id)
        {
          //living roon
            case R.id.living:

                startActivity(new Intent(this, LivingroomList.class));

                break;
            //kitchen
            case R.id.kitchen:


                Intent itntKitchenMain = new Intent(this, KitchenMain.class);
                startActivity(itntKitchenMain);


                startActivity(new Intent(this, KitchenMain.class));

                break;

            //house help menu
            case R.id.househelp:




                 //alertDialog show the introduction of the house setting
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(getString(R.string.house_introduction)).setMessage(getString(R.string.house_introductiondetail))
                        .setNegativeButton(getString(R.string.house_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        });
                builder.create().show();

                break;
                  //Auto car
            case R.id.car:

                startActivity(new Intent(this, AutoListView.class));

                break;
        }
        return true;
    }
}
