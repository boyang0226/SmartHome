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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;


public class AutoRadioFragment extends Fragment {
    boolean mute;
    int volumn;
    Long radioID;
    String radio1;
    String radio2;
    String radio3;
    String radio4;
    String radio5;
    String radio6;
    View gui;
    Context ctx;

    AutoListView autoLV = null;

    public AutoRadioFragment(){
    }

    public AutoRadioFragment (AutoListView a){
        autoLV = a;
    }

    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        mute = bun.getBoolean("Mute");
        volumn = bun.getInt("Volumn");
        radio1 = bun.getString("Station1");
        radio2 = bun.getString("Station2");
        radio3 = bun.getString("Station3");
        radio4 = bun.getString("Station4");
        radio5 = bun.getString("Station5");
        radio6 = bun.getString("Station6");
        radioID = bun.getLong("id");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        gui = inflater.inflate(R.layout.activity_auto_radio_stations, null);
        final Switch muteSwitch = (Switch)gui.findViewById(R.id.auto_radio_mute_switch);
        muteSwitch.setChecked(mute);
        muteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence text;
                int duration;
                if (compoundButton.isChecked()) {
                    text = "Switch is On";
                    duration = Toast.LENGTH_SHORT;
                } else {
                    text = "Switch is Off";
                    duration = Toast.LENGTH_SHORT;
                }

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
            }
        });

        final SeekBar volumnSeekbar = (SeekBar)gui.findViewById(R.id.auto_radio_volumn_seekBar);
        volumnSeekbar.setMax(0);
        volumnSeekbar.setMax(100);
        volumnSeekbar.setProgress(volumn);

        Button btnSet = (Button)gui.findViewById(R.id.auto_light_setbutton);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AutoLightsFragment", "User clicked set auto light button");

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Do you want to set lights?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
//                                //boolean nSwitch1 = false;
//                                //boolean hSwitch1 = false;
//                                nSwitch = normal.isChecked();
//                                hSwitch = head.isChecked();
//                                iBrightness = brightness.getProgress();
//                                if (autoLV == null) {               // called from phone
//                                    Intent resultIntent = new Intent();
//                                    resultIntent.putExtra("NormalSwitch", nSwitch);
//                                    resultIntent.putExtra("HeadSwitch", hSwitch);
//                                    resultIntent.putExtra("InsideBrightness", iBrightness);
//                                    resultIntent.putExtra("id", lightsID);
//                                    getActivity().setResult(1, resultIntent);
//                                    getActivity().finish();
//                                } else            // callled from tablet
//                                {
//                                    autoLV.updateLights(lightsID, nSwitch, hSwitch, iBrightness);
//                                    autoLV.removeFragment();
//                                }

                            }
                        });
                builder.create().show();
            }
        });
        return gui;
    }
}
