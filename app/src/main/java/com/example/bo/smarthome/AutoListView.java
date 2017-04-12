package com.example.bo.smarthome;
// author  Zhen Qu
// student number 040587623

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
    //main class of auto part
    Context ctx;
    ArrayList<String> autoArray = new ArrayList<String>();
    ArrayAdapter<String> listAdapter;
    protected static final String ACTIVITY_NAME = "AutoListView";
    protected AutoDatabaseHelper dbHelper;
    public SQLiteDatabase db;
    protected Boolean isTablet;
    Cursor results;
    AutoTemperatureFragment fragTemp;
    AutoLightsFragment fragLights;
    AutoGPSFragment fragGPS;
    AutoRadioFragment fragRadio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.auto_Toolbar);
        setSupportActionBar(toolbar);
        ctx = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.auto_fab);
        Snackbar.make(fab, getString(R.string.auto_welcome), Snackbar.LENGTH_LONG)   //greeting of snachbar
                .setAction("Action", null).show();

        ListView autoListView =(ListView)findViewById(R.id.auto_ListView);

        String [] item = {getString(R.string.auto_temp), getString(R.string.auto_lights), getString(R.string.auto_gps), getString(R.string.auto_radio)};
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

        autoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //main menu of auto setting, listener of listview
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("autoListView", "onItemClick: " + i + " " + l);
                Bundle bun = new Bundle();    //create data bundle passed to different fragments
                long id = results.getLong(results.getColumnIndex(AutoDatabaseHelper.KEY_ID));
                String gpsEntry = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_GPS_ENTRY));
                String temp = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_TEMPERATURE));
                int  normalSwitch = results.getInt(results.getColumnIndex(AutoDatabaseHelper.KEY_NORMAL_SWITCH));
                int headSwitch = results.getInt(results.getColumnIndex(AutoDatabaseHelper.KEY_HEAD_SWITCH));
                int insideBrightness = results.getInt(results.getColumnIndex(AutoDatabaseHelper.KEY_INSIDE_BRIGHTNESS));
                int volumn = results.getInt(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_VOLUME));
                int mute = results.getInt(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_MUTE));
                String radiostation1 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_ONE));
                String radiostation2 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_TWO));
                String radiostation3 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_THREE));
                String radiostation4 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_FOUR));
                String radiostation5 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_FIVE));
                String radiostation6 = results.getString(results.getColumnIndex(AutoDatabaseHelper.KEY_RADIO_STATION_SIX));

                bun.putLong("id",id);
                bun.putString("GPSEntry",gpsEntry);
                bun.putString("Temperature",temp);
                bun.putBoolean("NormalSwitch",normalSwitch!=0);
                bun.putBoolean("HeadSwitch",headSwitch!=0);
                bun.putInt("InsideBrightness",insideBrightness);
                bun.putInt("Volumn",volumn);
                bun.putBoolean("Mute",mute!=0);
                bun.putString("Station1",radiostation1);
                bun.putString("Station2",radiostation2);
                bun.putString("Station3",radiostation3);
                bun.putString("Station4",radiostation4);
                bun.putString("Station5",radiostation5);
                bun.putString("Station6",radiostation6);

                if (adapterView.getItemAtPosition(i).equals(getString(R.string.auto_temp))) {  //auto temperature setting
                    if (isTablet) {   //if it's tablet, start the fragment with the bundle data
                        fragTemp = new AutoTemperatureFragment(AutoListView.this);
                        fragTemp.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.auto_FragmentHolder, fragTemp).commit();
                    }else {    // if it's phone, start AutoItemDetails activity
                        Intent intnt = new Intent(AutoListView.this, AutoItemDetails.class);
                        intnt.putExtras(bun);//pass the Database ID and message to next activity
                        intnt.putExtra("Function", "temperature");
                        startActivityForResult(intnt, 5); //go to view fragment details
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.car1)
                                    .setContentTitle(getString(R.string.auto_notice))
                                    .setContentText(getString(R.string.auto_temp_setting));

                    //where to go if clicked
                    Intent resultIntent = new Intent(ctx, AutoItemDetails.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity( ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    //now show the notification:
                    int mNotificationId = 0634;
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }

                else if (adapterView.getItemAtPosition(i).equals(getString(R.string.auto_lights))) { //auto lights setting
                    if (isTablet) {
                        fragLights = new AutoLightsFragment(AutoListView.this);
                        fragLights.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.auto_FragmentHolder, fragLights).commit();
                    }else {
                        Intent intnt = new Intent(AutoListView.this, AutoItemDetails.class);
                        intnt.putExtras(bun);//pass the Database ID and message to next activity
                        intnt.putExtra("Function", "lights");
                        startActivityForResult(intnt, 5); //go to view fragment details
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.car1)
                                    .setContentTitle(getString(R.string.auto_notice))
                                    .setContentText(getString(R.string.auto_light_setting));

                    //where to go if clicked
                    Intent resultIntent = new Intent(ctx, AutoItemDetails.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity( ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    //now show the notification:
                    int mNotificationId = 0635;
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }

                else if (adapterView.getItemAtPosition(i).equals(getString(R.string.auto_gps))) {  //auto gps setting
                    if (isTablet) {
                        fragGPS = new AutoGPSFragment(AutoListView.this);
                        fragGPS.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.auto_FragmentHolder, fragGPS).commit();
                    }else {
                        Intent intnt = new Intent(AutoListView.this, AutoItemDetails.class);
                        intnt.putExtras(bun);//pass the Database ID and message to next activity
                        intnt.putExtra("Function", "gps");
                        startActivityForResult(intnt, 5); //go to view fragment details
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.car1)
                                    .setContentTitle(getString(R.string.auto_notice))
                                    .setContentText(getString(R.string.auto_gps_setting));

                    //where to go if clicked
                    Intent resultIntent = new Intent(ctx, AutoItemDetails.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity( ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    //now show the notification:
                    int mNotificationId = 0636;
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }

                else if (adapterView.getItemAtPosition(i).equals(getString(R.string.auto_radio))) {  //auto radio setting
                    if (isTablet) {
                        fragRadio = new AutoRadioFragment(AutoListView.this);
                        fragRadio.setArguments(bun);
                        getSupportFragmentManager().beginTransaction().replace(R.id.auto_FragmentHolder, fragRadio).commit();
                    }else {
                        Intent intnt = new Intent(AutoListView.this, AutoItemDetails.class);
                        intnt.putExtras(bun);//pass the Database ID and message to next activity
                        intnt.putExtra("Function", "radio");
                        startActivityForResult(intnt, 5); //go to view fragment details
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.car1)
                                    .setContentTitle(getString(R.string.auto_notice))
                                    .setContentText(getString(R.string.auto_radio_setting));

                    //where to go if clicked
                    Intent resultIntent = new Intent(ctx, AutoItemDetails.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity( ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    //now show the notification:
                    int mNotificationId = 0637;
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
            }
        });
    }

    public void updateTemp(long tempid, String temp) {    //when temperature changed, update database and set toast
        ContentValues values = new ContentValues();
        values.put(AutoDatabaseHelper.KEY_TEMPERATURE, temp);
        db.update(AutoDatabaseHelper.DATABASE_NAME, values, AutoDatabaseHelper.KEY_ID + "=" + tempid, null);
        refreshDB();
        String toastText = getString(R.string.auto_temp_toast);
        Toast toast = Toast.makeText(AutoListView.this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void updateLights(long lightsid, Boolean nswitch, Boolean hswitch, int ibrightness){    //when lights setting changed, update database and set toast
        ContentValues values = new ContentValues();
        values.put(AutoDatabaseHelper.KEY_NORMAL_SWITCH, nswitch ? 1 : 0);
        values.put(AutoDatabaseHelper.KEY_HEAD_SWITCH, hswitch ? 1 : 0);
        values.put(AutoDatabaseHelper.KEY_INSIDE_BRIGHTNESS, ibrightness);
        db.update(AutoDatabaseHelper.DATABASE_NAME, values, AutoDatabaseHelper.KEY_ID + "=" + lightsid, null);
        refreshDB();
        String toastText = getString(R.string.auto_light_toast);
        Toast toast = Toast.makeText(AutoListView.this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void updateRadio(long radioid, Boolean mute, int volumn, String radio1, String radio2, String radio3, String radio4, String radio5, String radio6){
        // update auto radio setting and set toast
        ContentValues values = new ContentValues();
        values.put(AutoDatabaseHelper.KEY_RADIO_MUTE, mute ? 1 : 0);
        values.put(AutoDatabaseHelper.KEY_RADIO_VOLUME, volumn);
        values.put(AutoDatabaseHelper.KEY_RADIO_STATION_ONE, radio1);
        values.put(AutoDatabaseHelper.KEY_RADIO_STATION_TWO, radio2);
        values.put(AutoDatabaseHelper.KEY_RADIO_STATION_THREE, radio3);
        values.put(AutoDatabaseHelper.KEY_RADIO_STATION_FOUR, radio4);
        values.put(AutoDatabaseHelper.KEY_RADIO_STATION_FIVE, radio5);
        values.put(AutoDatabaseHelper.KEY_RADIO_STATION_SIX, radio6);
        db.update(AutoDatabaseHelper.DATABASE_NAME, values, AutoDatabaseHelper.KEY_ID + "=" + radioid, null);
        refreshDB();
        String toastText = getString(R.string.auto_radio_toast);
        Toast toast = Toast.makeText(AutoListView.this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //get the return bundle data from different fragment of phone and update database
        final Bundle bun = data.getExtras();

        if (requestCode==5 && resultCode == 0) {  //if the return data from auto temp setting, update database temperature column
            Long tempid = bun.getLong("id");
            String temp = bun.getString("Temperature");
            updateTemp(tempid, temp);
        }

        if (requestCode==5 && resultCode == 1) {   ////if the return data from auto lights setting, update database lights columns
            Long lightsID = bun.getLong("id");
            boolean nswitch = bun.getBoolean("NormalSwitch");
            boolean hswitch = bun.getBoolean("HeadSwitch");
            int ibrightness = bun.getInt("InsideBrightness");
            updateLights(lightsID, nswitch, hswitch, ibrightness);
        }

        if (requestCode==5 && resultCode == 3) {    ////if the return data from auto radio setting, update database radios columns
            Long radioid = bun.getLong("id");
            boolean mute = bun.getBoolean("Mute");
            int volumn = bun.getInt("Volumn");
            String radio1 = bun.getString("Station1");
            String radio2 = bun.getString("Station2");
            String radio3 = bun.getString("Station3");
            String radio4 = bun.getString("Station4");
            String radio5 = bun.getString("Station5");
            String radio6 = bun.getString("Station6");
            updateRadio(radioid,mute,volumn,radio1,radio2,radio3,radio4,radio5,radio6);
        }
    }

    private void refreshDB() {    //refresh database when the database is updated
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

    public void removeTempFragment() {   //remove auto temp fragment
        getSupportFragmentManager().beginTransaction().remove(fragTemp).commit();
    }

    public void removeLightsFragment() {   //remove auto lights fragment
        getSupportFragmentManager().beginTransaction().remove(fragLights).commit();
    }

    public void removeRadioFragment() {   //remove auto radio fragment
        getSupportFragmentManager().beginTransaction().remove(fragRadio).commit();
    }

    protected void onDestroy() {  //close database
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
                builder.setTitle(getString(R.string.auto_help_title)).setMessage(getString(R.string.auto_help_message))
                        .setNegativeButton(getString(R.string.auto_ok), new DialogInterface.OnClickListener() {
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