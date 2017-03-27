package com.example.bo.smarthome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LivingroomList extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    private ArrayList<String> devices = new ArrayList<>();
    Cursor results;
    public LivingroomDBHelper dbHelper;
    public SQLiteDatabase db;
    private DeviceAdapter deviceAdapter;
    FragmentTransaction fragTransaction;
    LivingroomFragment frag;

    private class DeviceAdapter extends ArrayAdapter<String>{
        public DeviceAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount(){
            return devices.size();
        }

        public long getItemId(int position)
        {
            results.moveToPosition(position);
            return results.getLong(results.getColumnIndex(LivingroomDBHelper.KEY_ID));
        }
        @Override
        public String getItem(int position){
            return devices.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = LivingroomList.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.list_item_template, null);

            TextView deviceName = (TextView)result.findViewById(R.id.item_text);
            deviceName.setText(getItem(position)); // get the string at position

            return result;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livingroom_list);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        Button addButton = (Button) findViewById(R.id.lr_list_add);

        final boolean isTablet = findViewById(R.id.fragment_holder) != null;

        deviceAdapter = new DeviceAdapter(this);
        listView.setAdapter(deviceAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView view, View arg1, int position, long id) {
                String deviceName = (String)(listView.getItemAtPosition(position));

                Bundle bun = new Bundle();
                bun.putLong("id", id);
                bun.putString("deviceName", deviceName);

                results.moveToPosition(position);

                String deviceType = results.getString(results.getColumnIndex(LivingroomDBHelper.KEY_Type));
                int deviceSwitch = results.getInt(results.getColumnIndex(LivingroomDBHelper.KEY_Switch));
                int brightness = results.getInt(results.getColumnIndex(LivingroomDBHelper.KEY_Brightness));
                String color = results.getString(results.getColumnIndex(LivingroomDBHelper.KEY_Color));
                int channel = results.getInt(results.getColumnIndex(LivingroomDBHelper.KEY_Channel));
                int volume = results.getInt(results.getColumnIndex(LivingroomDBHelper.Key_Volume));
                int height = results.getInt(results.getColumnIndex(LivingroomDBHelper.KEY_Height));

                bun.putString("deviceType", deviceType);
                bun.putBoolean("switch", deviceSwitch!=0);
                bun.putInt("brightness", brightness);
                bun.putString("color", color);
                bun.putInt("channel", channel);
                bun.putInt("volume", volume);
                bun.putInt("height", height);

                if (isTablet) {
                    bun.putBoolean("isTablet", true);
                    frag = new LivingroomFragment(LivingroomList.this);
                    frag.setArguments( bun );
                    fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.add(R.id.fragment_holder, frag).commit();
                }else{
                    Intent intent = new Intent(LivingroomList.this, LivingroomItemDetails.class);
                    intent.putExtra("isTablet", false);
                    intent.putExtras(bun);
                    startActivityForResult(intent, 5);
                }

                int frequency = results.getInt(results.getColumnIndex(LivingroomDBHelper.KEY_Frequency));
                updateDbDeviceFrequency(id, frequency + 1);
            }
        });

        refreshMessages();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bun = new Bundle();
                bun.putBoolean("addNewDevice", true);
                if (isTablet) {
                    bun.putBoolean("isTablet", true);
                    frag = new LivingroomFragment(LivingroomList.this);
                    frag.setArguments( bun );
                    fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.add(R.id.fragment_holder, frag).commit();
                }else{
                    Intent intent = new Intent(LivingroomList.this, LivingroomItemDetails.class);
                    intent.putExtra("isTablet", false);
                    intent.putExtras(bun);
                    startActivityForResult(intent, 6);
                }
            }
        });

        //toolbar
        Toolbar tb =(Toolbar)findViewById(R.id.lr_toolbar);
        setSupportActionBar(tb);
    }

    private void refreshMessages(){
        dbHelper = new LivingroomDBHelper(this);
        db = dbHelper.getWritableDatabase();

        results = db.query(false, LivingroomDBHelper.TABLENAME,
                new String[] {LivingroomDBHelper.KEY_ID, LivingroomDBHelper.KEY_Name,
                            LivingroomDBHelper.KEY_Switch, LivingroomDBHelper.KEY_Brightness,
                            LivingroomDBHelper.KEY_Color, LivingroomDBHelper.KEY_Channel,
                            LivingroomDBHelper.Key_Volume, LivingroomDBHelper.KEY_Height,
                            LivingroomDBHelper.KEY_Type, LivingroomDBHelper.KEY_Frequency
                },
                null, null, null, null, LivingroomDBHelper.KEY_Frequency+ " DESC", null);
        int rows = results.getCount() ; //number of rows returned
        results.moveToFirst(); //move to first result

        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + results.getColumnCount());
        for(int i = 0; i < results.getColumnCount();i++)
            Log.i(ACTIVITY_NAME, "Cursor’s column name = " + results.getColumnName(i));

        devices.clear();
        while(!results.isAfterLast() ) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString(results.getColumnIndex(LivingroomDBHelper.KEY_Name)));
            devices.add(results.getString(results.getColumnIndex(LivingroomDBHelper.KEY_Name)));
            results.moveToNext();
        }
        deviceAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

    public void insertDbDevice(String deviceName, String deviceTye){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Name, deviceName);
        values.put(LivingroomDBHelper.KEY_Type, deviceTye);
        db.insert(LivingroomDBHelper.TABLENAME, null, values);
        refreshMessages();
    }

    public void updateDbSimpleLamp(long deviceID, boolean deviceSwitch){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Switch, deviceSwitch ? 1 : 0);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }
    public void updateDbDimmableLamp(long deviceID, int brightness){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Brightness, brightness);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }
    public void updateDbSmartLamp(long deviceID, int brightness, String color){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Brightness, brightness);
        values.put(LivingroomDBHelper.KEY_Color, color);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }
    public void updateDbTV(long deviceID, boolean deviceSwitch, int channel, int volume){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Switch, deviceSwitch);
        values.put(LivingroomDBHelper.KEY_Channel, channel);
        values.put(LivingroomDBHelper.Key_Volume, volume);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }

    public void updateDbBlinds(long deviceID, int height){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Height, height);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }


    public void updateDbDeviceFrequency(long deviceID, int frequency){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Frequency, frequency);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);

    }

    public void deleteDbDevice(Long deviceID){
        db.delete(LivingroomDBHelper.TABLENAME, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
        refreshMessages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Bundle bun = data.getExtras();
        Long deviceID = bun.getLong("id");
        if (requestCode==5 && resultCode == 0) {
            deleteDbDevice(deviceID);
            Toast.makeText(this, R.string.delete_message, Toast.LENGTH_LONG).show();
        }
        if (requestCode==5 && resultCode == 1) {
            String deviceType = bun.getString("deviceType");
            if (deviceType.equals("Simple Lamp")){
                boolean deviceSwith = bun.getBoolean("switch");
                updateDbSimpleLamp(deviceID, deviceSwith);
            }else if (deviceType.equals("Dimmable Lamp")){
                int brightness = bun.getInt("brightness");
                updateDbDimmableLamp(deviceID, brightness);
            }else if (deviceType.equals("Smart Lamp")){
                int brightness = bun.getInt("brightness");
                String color = bun.getString("color");
                updateDbSmartLamp(deviceID, brightness, color);
            }else if (deviceType.equals("Television")){
                boolean deviceSwith = bun.getBoolean("switch");
                int channel = bun.getInt("channel");
                int volume = bun.getInt("volume");
                updateDbTV(deviceID, deviceSwith, channel, volume);
            }else if (deviceType.equals("Window Blinds")){
                int height = bun.getInt("height");
                updateDbBlinds(deviceID, height);
            }
            Snackbar.make(findViewById(android.R.id.content), R.string.update_message, Snackbar.LENGTH_LONG)
                    .show();
        }
        if (requestCode == 6 && resultCode == 0) {
            String deviceName = bun.getString("deviceName");
            String deviceType = bun.getString("deviceType");
            insertDbDevice(deviceName, deviceType);
            Toast.makeText(this, R.string.save_message, Toast.LENGTH_LONG).show();
        }
        refreshMessages();
    }

    public void removeFragment()
    {
        FragmentManager fm = getFragmentManager();
        fragTransaction = fm.beginTransaction();
        fragTransaction.remove(frag);
        fragTransaction.commit();


    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.livingroom_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId() ){
            case R.id.living:

                break;
            case R.id.kitchen:

                break;
            case R.id.house:

                break;
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Welcome to Living Room");
                builder.setMessage("Version 1.0 by Sizhe Chen\n blablablablablablablabla");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }
}

