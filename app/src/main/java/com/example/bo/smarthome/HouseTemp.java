package com.example.bo.smarthome;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    String schedule;
     Dialog dialog;
    ListView listview;
HouseDatabaseHelper houseDatabaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    Context ctx;


    EditText tempinput;

    public HouseTemp(){}
    public HouseTemp(HousesettingDetail house){
        housesettingDetail =house;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        Bundle bun = getArguments();
        id = bun.getLong("_id");
        time = bun.getString("Time");
        temp = bun.getString("Temp");

         //  schedule=bun.getString("Schedule");
    }




    public void onAttach(Activity activity){
        super.onAttach(activity);
        ctx=activity;
    }

    public  Cursor  getCursor(){
       Cursor cursor1 =db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[]{
                        HouseDatabaseHelper.KEY_ID,
                    //    HouseDatabaseHelper.KEY_Schedule
                        HouseDatabaseHelper.KEY_Time,
                        HouseDatabaseHelper.Key_Temp,
                        },
                null, null, null, null, HouseDatabaseHelper.KEY_Time + " DESC", null);
        return cursor1;
    }






    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        gui = inflater.inflate(R.layout.activity_house_temp, null);

        houseDatabaseHelper =new HouseDatabaseHelper(ctx);
        db=houseDatabaseHelper.getWritableDatabase();

        listview= (ListView)gui.findViewById(R.id.house_temp_listview);
           cursor = getCursor();

        adapter = new SimpleCursorAdapter(ctx,
                R.layout.activity_house_displayschedule,
                cursor,
                new String[] { "Time","Temp" },
                new int[] { R.id.house_listviewholder1 ,R.id.house_listviewholder2});


           adapter.changeCursor(getCursor());

           listview.setAdapter(adapter);





        displaytime=(TextView)gui.findViewById(R.id.house_time_setting);
        displaytemp=(TextView)gui.findViewById(R.id.house_temp_setting);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                dialog=new Dialog(getActivity());
                dialog.setTitle(R.string.house_dialog_title);

                dialog.setContentView(R.layout.activity_house_add_dialog);
                TextView txtMessage=(TextView)dialog.findViewById(R.id.house_temp_hold);
                txtMessage.setText(R.string.house_dialog_text);
                txtMessage.setTextColor(Color.parseColor("#ff2222"));
                final EditText editText=(EditText)dialog.findViewById(R.id.house_temp);
                Button bt=(Button)dialog.findViewById(R.id.house_temp_save);
                Button delete =(Button)dialog.findViewById(R.id.house_temp_delete) ;
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempupdate=editText.getText().toString();
                        if (housesettingDetail==null){
                            Intent intent = new Intent();
                            intent.putExtra("_id", id);
                            intent.putExtra("Temp",tempupdate);
                            getActivity().setResult(0, intent);
                            getActivity().finish();
                        } else
                        {

                            housesettingDetail.EditTemp(id,tempupdate);
                            housesettingDetail.removetempFragment();
                        }


                        adapter.notifyDataSetChanged();
                        dialog.dismiss();


                    }
                });

                dialog.show();

                delete.setOnClickListener(new View.OnClickListener() {



                    @Override
                    public void onClick(View view) {


                        if (housesettingDetail==null){
                            Intent intent = new Intent();

                            intent.putExtra("Schedule", schedule);
                            intent.putExtra("_id", id);

                            getActivity().setResult(0, intent);
                            getActivity().finish();
                        } else
                        {

                            housesettingDetail.deleteschedule(id);
                            housesettingDetail.removetempFragment();
                            dialog.dismiss();
                        }

                    }
                });



            }
        });





















        Button add =(Button)gui.findViewById(R.id.house_temp_add);



        add.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                timeupdate=displaytime.getText().toString();
                tempupdate =displaytemp.getText().toString();

                if (housesettingDetail==null){
                    Intent intent = new Intent();
             //       intent.putExtra("Schedule", schedule);
                    intent.putExtra("_id", id);
                    intent.putExtra("Time",timeupdate);
                    intent.putExtra("Temp",tempupdate);
                    getActivity().setResult(0, intent);
                    getActivity().finish();
                } else
                {

               //     housesettingDetail.updateTemp(id,schedule);
                    housesettingDetail.updateTemp(id,timeupdate,tempupdate);
                    housesettingDetail.removetempFragment();
                }

            }
        });



        return gui;
    }
}
