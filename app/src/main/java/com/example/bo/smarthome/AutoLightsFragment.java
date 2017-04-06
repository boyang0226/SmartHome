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

public class AutoLightsFragment extends Fragment {
    boolean nSwitch;
    boolean hSwitch;
    int iBrightness;
    Long lightsID;
    View gui;
    Context ctx;

    AutoListView autoLV = null;

    public AutoLightsFragment(){
    }

    public AutoLightsFragment (AutoListView a){
        autoLV = a;
    }

    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        nSwitch = bun.getBoolean("NormalSwitch");
        hSwitch = bun.getBoolean("HeadSwitch");
        iBrightness = bun.getInt("InsideBrightness");
        lightsID = bun.getLong("id");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        gui = inflater.inflate(R.layout.activity_auto_light, null);
        final Switch normal = (Switch)gui.findViewById(R.id.auto_normallight_switch);
        normal.setChecked(nSwitch);
        normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        final Switch head = (Switch)gui.findViewById(R.id.auto_highlight_switch);
        head.setChecked(hSwitch);
        head.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        final SeekBar brightness = (SeekBar)gui.findViewById(R.id.auto_dimmablelight_seekBar);
        brightness.setMax(0);
        brightness.setMax(100);
        brightness.setProgress(iBrightness);

        Button btn = (Button)gui.findViewById(R.id.auto_light_setbutton);

        btn.setOnClickListener(new View.OnClickListener() {
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
                                    nSwitch = normal.isChecked();
                                    hSwitch = head.isChecked();
                                    iBrightness = brightness.getProgress();
                                    if (autoLV == null) {               // called from phone
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("NormalSwitch", nSwitch);
                                        resultIntent.putExtra("HeadSwitch", hSwitch);
                                        resultIntent.putExtra("InsideBrightness", iBrightness);
                                        resultIntent.putExtra("id", lightsID);
                                        getActivity().setResult(1, resultIntent);
                                        getActivity().finish();
                                    } else            // callled from tablet
                                    {
                                        autoLV.updateLights(lightsID, nSwitch, hSwitch, iBrightness);
                                        autoLV.removeFragment();
                                    }

                            }
                        });
                builder.create().show();
            }
        });
        return gui;
    }
}


