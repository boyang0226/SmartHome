package com.example.bo.smarthome;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
    Switch tvSwitch;
    SeekBar dimmableLampSeekBar;
    SeekBar smartLampSeekBar;
    SeekBar BlindsSeekBar;
    SeekBar tvVolumeSeekBar;
    SeekBar tvChannelSeekBar;
    Spinner smartLampSpinner;
    TextView brightnessLabel;
    TextView heightLabel;
    Spinner typeSpinner;
    EditText deviceInput;
    TextView tvVolumnLabel;
    TextView tvChannelLabel;
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
        isTablet = bun.getBoolean("isTablet");
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
            // display add new device view
            gui = inflater.inflate(R.layout.activity_livingroom_add_item_content, null);
            deviceInput = (EditText)gui.findViewById(R.id.lr_add_device_name_edit);
            typeSpinner = (Spinner)gui.findViewById(R.id.lr_spinner);
        }else if (deviceType.equals("Simple Lamp")){
            // display simple lamp view
            gui = inflater.inflate(R.layout.activity_livingroom_simple_lamp_content, null);
            SimpleLampSwitch = (Switch)gui.findViewById(R.id.lr_simple_lamp_switch);
            SimpleLampSwitch.setChecked(deviceSwitch);
        }else if (deviceType.equals("Dimmable Lamp")){
            // display dimmable lamp view
            gui = inflater.inflate(R.layout.activity_livingroom_dimmable_lamp_content, null);
            dimmableLampSeekBar = (SeekBar)gui.findViewById(R.id.lr_dimmable_lamp_seekBar);
            dimmableLampSeekBar.setMax(0);
            dimmableLampSeekBar.setMax(100);
            dimmableLampSeekBar.setProgress(brightness);

            dimmableLampSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar){
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    brightnessLabel.setText(String.valueOf(progress));
                }
            });
            brightnessLabel = (TextView)gui.findViewById(R.id.lr_dimmable_lamp_seekBar_value);
            brightnessLabel.setText(String.valueOf(brightness));
        }else if (deviceType.equals("Smart Lamp")){
            // display smart lamp view
            gui = inflater.inflate(R.layout.activity_livingroom_smart_lamp_content, null);
            smartLampSeekBar = (SeekBar)gui.findViewById(R.id.lr_smart_lamp_seekBar);
            smartLampSeekBar.setMax(0);
            smartLampSeekBar.setMax(100);
            smartLampSeekBar.setProgress(brightness);

            brightnessLabel = (TextView)gui.findViewById(R.id.lr_smart_lamp_seekBar_value);
            brightnessLabel.setText(String.valueOf(brightness));

            smartLampSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar){
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    brightnessLabel.setText(String.valueOf(progress));
                }
            });

            smartLampSpinner = (Spinner)gui.findViewById(R.id.lr_color_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.lr_smart_lamp_color, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            smartLampSpinner.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(color);
            smartLampSpinner.setSelection(spinnerPosition);

        }else if (deviceType.equals("Television")){
            // display tv view
            gui = inflater.inflate(R.layout.activity_livingroom_tv_content, null);
            tvSwitch = (Switch)gui.findViewById(R.id.lr_tv_switch);
            tvSwitch.setChecked(deviceSwitch);
            tvVolumeSeekBar = (SeekBar)gui.findViewById(R.id.lr_tv_volume_seekBar);
            tvVolumeSeekBar.setMax(1);
            tvVolumeSeekBar.setMax(100);
            tvVolumeSeekBar.setProgress(volume);
            tvChannelSeekBar = (SeekBar)gui.findViewById(R.id.lr_tv_channel_seekBar);
            tvChannelSeekBar.setMax(0);
            tvChannelSeekBar.setMax(100);
            tvChannelSeekBar.setProgress(channel);

            tvVolumnLabel = (TextView)gui.findViewById(R.id.lr_tv_volumn_seekBar_value);
            tvVolumnLabel.setText(String.valueOf(volume));
            tvChannelLabel= (TextView)gui.findViewById(R.id.lr_tv_channel_seekBar_value);
            tvChannelLabel.setText(String.valueOf(channel));

            tvVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar){
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    tvVolumnLabel.setText(String.valueOf(progress));
                }
            });

            tvChannelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar){
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    tvChannelLabel.setText(String.valueOf(progress));
                }
            });

            Button upButton = (Button)gui.findViewById(R.id.lr_tv_up_button);
            Button downButton = (Button)gui.findViewById(R.id.lr_tv_down_button);
            Button leftButton = (Button)gui.findViewById(R.id.lr_tv_left_button);
            Button rightButton = (Button)gui.findViewById(R.id.lr_tv_right_button);

            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int volume = tvVolumeSeekBar.getProgress();
                    if (volume < 100)
                        tvVolumeSeekBar.setProgress(volume + 1);
                }
            });
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int volume = tvVolumeSeekBar.getProgress();
                    if (volume > 0)
                        tvVolumeSeekBar.setProgress(volume - 1);
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int channel = tvChannelSeekBar.getProgress();
                    if (channel < 100)
                        tvChannelSeekBar.setProgress(channel + 1);
                }
            });
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int channel = tvChannelSeekBar.getProgress();
                    if (channel > 1)
                        tvChannelSeekBar.setProgress(channel - 1);
                }
            });

        }else if (deviceType.equals("Window Blinds")){
            // display window blinds view
            gui = inflater.inflate(R.layout.activity_livingroom_blinds_content, null);
            BlindsSeekBar = (SeekBar)gui.findViewById(R.id.lr_blinds_seekBar);
            BlindsSeekBar.setMax(0);
            BlindsSeekBar.setMax(100);
            BlindsSeekBar.setProgress(height);

            BlindsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar){
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    heightLabel.setText(String.valueOf(progress));
                }
            });

            heightLabel = (TextView)gui.findViewById(R.id.lr_blinds_seekBar_value);
            heightLabel.setText(String.valueOf(height));
        }

        //Delete event handler
        Button deleteButton = (Button)gui.findViewById(R.id.lr_delete);
        if (deleteButton != null)
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.delete_confirm_message);

                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id1) {
                            // User clicked OK button
                            if (!isTablet){
                                Intent intent = new Intent();
                                intent.putExtra("id" ,id);
                                getActivity().setResult(3, intent);
                                getActivity().finish();
                            }
                            else{
                                livingroomList.deleteDbDevice(id);
                                livingroomList.removeFragment();
                                livingroomList.showDeleteMessage();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();



                }
            });

        //Submit event handler
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
                        getActivity().setResult(2, intent);
                        getActivity().finish();
                    }
                    else{
                        livingroomList.insertDbDevice(deviceName, deviceType);
                        livingroomList.removeFragment();
                        livingroomList.showSaveMessage();
                    }

                }
            });
        }

        //save event handler
        Button saveButton = (Button)gui.findViewById(R.id.lr_save);
        if (saveButton != null)
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
                    intent.putExtra("deviceType" ,deviceType);
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
                        tvChannel = tvChannelSeekBar.getProgress();
                        tvVolume = tvVolumeSeekBar.getProgress();
                        tvSwitchValue = tvSwitch.isChecked();
                        intent.putExtra("channel", tvChannel);
                        intent.putExtra("volume", tvVolume);
                        intent.putExtra("switch", tvSwitchValue);
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
                            livingroomList.updateDbTV(id, tvSwitchValue, tvChannel, tvVolume);
                        }else if (deviceType.equals("Window Blinds")){
                            livingroomList.updateDbBlinds(id, blindsHeight);
                        }
                        livingroomList.showUpdateMessage();
                        livingroomList.refreshMessages();
                        livingroomList.removeFragment();
                    }

                }
            });
        return gui;
    }

}



