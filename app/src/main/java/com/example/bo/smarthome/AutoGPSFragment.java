package com.example.bo.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;


public class AutoGPSFragment extends Fragment {
    String gpsEntry;
    Long gpsID;
    View gui;
    Intent mapIntent;
    ProgressBar progressBar;
    Context ctx;
    AutoDatabaseHelper dbHelper;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    ListView listView;

    AutoListView autoLV = null;

    public AutoGPSFragment(){
    }

    public AutoGPSFragment (AutoListView a){
        autoLV = a;
    }
    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        gpsEntry = bun.getString("GPSEntry");
        gpsID = bun.getLong("id");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ctx=activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        gui = inflater.inflate(R.layout.activity_auto_gps, null);
        final EditText message = (EditText)gui.findViewById(R.id.auto_gps_editText);
        message.setText(gpsEntry);
        progressBar = (ProgressBar)gui.findViewById(R.id.auto_gps_progressBar);

        GPSQuery thread = new GPSQuery();
        thread.execute();

        SimpleCursorAdapter adpter = new SimpleCursorAdapter(ctx,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] { "GPSEntry" },
                new int[] { android.R.id.text1 });
        listView = (ListView)gui.findViewById(R.id.auto_gps_entry_listview);
        listView.setAdapter(adpter);

        Button btn = (Button)gui.findViewById(R.id.auto_gps_setbutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AutoGPSFragment", "User clicked set auto GPS button");
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Do you want to set a new destination?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
                                if (message !=null) {
                                    gpsEntry = message.getText().toString();
                                    if (autoLV == null) {               // called from phone
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("GPSEntry", gpsEntry);
                                        resultIntent.putExtra("id", gpsID);
                                        getActivity().setResult(2, resultIntent);
                                        getActivity().finish();
                                    } else            // callled from tablet
                                    {
                                        autoLV.insertGPSEntry(gpsID, gpsEntry);
                                        autoLV.removeFragment();
                                    }
                                }

                                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+gpsEntry);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        });
                builder.create().show();
            }
        });
        return gui;
    }

    public class GPSQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args) {
            String in="";
            dbHelper = new AutoDatabaseHelper(ctx);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            cursor = db.query(false, AutoDatabaseHelper.DATABASE_NAME,
                    new String[] { AutoDatabaseHelper.KEY_ID, AutoDatabaseHelper.KEY_GPS_ENTRY }, null, null, null, null, null, null);
            publishProgress(50);
            try{
                Thread.sleep(1500);
            }catch(InterruptedException e){}
            publishProgress(100);

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

}
