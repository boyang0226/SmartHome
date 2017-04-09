package com.example.bo.smarthome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    /*
        Adapter to adapt living room device list
     */
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

        // hide add button to show the progress bar
        addButton.setVisibility(View.INVISIBLE);

        final boolean isTablet = findViewById(R.id.fragment_holder) != null;

        deviceAdapter = new DeviceAdapter(this);
        listView.setAdapter(deviceAdapter);

        Toolbar tb =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ProgressBar progressBar= (ProgressBar) findViewById(R.id.lr_progressBar);
        // show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // add listener when list item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView view, View arg1, int position, long id) {
                String deviceName = (String)(listView.getItemAtPosition(position));

                // generate the data for the bundle
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

                if (fragTransaction!=null && !fragTransaction.isEmpty())
                    removeFragment();

                // if tablet, we send data to fragment, otherwise, we go to next activity
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
                // update the frequency in the database
                int frequency = results.getInt(results.getColumnIndex(LivingroomDBHelper.KEY_Frequency));
                updateDbDeviceFrequency(id, frequency + 1);
            }
        });

        refreshMessages();

        // add listener when add button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bun = new Bundle();
                bun.putBoolean("addNewDevice", true);

                if (fragTransaction!=null && !fragTransaction.isEmpty())
                    removeFragment();

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

        InitiaizeDB thread = new InitiaizeDB();
        thread.execute();
    }

    /*
        Retrieve the latest data from DB and update the list
     */
    public void refreshMessages(){
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

    /*
        Insert a device into DB
     */
    public void insertDbDevice(String deviceName, String deviceTye){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Name, deviceName);
        values.put(LivingroomDBHelper.KEY_Type, deviceTye);
        db.insert(LivingroomDBHelper.TABLENAME, null, values);
        refreshMessages();
    }

    /*
        Update the simple lamp in DB
     */
    public void updateDbSimpleLamp(long deviceID, boolean deviceSwitch){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Switch, deviceSwitch ? 1 : 0);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }

    /*
        Update the dimmable lamp in DB
     */
    public void updateDbDimmableLamp(long deviceID, int brightness){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Brightness, brightness);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }

    /*
        Update the smart lamp in DB
     */
    public void updateDbSmartLamp(long deviceID, int brightness, String color){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Brightness, brightness);
        values.put(LivingroomDBHelper.KEY_Color, color);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }

    /*
        Update tv in DB
     */
    public void updateDbTV(long deviceID, boolean deviceSwitch, int channel, int volume){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Switch, deviceSwitch);
        values.put(LivingroomDBHelper.KEY_Channel, channel);
        values.put(LivingroomDBHelper.Key_Volume, volume);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }

    /*
        Update blinds in DB
     */
    public void updateDbBlinds(long deviceID, int height){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Height, height);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
    }

    /*
        Update the frequency of device in DB
     */
    public void updateDbDeviceFrequency(long deviceID, int frequency){
        ContentValues values = new ContentValues();
        values.put(LivingroomDBHelper.KEY_Frequency, frequency);
        db.update(LivingroomDBHelper.TABLENAME, values, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);

    }

    /*
        Delete a device in DB
     */
    public void deleteDbDevice(Long deviceID){
        db.delete(LivingroomDBHelper.TABLENAME, LivingroomDBHelper.KEY_ID + "=" + deviceID, null);
        refreshMessages();
    }

    /*
        Show the update succeed message
     */
    public void showUpdateMessage(){
        Snackbar.make(findViewById(android.R.id.content), R.string.update_message, Snackbar.LENGTH_LONG)
                .show();
    }

    /*
        Show the save succeed message
     */
    public void showSaveMessage(){
        Toast.makeText(this, R.string.save_message, Toast.LENGTH_LONG).show();
    }

    /*
        Show the delete succeed message
     */
    public void showDeleteMessage(){
        Toast.makeText(this, R.string.delete_message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0)
            return;
        final Bundle bun = data.getExtras();
        Long deviceID = bun.getLong("id");

        // handle delete
        if (requestCode==5 && resultCode == 3) {
            deleteDbDevice(deviceID);
            showDeleteMessage();
        }

        // handle update
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
            showUpdateMessage();
        }

        // handle new device
        if (requestCode == 6 && resultCode == 2) {
            String deviceName = bun.getString("deviceName");
            String deviceType = bun.getString("deviceType");
            insertDbDevice(deviceName, deviceType);
            showSaveMessage();
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

    //livingroom_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.livingroom_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id)
        {

            case R.id.kitchen:

                Intent itntKitchenMain = new Intent(this, KitchenMain.class);
                startActivityForResult(itntKitchenMain, 5);

                break;
            case R.id.house:


                Intent HouseSetting = new Intent(this, HousesettingDetail.class);
                startActivityForResult(HouseSetting, 5);

                break;
            case R.id.car:

               startActivity(new Intent(this, AutoListView.class));
                break;
            case R.id.lr_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.lr_help_title);
                builder.setMessage(R.string.lr_help_message);
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

    //initialize DB and show Progress bar
    private class InitiaizeDB extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String ... args)
        {
            Cursor results = db.query(false, LivingroomDBHelper.TABLENAME,
                    new String[] {},
                    null, null, null, null, null, null);
            int rows = results.getCount() ; //number of rows returned
            if (rows == 0) {
                try {
                    publishProgress(0);
                    ContentValues values = new ContentValues();
                    values.put(LivingroomDBHelper.KEY_Name, "Simple Lamp 1");
                    values.put(LivingroomDBHelper.KEY_Type, "Simple Lamp");
                    db.insert(LivingroomDBHelper.TABLENAME, null, values);
                    Thread.sleep(500);
                    publishProgress(20);

                    values = new ContentValues();
                    values.put(LivingroomDBHelper.KEY_Name, "Dimmable Lamp 1");
                    values.put(LivingroomDBHelper.KEY_Type, "Dimmable Lamp");
                    db.insert(LivingroomDBHelper.TABLENAME, null, values);
                    Thread.sleep(500);
                    publishProgress(40);

                    values = new ContentValues();
                    values.put(LivingroomDBHelper.KEY_Name, "Smart Lamp 1");
                    values.put(LivingroomDBHelper.KEY_Type, "Smart Lamp");
                    db.insert(LivingroomDBHelper.TABLENAME, null, values);
                    Thread.sleep(500);
                    publishProgress(60);

                    values = new ContentValues();
                    values.put(LivingroomDBHelper.KEY_Name, "TV 1");
                    values.put(LivingroomDBHelper.KEY_Type, "Television");
                    db.insert(LivingroomDBHelper.TABLENAME, null, values);
                    Thread.sleep(500);
                    publishProgress(80);

                    values = new ContentValues();
                    values.put(LivingroomDBHelper.KEY_Name, "Blinds 1");
                    values.put(LivingroomDBHelper.KEY_Type, "Window Blinds");
                    db.insert(LivingroomDBHelper.TABLENAME, null, values);
                    Thread.sleep(500);
                    publishProgress(100);
                }
                catch( Exception me)
                {
                    Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
                }


            }
            return " ";

        }

        public void onProgressUpdate(Integer... progress)
        {
            ProgressBar progressBar= (ProgressBar) findViewById(R.id.lr_progressBar);
            progressBar.setProgress(progress[0]);
            Log.i("ASYNCTASK", "" + progress[0]);
        }

        public void onPostExecute(String work)
        {
            ProgressBar progressBar= (ProgressBar) findViewById(R.id.lr_progressBar);
            progressBar.setVisibility(View.INVISIBLE);

            Button addButton = (Button) findViewById(R.id.lr_list_add);
            addButton.setVisibility(View.VISIBLE);

            refreshMessages();
            Log.i("ASYNC TASK DONE", work);
        }

        private Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

    }
}

