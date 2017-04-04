package com.example.bo.smarthome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

public class KitchenLightFragment extends Fragment {

    private static final String ACTIVITY_NAME = "KitchenLight_";
    private KitchenLightFragment.LightSetting lightSetting;

    private int applianceId = 0;

    View frgRootView;
    private class LightSetting {
        private int id = 0;
        private boolean mainSwitch = false;
        private int dimmerLevel = 100;



        public LightSetting()
        {

        }
        public LightSetting(int id, boolean mainSwitch, int dimmerLevel)
        {   this.id = id;
            this.mainSwitch = mainSwitch;
            this.dimmerLevel = dimmerLevel;
        }
        public int getId()
        {
            return this.id;
        }

        public boolean getMainSwitch()
        {
            return mainSwitch;
        }
        public void setMainSwitch(boolean isChecked)
        {
            this.mainSwitch = isChecked;
        }

        public int getDimmerLevel()
        {
            return this.dimmerLevel;
        }
        public void setDimmerLevel(int dimmerLevel)
        {
            this.dimmerLevel = dimmerLevel;
        }
    }



    SQLiteDatabase db = null;

    public KitchenLightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bun = getArguments();
        applianceId = bun.getInt("applianceId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frgRootView = inflater.inflate(R.layout.fragment_kitchen_light, container, false);

        SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);
        dimmerBar.setMax(100);

        loadValuesFromDb();
        InitializeInuptControls();
        handleSwitchOnOff();
        handleSetDimmerBtn();
        handleDimmerSeekBarChange();
        return frgRootView;

        //View v = inflater.inflate(R.layout.fragment_mssage, null);
    }

    private void loadValuesFromDb() {
        String methodName = "ReadSetting";
        KitchenDatabaseHelper dbHelper = new KitchenDatabaseHelper(getContext());
        lightSetting = new KitchenLightFragment.LightSetting();
        db = dbHelper.getWritableDatabase();
        Cursor results = db.query(false, KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME,
                new String[]{KitchenDatabaseHelper.KEY_ID, KitchenDatabaseHelper.KEY_MAINSWITCH, KitchenDatabaseHelper.KEY_DIMMER_LEVEL}, KitchenDatabaseHelper.KEY_ID +"=" + String.valueOf(applianceId), null, null, null, null, null);

        int rows = results.getCount();
        if (rows > 0) {
            results.moveToFirst();
            Log.i(ACTIVITY_NAME + methodName, "SQL MESSAGE:" + "Read record no. "+ results.getInt( results.getColumnIndex( KitchenDatabaseHelper.KEY_ID) ) );

            int tempId = applianceId;
            boolean tempMainSwith = results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_MAINSWITCH))==0?false:true;
            int tempDimmerLevel = results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_DIMMER_LEVEL));
            lightSetting = new KitchenLightFragment.LightSetting(tempId, tempMainSwith, tempDimmerLevel);        }
    }

    private void handleSwitchOnOff() {
        Switch mainSWitch=(Switch)frgRootView.findViewById(R.id.swtMainSwitch);
        mainSWitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DisableInuptControls(true);
                    showToastMessage(true);
                }
                else {
                    DisableInuptControls(false);
                    showToastMessage(false);
                }

                saveLightSetting();



            }
        });
    }

    private void showToastMessage(boolean isChecked) {
        CharSequence text = null;
        int duration = Toast.LENGTH_SHORT;

        if (isChecked) {
            text = "Switch is On";
            duration = Toast.LENGTH_SHORT;
        } else
        {
            text = "Switch is Off";
            duration = Toast.LENGTH_LONG;
        }
        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();
    }

    private void saveLightSetting() {

        final Switch mainSWitch=(Switch)frgRootView.findViewById(R.id.swtMainSwitch);
        final SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);

        if (mainSWitch.isChecked())
            lightSetting.setMainSwitch(true);
        else
            lightSetting.setMainSwitch(false);

        lightSetting.setDimmerLevel(dimmerBar.getProgress());

        ContentValues values = new ContentValues();
        values.put(KitchenDatabaseHelper.KEY_MAINSWITCH, lightSetting.getMainSwitch()?1:0 );
        values.put(KitchenDatabaseHelper.KEY_DIMMER_LEVEL, lightSetting.getDimmerLevel() );

        //http://stackoverflow.com/questions/5987863/android-sqlite-update-statement
        if (lightSetting.getId() == 0)
        {
            db.insert(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, "", values);
        }
        else
        {
            db.update(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, values, KitchenDatabaseHelper.KEY_ID + "=" + lightSetting.getId(),null );

        }


    }

    private void DisableInuptControls (boolean enabled) {

        final EditText dimmerLevel = (EditText)frgRootView.findViewById(R.id.edtDimmerLevel);
        final SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);
        final Button btnSetDimmer = (Button) frgRootView.findViewById(R.id.btnSetDimmer);

        dimmerLevel.setEnabled(enabled);
        btnSetDimmer.setEnabled(enabled);
        dimmerBar.setEnabled(enabled);
    }
    private void InitializeInuptControls () {
        Switch mainSWitch=(Switch)frgRootView.findViewById(R.id.swtMainSwitch);
        final EditText dimmerLevel = (EditText)frgRootView.findViewById(R.id.edtDimmerLevel);
        final SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);
        final Button btnSetDimmer = (Button) frgRootView.findViewById(R.id.btnSetDimmer);

        dimmerBar.setProgress(lightSetting.getDimmerLevel());
        dimmerLevel.setText(String.valueOf(lightSetting.getDimmerLevel()));

        if (lightSetting.getMainSwitch()) {

            mainSWitch.setChecked(true);
            dimmerLevel.setEnabled(true);
            btnSetDimmer.setEnabled(true);
            dimmerBar.setEnabled(true);

        }
        else {

            mainSWitch.setChecked(false);
            dimmerLevel.setEnabled(false);
            btnSetDimmer.setEnabled(false);
            dimmerBar.setEnabled(false);
        }



    }
    private void handleDimmerSeekBarChange() {

        SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);

        dimmerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                EditText dimmerLevel = (EditText)frgRootView.findViewById(R.id.edtDimmerLevel);
                dimmerLevel.setText(String.valueOf(progress));
                saveLightSetting();
            }

        });
    }
    private void handleSetDimmerBtn() {
        Button btnSetDimmer = (Button) frgRootView.findViewById(R.id.btnSetDimmer);
        btnSetDimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dimmerLevel = (EditText)frgRootView.findViewById(R.id.edtDimmerLevel);
                SeekBar dimmerBar = (SeekBar)frgRootView.findViewById(R.id.skbDimmer);

                dimmerBar.setProgress(Integer.parseInt(dimmerLevel.getText().toString()));

                saveLightSetting();
                showSnackbarMessage();
            }
        });
    }
    private void showSnackbarMessage() {

        String snackBarMsg = "Dimmer level saved.";
        FrameLayout coordinatorLayout = (FrameLayout) frgRootView.findViewById(R.id.layoutFragmentKitchenLight);

        Snackbar.make(coordinatorLayout, snackBarMsg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


}
