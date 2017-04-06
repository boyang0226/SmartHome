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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;


public class AutoListView extends AppCompatActivity {
    Context ctx;
    ArrayList<String> autoArray = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    protected static final String ACTIVITY_NAME = "AutoListView";
    protected AutoDatabaseHelper dbHelper;
    public SQLiteDatabase db;
    protected Boolean isTablet;
    Cursor results;
    AutoTemperatureFragment frag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.auto_Toolbar);
        setSupportActionBar(toolbar);
        ctx = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.auto_fab);
        Snackbar.make(fab, "Welcome to Auto Settings", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ListView autoListView =(ListView)findViewById(R.id.auto_ListView);

        String [] item = {"Auto Temperature", "Auto Lights", "Auto GPS", "Auto Radio Stations"};
        autoArray = new ArrayList<>(Arrays.asList(item));
        listAdapter =new ArrayAdapter <> (this,R.layout.activity_auto_list_view_item,R.id.auto_AddedListView,autoArray);
        autoListView.setAdapter(listAdapter);

        isTablet = (findViewById(R.id.auto_FragmentHolder)!=null);
        dbHelper = new AutoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        results = db.query(false, AutoDatabaseHelper.DATABASE_NAME,
                new String[] { AutoDatabaseHelper.KEY_ID, AutoDatabaseHelper.KEY_GPS_ENTRY, AutoDatabaseHelper.KEY_TEMPERATURE, AutoDatabaseHelper.KEY_NORMAL_SWITCH,
                               AutoDatabaseHelper.KEY_HEAD_SWITCH, AutoDatabaseHelper.KEY_INSIDE_BRIGHTNESS, AutoDatabaseHelper.KEY_RADIO_VOLUME, AutoDatabaseHelper.KEY_RADIO_MUTE,
                               AutoDatabaseHelper.KEY_RADIO_STATION_ONE, AutoDatabaseHelper.KEY_RADIO_STATION_TWO, AutoDatabaseHelper.KEY_RADIO_STATION_THREE, AutoDatabaseHelper.KEY_RADIO_STATION_FOUR,
                               AutoDatabaseHelper.KEY_RADIO_STATION_FIVE, AutoDatabaseHelper.KEY_RADIO_STATION_SIX}, null, null, null, null, null, null);
        results.moveToLast();

        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("autoListView", "onItemClick: " + i + " " + l);
                Bundle bun = new Bundle();
                long id = results.getLong(results.getColumnIndex(AutoDatabaseHelper.KEY_ID));
                String gpsEntry = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_GPS_ENTRY));
                String temp = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_TEMPERATURE));
                String normalSwitch = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_NORMAL_SWITCH));
                String headSwitch = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_HEAD_SWITCH));
                String insideBrightness = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_INSIDE_BRIGHTNESS));
                String volumn = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_VOLUME));
                String mute = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_MUTE));
                String radiaostation1 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_ONE));
                String radiaostation2 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_TWO));
                String radiaostation3 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_THREE));
                String radiaostation4 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_FOUR));
                String radiaostation5 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_FIVE));
                String radiaostation6 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_SIX));

                bun.putLong("id",id);
                bun.putString("GPSEntry",gpsEntry);
                bun.putString("Temperature",temp);
                bun.putString("NormalSwitch",normalSwitch);
                bun.putString("HeadSwitch",headSwitch);
                bun.putString("InsideBrightness",insideBrightness);
                bun.putString("Volumn",volumn);
                bun.putString("Mute",mute);
                bun.putString("Station1",radiaostation1);
                bun.putString("Station2",radiaostation2);
                bun.putString("Station3",radiaostation3);
                bun.putString("Station4",radiaostation4);
                bun.putString("Station5",radiaostation5);
                bun.putString("Station6",radiaostation6);

                if (adapterView.getItemAtPosition(i).equals("Auto Temperature")) {
                    if (isTablet) {
                        frag = new AutoTemperatureFragment(AutoListView.this);
                        frag.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.auto_FragmentHolder, frag).commit();
                    }else {
                        Intent intnt = new Intent(AutoListView.this, AutoItemDetails.class);
                        intnt.putExtra("id", id);
                        intnt.putExtra("Temperature", temp);//pass the Database ID and message to next activity
                        intnt.putExtra("Function", "temperature");
                        startActivityForResult(intnt, 5); //go to view fragment details
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.car1)
                                    .setContentTitle("Notification")
                                    .setContentText("Auto Temperature Setting");

                    //where to go if clicked
                    Intent resultIntent = new Intent(ctx, AutoItemDetails.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity( ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    //now show the notification:
                    int mNotificationId = 0634;
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }

                else if (adapterView.getItemAtPosition(i).equals("Auto GPS")) {
                    if (isTablet) {
                        frag = new AutoTemperatureFragment(AutoListView.this);
                        frag.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.auto_FragmentHolder, frag).commit();
                    }else {
                        Intent intnt = new Intent(AutoListView.this, AutoItemDetails.class);
                        intnt.putExtra("id", id);
                        intnt.putExtra("GPSEntry", gpsEntry);//pass the Database ID and message to next activity
                        intnt.putExtra("Function", "gps");
                        startActivityForResult(intnt, 5); //go to view fragment details
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.car1)
                                    .setContentTitle("Notification")
                                    .setContentText("Auto GPS Setting");

                    //where to go if clicked
                    Intent resultIntent = new Intent(ctx, AutoItemDetails.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity( ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    //now show the notification:
                    int mNotificationId = 0634;
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
            }
        });
    }

    public void updateTemp(long tempid, String temp) {
        ContentValues values = new ContentValues();
        values.put(AutoDatabaseHelper.KEY_TEMPERATURE, temp);
        db.update(AutoDatabaseHelper.DATABASE_NAME, values, AutoDatabaseHelper.KEY_ID + "=" + tempid, null);
        refreshDB();
        String toastText = "Auto Temperature is set";
        Toast toast = Toast.makeText(AutoListView.this, toastText, Toast.LENGTH_SHORT);
        toast.show();

    }

    public void insertGPSEntry(long gpsentryid, String gpsentry){
        ContentValues values = new ContentValues();
        values.put(AutoDatabaseHelper.KEY_GPS_ENTRY, gpsentry);
        db.insert(AutoDatabaseHelper.DATABASE_NAME, null, values);
        refreshDB();
        String toastText = "Auto GPS is set";
        Toast toast = Toast.makeText(AutoListView.this, toastText, Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Bundle bun = data.getExtras();


        Long tempid = bun.getLong("id");
        String temp = bun.getString("Temperature");


        if (requestCode==5 && resultCode == 0) {
            Long tempid = bun.getLong("id");
            String temp = bun.getString("Temperature");
            updateTemp(tempid, temp);

        }

        if (requestCode==5 && resultCode == 2) {
            Long gpsid = bun.getLong("id");
            String gpsentry = bun.getString("GPSEntry");
            insertGPSEntry(gpsid, gpsentry);

        }
    }

    private void refreshDB() {
        dbHelper = new AutoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        results = db.query(false, AutoDatabaseHelper.DATABASE_NAME,
                new String[] { AutoDatabaseHelper.KEY_ID, AutoDatabaseHelper.KEY_GPS_ENTRY, AutoDatabaseHelper.KEY_TEMPERATURE, AutoDatabaseHelper.KEY_NORMAL_SWITCH,
                        AutoDatabaseHelper.KEY_HEAD_SWITCH, AutoDatabaseHelper.KEY_INSIDE_BRIGHTNESS, AutoDatabaseHelper.KEY_RADIO_VOLUME, AutoDatabaseHelper.KEY_RADIO_MUTE,
                        AutoDatabaseHelper.KEY_RADIO_STATION_ONE, AutoDatabaseHelper.KEY_RADIO_STATION_TWO, AutoDatabaseHelper.KEY_RADIO_STATION_THREE, AutoDatabaseHelper.KEY_RADIO_STATION_FOUR,
                        AutoDatabaseHelper.KEY_RADIO_STATION_FIVE, AutoDatabaseHelper.KEY_RADIO_STATION_SIX}, null, null, null, null, null, null);
        int rows = results.getCount(); //number of rows returned
        results.moveToLast(); //move to first result
    }

    public void removeFragment() {
        getSupportFragmentManager().beginTransaction().remove(frag).commit();
    }

    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
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
                builder.setTitle("Welcome to Smart Home Auto Setting").setMessage("This is the Auto Setting part of the Smart Home App. You can set Temperature, Lights, Radio Stations and GPS of a car. Version 1.0 by Zhen Qu.")
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