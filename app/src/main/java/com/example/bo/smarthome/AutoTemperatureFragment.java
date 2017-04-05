package com.example.bo.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AutoTemperatureFragment extends Fragment {
    String temp;
    Long tempID;
    View gui;

    AutoListView autoLV = null;

    public AutoTemperatureFragment(){
    }

    public AutoTemperatureFragment (AutoListView a){
        autoLV = a;
    }

    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        temp = bun.getString("Temperature");
        tempID = bun.getLong("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
         gui = inflater.inflate(R.layout.activity_auto_temperature, null);
        final EditText message = (EditText)gui.findViewById(R.id.auto_TemperatureEditText);
        message.setText(temp);

        Button btn = (Button)gui.findViewById(R.id.auto_TemperatureSetButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AutoTemperatureFragment", "User clicked set auto temp button");
                if (message !=null) {
                    String newTemp = message.getText().toString();
                    if (autoLV == null) {               // called from phone
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Temperature", newTemp);
                        resultIntent.putExtra("id", tempID);
                        resultIntent.putExtra("Response", "Auto Temperature is set");
                        getActivity().setResult(0, resultIntent);
                        getActivity().finish();
                    } else            // callled from tablet
                    {
                        autoLV.updateTemp(tempID, newTemp);
                        autoLV.removeFragment();
                    }
                }
            }
        });
        return gui;
    }
}


