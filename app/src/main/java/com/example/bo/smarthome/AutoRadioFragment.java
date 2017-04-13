package com.example.bo.smarthome;
// author  Zhen Qu
// student number 040587623

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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;


public class AutoRadioFragment extends Fragment {   //auto radio fragment
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

    @Override
    public void onCreate(Bundle b)   //get the passed bundle data
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
    public void onAttach(Activity activity) {    //create context object in fragment
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  //create view and set the passed bundle data

        gui = inflater.inflate(R.layout.activity_auto_radio_stations, null);
        final Switch muteSwitch = (Switch)gui.findViewById(R.id.auto_radio_mute_switch);
        muteSwitch.setChecked(mute);
        muteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence text;
                int duration;
                if (compoundButton.isChecked()) {
                    text = (getString(R.string.auto_switch_on));
                    duration = Toast.LENGTH_SHORT;
                } else {
                    text = (getString(R.string.auto_switch_off));
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

        final EditText radiostation1 = (EditText)gui.findViewById(R.id.auto_radiostation1_editText);
        radiostation1.setText(radio1);
        final EditText radiostation2 = (EditText)gui.findViewById(R.id.auto_radiostation2_editText);
        radiostation2.setText(radio2);
        final EditText radiostation3 = (EditText)gui.findViewById(R.id.auto_radiostation3_editText);
        radiostation3.setText(radio3);
        final EditText radiostation4 = (EditText)gui.findViewById(R.id.auto_radiostation4_editText);
        radiostation4.setText(radio4);
        final EditText radiostation5 = (EditText)gui.findViewById(R.id.auto_radiostation5_editText);
        radiostation5.setText(radio5);
        final EditText radiostation6 = (EditText)gui.findViewById(R.id.auto_radiostation6_editText);
        radiostation6.setText(radio6);

        Button btnSet = (Button)gui.findViewById(R.id.auto_radiostation1_setbutton);

        btnSet.setOnClickListener(new View.OnClickListener() {    //when click set, create bundle data return to AutoListView class and update database
            @Override
            public void onClick(View view) {
                Log.i("AutoLightsFragment", "User clicked set auto light button");

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(getString(R.string.auto_radio_set_dialog))
                        .setNegativeButton(getString(R.string.auto_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton(getString(R.string.auto_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
                                mute = muteSwitch.isChecked();
                                volumn = volumnSeekbar.getProgress();
                                radio1= radiostation1.getText().toString();
                                radio2= radiostation2.getText().toString();
                                radio3= radiostation3.getText().toString();
                                radio4= radiostation4.getText().toString();
                                radio5= radiostation5.getText().toString();
                                radio6= radiostation6.getText().toString();
                                if (autoLV == null) {               // called from phone
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("Mute", mute);
                                    resultIntent.putExtra("Volumn", volumn);
                                    resultIntent.putExtra("Station1", radio1);
                                    resultIntent.putExtra("Station2", radio2);
                                    resultIntent.putExtra("Station3", radio3);
                                    resultIntent.putExtra("Station4", radio4);
                                    resultIntent.putExtra("Station5", radio5);
                                    resultIntent.putExtra("Station6", radio6);
                                    resultIntent.putExtra("id", radioID);
                                    getActivity().setResult(3, resultIntent);
                                    getActivity().finish();
                                } else            // callled from tablet
                                {
                                    autoLV.updateRadio(radioID, mute, volumn, radio1, radio2, radio3, radio4, radio5, radio6);
                                    autoLV.removeRadioFragment();
                                }
                            }
                        });
                builder.create().show();
            }
        });

        Button btnClear = (Button)gui.findViewById(R.id.auto_radiostation1_clearbutton);

        btnClear.setOnClickListener(new View.OnClickListener() {    //when click clear, set the components to initial state
            @Override
            public void onClick(View view) {
                Log.i("AutoLightsFragment", "User clicked set auto light button");

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(getString(R.string.auto_radio_clear_dialog))
                        .setNegativeButton(getString(R.string.auto_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("No", "No");
                            }
                        })
                        .setPositiveButton(getString(R.string.auto_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Yes", "Yes");
                                muteSwitch.setChecked(false);
                                volumnSeekbar.setProgress(0);
                                radiostation1.setText("");
                                radiostation2.setText("");
                                radiostation3.setText("");
                                radiostation4.setText("");
                                radiostation5.setText("");
                                radiostation6.setText("");
                            }
                        });
                builder.create().show();
            }
        });
        return gui;
    }
}
