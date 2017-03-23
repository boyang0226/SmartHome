package com.example.bo.smarthome;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Open on 3/21/2017.
 */

public class LivingroomFragment extends Fragment {

    Long id;
    String deviceName;
    boolean deviceSwitch;
    int brightness;
    String color;
    int volume;
    int channel;
    int height;
    String deviceType;
    boolean addNewDevice;
    boolean isTablet;
    LivingroomList livingroomList = null;

    View gui;
    Switch SimpleLampSwitch;
    SeekBar dimmableLampSeekBar;
    SeekBar smartLampSeekBar;
    SeekBar BlindsSeekBar;
    Spinner smartLampSpinner;
    TextView brightnessLabel;
    TextView heightLabel;
    Spinner typeSpinner;
    EditText deviceInput;

    public LivingroomFragment(){}

    public LivingroomFragment(LivingroomList lrList){
        livingroomList = lrList;
    }

    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        id = bun.getLong("id");
        deviceName = bun.getString("deviceName");
        deviceType = bun.getString("deviceType");
        deviceSwitch = bun.getBoolean("switch");
        color = bun.getString("color");
        brightness = bun.getInt("brightness");
        channel = bun.getInt("channel");
        volume = bun.getInt("volume");
        height = bun.getInt("height");
        brightness = bun.getInt("brightness");
        addNewDevice = bun.getBoolean("addNewDevice", false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (addNewDevice) {
            gui = inflater.inflate(R.layout.activity_livingroom_item_details, null);
            deviceInput = (EditText)gui.findViewById(R.id.lr_add_device_name_edit);
            typeSpinner = (Spinner)gui.findViewById(R.id.lr_spinner);
        }else if (deviceType.equals("Simple Lamp")){
            gui = inflater.inflate(R.layout.activity_livingroom_simple_lamp_content, null);
            SimpleLampSwitch = (Switch)gui.findViewById(R.id.lr_simple_lamp_switch);
            SimpleLampSwitch.setChecked(deviceSwitch);
        }else if (deviceType.equals("Dimmable Lamp")){
            gui = inflater.inflate(R.layout.activity_livingroom_dimmable_lamp_content, null);
            dimmableLampSeekBar = (SeekBar)gui.findViewById(R.id.lr_dimmable_lamp_seekBar);
            dimmableLampSeekBar.setMax(0);
            dimmableLampSeekBar.setMax(100);
            dimmableLampSeekBar.setProgress(brightness);

            brightnessLabel = (TextView)gui.findViewById(R.id.lr_dimmable_lamp_brightness_label);
            brightnessLabel.setText(brightness);
        }else if (deviceType.equals("Smart Lamp")){
            gui = inflater.inflate(R.layout.activity_livingroom_smart_lamp_content, null);
            smartLampSeekBar = (SeekBar)gui.findViewById(R.id.lr_smart_lamp_seekBar);
            smartLampSeekBar.setMax(0);
            smartLampSeekBar.setMax(100);
            smartLampSeekBar.setProgress(brightness);

            smartLampSpinner = (Spinner)gui.findViewById(R.id.lr_color_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(livingroomList, R.array.lr_smart_lamp_color, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            smartLampSpinner.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(color);
            smartLampSpinner.setSelection(spinnerPosition);

        }else if (deviceType.equals("Television")){
            gui = inflater.inflate(R.layout.activity_livingroom_tv_content, null);
            // TODO

        }else if (deviceType.equals("Window Blinds")){
            gui = inflater.inflate(R.layout.activity_livingroom_blinds_content, null);
            BlindsSeekBar = (SeekBar)gui.findViewById(R.id.lr_blinds_seekBar);
            BlindsSeekBar.setMax(0);
            BlindsSeekBar.setMax(100);
            BlindsSeekBar.setProgress(height);
            heightLabel = (TextView)gui.findViewById(R.id.lr_blinds_seekBar_value);
            heightLabel.setText(height);
        }

        Button deleteButton = (Button)gui.findViewById(R.id.lr_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isTablet){
                    Intent intent = new Intent();
                    intent.putExtra("id" ,id);
                    getActivity().setResult(0, intent);
                    getActivity().finish();
                }
                else{
                    livingroomList.deleteDbDevice(id);
                    livingroomList.removeFragment();
                }
            }
        });
        if (gui.findViewById(R.id.lr_list_details_content_submit) != null){
            Button submitButton = (Button)gui.findViewById(R.id.lr_list_details_content_submit);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String deviceName = deviceInput.getText().toString();
                    String deviceType = typeSpinner.getSelectedItem().toString();
                    if (!isTablet){
                        Intent intent = new Intent();
                        intent.putExtra("deviceName", deviceName);
                        intent.putExtra("deviceType", deviceType);
                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    }
                    else{
                        livingroomList.insertDbDevice(deviceName, deviceType);
                        livingroomList.removeFragment();
                    }

                }
            });
        }
        Button saveButton = (Button)gui.findViewById(R.id.lr_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean simpleLampSwitchValue = false;
                int dimmableLampBrightness = 0;
                int smartLampBrighteness = 0;
                String  smartLampColor = "White";
                boolean tvSwitchValue = false;
                int tvChannel = 1;
                int tvVolume = 0;
                int blindsHeight = 0;
                Intent intent = new Intent();
                intent.putExtra("id" ,id);

                if (deviceType.equals("Simple Lamp")){
                    simpleLampSwitchValue = SimpleLampSwitch.isChecked();
                    intent.putExtra("switch", simpleLampSwitchValue);
                }else if (deviceType.equals("Dimmable Lamp")){
                    dimmableLampBrightness = dimmableLampSeekBar.getProgress();
                    intent.putExtra("brightness", dimmableLampBrightness);
                }else if (deviceType.equals("Smart Lamp")){
                    smartLampBrighteness = smartLampSeekBar.getProgress();
                    smartLampColor = smartLampSpinner.getSelectedItem().toString();
                    intent.putExtra("brightness", smartLampBrighteness);
                    intent.putExtra("color", smartLampColor);
                }else if (deviceType.equals("Television")){
                    // TODO
                }else if (deviceType.equals("Window Blinds")){
                    blindsHeight = BlindsSeekBar.getProgress();
                    intent.putExtra("height", blindsHeight);
                }

                if (!isTablet){
                    getActivity().setResult(1, intent);
                    getActivity().finish();
                }
                else{
                    if (deviceType.equals("Simple Lamp")){
                        livingroomList.updateDbSimpleLamp(id, simpleLampSwitchValue);
                    }else if (deviceType.equals("Dimmable Lamp")){
                        livingroomList.updateDbDimmableLamp(id, dimmableLampBrightness);
                    }else if (deviceType.equals("Smart Lamp")){
                        livingroomList.updateDbSmartLamp(id, smartLampBrighteness, smartLampColor);
                    }else if (deviceType.equals("Television")){
                        // TODO
                    }else if (deviceType.equals("Window Blinds")){
                        livingroomList.updateDbBlinds(id, blindsHeight);
                    }
                    livingroomList.removeFragment();
                }
            }
        });
        return gui;
    }

}



