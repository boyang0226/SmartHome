package com.example.bo.smarthome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by Bo on 2017-04-03.
 *
 * this is the fragment class for the home temp setting
 */

public class HouseTemp extends Fragment {

    View gui;
     long id;
    String time;
    String temp;
    HousesettingDetail housesettingDetail;

    TextView displaytime;
    TextView displaytemp;

    String timeupdate;
    String tempupdate;

     Dialog dialog;
    ListView listview;
HouseDatabaseHelper houseDatabaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    Context ctx;



    //constructor
    public HouseTemp(){}
    public HouseTemp(HousesettingDetail house){
        housesettingDetail =house;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        Bundle bun = getArguments();
        //get the time and temp from database
        id = bun.getLong("_id");
        time = bun.getString("Time");
        temp = bun.getString("Temp");


    }



    // http://stackoverflow.com/questions/29677812/why-is-onattach-called-before-oncreate
    public void onAttach(Activity activity){
        super.onAttach(activity);
        ctx=activity;
    }

    //The Cursor of that get the database information, return Cursor
    public  Cursor  getCursor(){
       Cursor cursor1 =db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[]{

                        HouseDatabaseHelper.KEY_Time,
                        HouseDatabaseHelper.KEY_ID,
                        HouseDatabaseHelper.Key_Temp,
                        },
                null, null, null, null, HouseDatabaseHelper.KEY_Time + " DESC", null);
        return cursor1;
    }






    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

//set the layout inflater
        gui = inflater.inflate(R.layout.activity_house_temp, null);

        //instantiate a new HouseDatabaseHelper and make the writable database
        houseDatabaseHelper =new HouseDatabaseHelper(ctx);
        db=houseDatabaseHelper.getWritableDatabase();

        listview= (ListView)gui.findViewById(R.id.house_temp_listview);
           cursor = getCursor();

        // simpleCursor that make the two column of the database to be one
        adapter = new SimpleCursorAdapter(ctx,
                R.layout.activity_house_displayschedule,
                cursor,
                new String[] { "Time","Temp" },
                new int[] { R.id.house_listviewholder1 ,R.id.house_listviewholder2});

           //adapter changeCursor
           adapter.changeCursor(getCursor());
            //set adapter
           listview.setAdapter(adapter);




       //  textview that for display the time and temp
        displaytime=(TextView)gui.findViewById(R.id.house_time_setting);
        displaytemp=(TextView)gui.findViewById(R.id.house_temp_setting);

       //listview onclick listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,  final long id) {

                //when click, show the dialog
                dialog=new Dialog(getActivity());
                dialog.setTitle(R.string.house_dialog_title);  //set title
                dialog.setContentView(R.layout.activity_house_add_dialog); //set content view
                TextView txtMessage=(TextView)dialog.findViewById(R.id.house_temp_hold);
                txtMessage.setText(R.string.house_dialog_text);  //set text
                txtMessage.setTextColor(Color.parseColor("#ff2222")); //set color
                final EditText editText=(EditText)dialog.findViewById(R.id.house_temp);
                Button save=(Button)dialog.findViewById(R.id.house_temp_save);
                Button delete =(Button)dialog.findViewById(R.id.house_temp_delete) ;

                //save button click listener
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempupdate=editText.getText().toString();
                        if (housesettingDetail==null){ //if phone

                            Intent intent = new Intent();
                            intent.putExtra("_id", id);
                            intent.putExtra("Temp",tempupdate);
                            getActivity().setResult(3, intent); //set the edit temp result code be 3
                            getActivity().finish();
                        } else
                        {     //if tablet edit temp
                            housesettingDetail.EditTemp(id,tempupdate);
                            housesettingDetail.removetempFragment();
                        }


                        adapter.notifyDataSetChanged();
                        dialog.dismiss();


                    }
                });

                dialog.show();
         //delete button click listener
                delete.setOnClickListener(new View.OnClickListener() {



                    @Override
                    public void onClick(View view) {

                      //if phone
                        if (housesettingDetail==null){
                            Intent intent = new Intent();
                            intent.putExtra("_id", id);
                            //set the selet result code be 2
                            getActivity().setResult(2, intent);
                            getActivity().finish();
                        } else
                        {
                            //if tablet
                            housesettingDetail.deleteschedule(id);
                            housesettingDetail.removetempFragment();
                            dialog.dismiss();
                        }

                    }
                });



            }
        });



        //add button
        Button add =(Button)gui.findViewById(R.id.house_temp_add);


//add button click listener
        add.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
              //get the updated time and temp
                timeupdate=displaytime.getText().toString();
                tempupdate =displaytemp.getText().toString();

                //if phone
                if (housesettingDetail==null){
                    Intent intent = new Intent();
                    intent.putExtra("Time",timeupdate);
                    intent.putExtra("Temp",tempupdate);
                    //set the update time and temp result code be 1
                    getActivity().setResult(1, intent);
                    getActivity().finish();
                } else  //if tablet
                {
                    housesettingDetail.updateTemp(id,timeupdate,tempupdate);
                    housesettingDetail.removetempFragment();
                }

            }
        });

        return gui;
    }
}
