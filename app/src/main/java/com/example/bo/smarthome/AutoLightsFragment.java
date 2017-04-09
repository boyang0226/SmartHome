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

// http://stackoverflow.com/questions/29677812/why-is-onattach-called-before-oncreate

public class AutoLightsFragment extends Fragment {   //auto lights fragment
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

    @Override
    public void onCreate(Bundle b)   //get the passed bundle data
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        nSwitch = bun.getBoolean("NormalSwitch");
        hSwitch = bun.getBoolean("HeadSwitch");
        iBrightness = bun.getInt("InsideBrightness");
        lightsID = bun.getLong("id");
    }

    @Override
    public void onAttach(Activity activity) {    //create context object in fragment
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //create view and set the passed bundle data

        gui = inflater.inflate(R.layout.activity_auto_light, null);
        final Switch normal = (Switch)gui.findViewById(R.id.auto_normallight_switch);
        normal.setChecked(nSwitch);
        normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence text;
                int duration;
                if (compoundButton.isChecked()) {
                    text = getString(R.string.auto_switch_on);
                    duration = Toast.LENGTH_SHORT;
                } else {
                    text = getString(R.string.auto_switch_off);
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
                    text = getString(R.string.auto_switch_on);
                    duration = Toast.LENGTH_SHORT;
                } else {
                    text = getString(R.string.auto_switch_off);
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

        btn.setOnClickListener(new View.OnClickListener() {  //when click set, create bundle data return to AutoListView class and update database
            @Override
            public void onClick(View view) {
                Log.i("AutoLightsFragment", "User clicked set auto light button");

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(getString(R.string.auto_light_dialog))
                        .setNegativeButton(getString(R.string.auto_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton(getString(R.string.auto_yes), new DialogInterface.OnClickListener() {
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
                                        autoLV.removeLightsFragment();
                                    }

                            }
                        });
                builder.create().show();
            }
        });
        return gui;
    }
}


