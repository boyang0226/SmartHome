package com.example.bo.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AutoTemperatureFragment extends Fragment {    //auto temp fragment
    String temp;
    Long tempID;
    View gui;
    Context ctx;

    AutoListView autoLV = null;

    public AutoTemperatureFragment(){
    }

    public AutoTemperatureFragment (AutoListView a){
        autoLV = a;
    }

    @Override
    public void onCreate(Bundle b)    //get the passed bundle data
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        temp = bun.getString("Temperature");
        tempID = bun.getLong("id");
    }

    @Override
    public void onAttach(Activity activity) {  //create context object in fragment
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  //create view and set the passed bundle data
        gui = inflater.inflate(R.layout.activity_auto_temperature, null);
        final EditText message = (EditText)gui.findViewById(R.id.auto_TemperatureEditText);
        message.setText(temp);

        Button btn = (Button)gui.findViewById(R.id.auto_TemperatureSetButton);

        btn.setOnClickListener(new View.OnClickListener() {    ////when click set, create bundle data return to AutoListView class and update database
            @Override
            public void onClick(View view) {
                Log.i("AutoTemperatureFragment", "User clicked set auto temp button");

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);   //custom dialog box
                builder.setTitle(getString(R.string.auto_temp_dialog))
                        .setNegativeButton(getString(R.string.auto_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton(getString(R.string.auto_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
                                if (message !=null) {
                                    temp = message.getText().toString();
                                    if (autoLV == null) {               // called from phone
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("Temperature", temp);
                                        resultIntent.putExtra("id", tempID);
                                        getActivity().setResult(0, resultIntent);
                                        getActivity().finish();
                                    } else            // callled from tablet
                                    {
                                        autoLV.updateTemp(tempID, temp);
                                        autoLV.removeTempFragment();
                                    }
                                }
                            }
                        });
                builder.create().show();
            }
        });
        return gui;
    }
}


