package com.example.bo.smarthome;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

public class KitchenLightDetail extends KitchenBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_light_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenlight);
        setToolbarColor(toolbar);
        SeekBar dimmerBar = (SeekBar) findViewById(R.id.skbDimmer);
        dimmerBar.setMax(100);
        setSupportActionBar(toolbar);

        handleSwitchOnOff();
        handleSetDimmerBtn();
        handleDimmerSeekBarChange();

        DisableInuptControls(false);


        }

    private void handleSwitchOnOff() {
        Switch mainSWitch=(Switch)findViewById(R.id.swtMainSwitch);
        mainSWitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DisableInuptControls(true);
                }
                else {
                    DisableInuptControls(false);
                }
            }
        });
    }
    private void DisableInuptControls (boolean enabled) {

        final EditText dimmerLevel = (EditText)findViewById(R.id.edtDimmerLevel);
        final SeekBar dimmerBar = (SeekBar) findViewById(R.id.skbDimmer);
        final Button btnSetDimmer = (Button) findViewById(R.id.btnSetDimmer);

        dimmerLevel.setEnabled(enabled);
        btnSetDimmer.setEnabled(enabled);
        dimmerBar.setEnabled(enabled);
    }
    private void handleDimmerSeekBarChange() {

        SeekBar dimmerBar = (SeekBar) findViewById(R.id.skbDimmer);

        dimmerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                EditText dimmerLevel = (EditText)findViewById(R.id.edtDimmerLevel);
                dimmerLevel.setText(String.valueOf(progress));
            }

        });
}
    private void handleSetDimmerBtn() {
        Button btnSetDimmer = (Button) findViewById(R.id.btnSetDimmer);
        btnSetDimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dimmerLevel = (EditText)findViewById(R.id.edtDimmerLevel);
                SeekBar dimmerBar = (SeekBar)findViewById(R.id.skbDimmer);

                dimmerBar.setProgress(Integer.parseInt(dimmerLevel.getText().toString()));
            }
        });
    }

}
