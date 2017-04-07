package com.example.bo.smarthome;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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


    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        gui = inflater.inflate(R.layout.activity_house_temp, null);

        displaytime=(TextView)gui.findViewById(R.id.house_time_getting);
        displaytemp=(TextView)gui.findViewById(R.id.house_temp_getting);
        TextView tabletime=(TextView)gui.findViewById(R.id.house_table_time_setting) ;
        TextView tabletemp=(TextView)gui.findViewById(R.id.house_table_temp_setting) ;

        tabletime.setText(time);
        tabletemp.setText(temp);

        NumberPicker numberPicker =(NumberPicker)gui.findViewById(R.id.house_temp_number_picker);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                displaytemp.setText(newVal);
                timeupdate=displaytemp.getText().toString();
            }
        });


        TimePicker timePicker =(TimePicker)gui.findViewById(R.id.house_temp_timepicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {


                displaytemp.setText(hourOfDay + ":" + minute);
                tempupdate=displaytemp.getText().toString();
            }});


        Button add =(Button)gui.findViewById(R.id.house_temp_add);
        add.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                if (housesettingDetail==null){
                    Intent intent = new Intent();
                    intent.putExtra("Time", timeupdate);
                    intent.putExtra("Temp", tempupdate);
                    intent.putExtra("_id", id);

                    getActivity().setResult(0, intent);
                    getActivity().finish();
                } else            // callled from tablet
                {
                    housesettingDetail.updateTemp(id,timeupdate,tempupdate);
                    housesettingDetail.removeFragment();

                    Toast.makeText(view.getContext(), " Data saved", Toast.LENGTH_LONG).show();
                }

            }
        });

                return gui;
    }
}
