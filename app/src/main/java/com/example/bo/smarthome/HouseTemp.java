package com.example.bo.smarthome;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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

    ListView listview;

    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    Context ctx;

    public HouseTemp(){}
    public HouseTemp(HousesettingDetail house){
        housesettingDetail =house;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        Bundle bun = getArguments();
        id = bun.getLong("_id");
//        time = bun.getString("Time");
//        temp = bun.getString("Temp");

           schedule=bun.getString("Schedule");
    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        ctx=activity;
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        gui = inflater.inflate(R.layout.activity_house_temp, null);



        listview= (ListView)gui.findViewById(R.id.house_temp_listview);
        cursor =db.query(false, HouseDatabaseHelper.DATABASE_NAME,
                new String[]{
                        HouseDatabaseHelper.KEY_Schedule},
                null, null, null, null, null, null);

        // Map Cursor columns to views defined in simple_list_item_2.xml

        adapter = new SimpleCursorAdapter(ctx,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] { "value" },
                new int[] { android.R.id.text1 });
        //Used to display a readable string for the phone type


           listview.setAdapter(adapter);
















        displaytime=(TextView)gui.findViewById(R.id.house_time_getting);
        displaytemp=(TextView)gui.findViewById(R.id.house_temp_getting);




        NumberPicker numberPicker =(NumberPicker)gui.findViewById(R.id.house_temp_number_picker);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                displaytemp.setText(newVal+"°C");
                timeupdate=displaytemp.getText().toString();
            }
        });



        TimePicker timePicker =(TimePicker)gui.findViewById(R.id.house_temp_timepicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {


                displaytime.setText(hourOfDay + ":" + minute);
                tempupdate=displaytemp.getText().toString();
            }});



        Button add =(Button)gui.findViewById(R.id.house_temp_add);
        add.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                schedule =timeupdate +"  "+ tempupdate;


                if (housesettingDetail==null){
                    Intent intent = new Intent();

                    intent.putExtra("Schedule", schedule);
                    intent.putExtra("_id", id);

                    getActivity().setResult(0, intent);
                    getActivity().finish();
                } else
                {

                    housesettingDetail.updateTemp(id,schedule);
                   housesettingDetail.removetempFragment();
                }

            }
        });

                return gui;
    }
}
