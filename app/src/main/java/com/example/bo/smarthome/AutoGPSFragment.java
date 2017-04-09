package com.example.bo.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

// http://stackoverflow.com/questions/12077955/android-using-simplecursoradapter-to-get-data-from-database-to-listview

public class AutoGPSFragment extends Fragment {   //auto temp fragment
    String gpsEntry;
    Long gpsID;
    View gui;
    ProgressBar progressBar;
    static Context ctx;
    AutoDatabaseHelper dbHelper;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    ListView listView;
    SQLiteDatabase db;

    AutoListView autoLV = null;

    public AutoGPSFragment(){
    }

    public AutoGPSFragment (AutoListView a){
        autoLV = a;
    }

    @Override
    public void onCreate(Bundle b)   //get the passed bundle data
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        gpsEntry = bun.getString("GPSEntry");
        gpsID = bun.getLong("id");
    }

    @Override
    public void onAttach(Activity activity) {   //create context object in fragment
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {   //create view and set the passed bundle data

        gui = inflater.inflate(R.layout.activity_auto_gps, null);
        final EditText message = (EditText)gui.findViewById(R.id.auto_gps_editText);
        message.setText(gpsEntry);
        progressBar = (ProgressBar)gui.findViewById(R.id.auto_gps_progressBar);

        dbHelper = new AutoDatabaseHelper(ctx);       //simplecursoradapter used to display gps entry from database
        db = dbHelper.getWritableDatabase();
        cursor = getCursor();
        adapter = new SimpleCursorAdapter(ctx,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] { "GPSEntry" },
                new int[] { android.R.id.text1 });
        listView = (ListView)gui.findViewById(R.id.auto_gps_entry_listview);
        listView.setAdapter(adapter);

        Button btn = (Button)gui.findViewById(R.id.auto_gps_setbutton);

        btn.setOnClickListener(new View.OnClickListener() {  //when click set, insert new entry to database and refresh adapter and listview
            @Override
            public void onClick(View view) {
                Log.i("AutoGPSFragment", "User clicked set auto GPS button");
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(getString(R.string.auto_gps_dialog))
                        .setNegativeButton(getString(R.string.auto_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton(getString(R.string.auto_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
                                gpsEntry = message.getText().toString();
                                insertGPSEntry(gpsID,gpsEntry);
                                adapter.changeCursor(getCursor());
                                listView.setAdapter(adapter);
                            }
                        });
                builder.create().show();
            }
        });

        Button btnNav = (Button)gui.findViewById(R.id.auto_gps_navgationbutton);

        btnNav.setOnClickListener(new View.OnClickListener() {  //when click navigation, start a asynctask to invoke google navigation intent
            @Override
            public void onClick(View view) {
                GPSQuery thread = new GPSQuery();
                thread.execute();
            }
        });

        return gui;
    }

    public class GPSQuery extends AsyncTask<String, Integer, String> {   //asynctask used to invoke google navigation
        @Override
        protected String doInBackground(String... args) {
            String in="";
            publishProgress(50);
            try{
                Thread.sleep(1500);
            }catch(InterruptedException e){}
            publishProgress(100);
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+gpsEntry);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            return in;
        }

        public void onProgressUpdate(Integer ... values){
            progressBar = (ProgressBar)gui.findViewById(R.id.auto_gps_progressBar);
            progressBar.setProgress(values[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String result){
            progressBar = (ProgressBar)gui.findViewById(R.id.auto_gps_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public Cursor getCursor(){   //query database gps entry column to put into cursor
        Cursor cursor1 = db.query(false, AutoDatabaseHelper.DATABASE_NAME,
                new String[] { AutoDatabaseHelper.KEY_ID, AutoDatabaseHelper.KEY_GPS_ENTRY }, null, null, null, null, null, null);
        return cursor1;
    }

    public void insertGPSEntry(long gpsentryid, String gpsentry){   //insert new gps entry into database and set toast
        ContentValues values = new ContentValues();
        values.put(AutoDatabaseHelper.KEY_GPS_ENTRY, gpsentry);
        db.insert(AutoDatabaseHelper.DATABASE_NAME, null, values);
        String toastText = getString(R.string.auto_gps_toast);
        Toast toast = Toast.makeText(AutoGPSFragment.ctx, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

}
