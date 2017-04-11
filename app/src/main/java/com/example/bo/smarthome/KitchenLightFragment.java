package com.example.bo.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class KitchenLightFragment extends KitchenFragmentBase {

    private static final String ACTIVITY_NAME = "KitchenLight_";
    private KitchenLightFragment.LightSetting lightSetting;

    //set the light.
    private class LightSetting {
        private int id = 0;
        private boolean mainSwitch = false;
        private int dimmerLevel = 100;

        public LightSetting(){

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

    KitchenMain km = null;

    //constructor.
    public KitchenLightFragment() {
        // Required empty public constructor
    }
    public KitchenLightFragment(KitchenMain km) {
        this.km = km;
    }

    /**
     * Create the layout of the fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the fragment root view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frgRootView = inflater.inflate(R.layout.fragment_kitchen_light, container, false);

        SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);
        dimmerBar.setMax(100);

        initializeDB();
        loadValuesFromDb();
        InitializeInuptControls();
        handleSwitchOnOff();
        handleSetDimmerBtn();
        handleDimmerSeekBarChange();
        handleDeleteAppliance();

        return frgRootView;
    }

    /**
     * Delete an appliance.
     */
    private void handleDeleteAppliance() {
        Button btnDeleteAppliance = (Button) frgRootView.findViewById(R.id.btnDeleteKitchenAppliance);
        btnDeleteAppliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = getString(R.string.kitchen_light_delete_appliance_dialog_message)  + " " + applianceName + "?";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(msg)
                        .setTitle(R.string.kitchen_light_delete_appliance_dialog_title)
                        .setPositiveButton(R.string.dialog_positive_text_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteDatabaseRecords();
                                if (km == null) {
                                    getActivity().finish();
                                } else {
                                    km.removeFragment();
                                }

                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_text_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               // buttonView.setChecked(false);
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }
    //Delete the database records.
    private void deleteDatabaseRecords() {

//        try {
//            db.beginTransaction();
            db.delete(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, "id="+applianceId , null);
            db.delete(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "id="+applianceId , null);
//            db.endTransaction();
//            db.setTransactionSuccessful();
//            db.set
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Load the setting values from the database.
     */
    private void loadValuesFromDb() {
        String methodName = "ReadSetting";
        lightSetting = new KitchenLightFragment.LightSetting();
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

    /**
     * Handle the light switch on/off.
     */
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

    /**
     * Show the toast message when the light switch changes state.
     * @param isChecked
     */
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

    /**
     * Save the light setting.
     */
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

        /* Android SQLite: Update Statement [Webpage]. Retrieved from:
         * http://stackoverflow.com/questions/5987863/android-sqlite-update-statement
         */
        if (lightSetting.getId() == 0)
        {
            db.insert(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, "", values);
        }
        else
        {
            db.update(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, values, KitchenDatabaseHelper.KEY_ID + "=" + lightSetting.getId(),null );

        }
    }

    /**
     * Disable button
     * @param enabled
     */
    private void DisableInuptControls (boolean enabled) {

        final EditText dimmerLevel = (EditText)frgRootView.findViewById(R.id.edtDimmerLevel);
        final SeekBar dimmerBar = (SeekBar) frgRootView.findViewById(R.id.skbDimmer);
        final Button btnSetDimmer = (Button) frgRootView.findViewById(R.id.btnSetDimmer);

        dimmerLevel.setEnabled(enabled);
        btnSetDimmer.setEnabled(enabled);
        dimmerBar.setEnabled(enabled);
    }
    //initialize the dimmer input
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

    /**
     * Handle the Dimmer seekBar.
     */
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

    /**
     * Handle the light dimmer setting button.
     */
    private void handleSetDimmerBtn() {
        Button btnSetDimmer = (Button) frgRootView.findViewById(R.id.btnSetDimmer);
        //button handler
        btnSetDimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dimmerLevel = (EditText)frgRootView.findViewById(R.id.edtDimmerLevel);
                SeekBar dimmerBar = (SeekBar)frgRootView.findViewById(R.id.skbDimmer);

                String dimmerLevelValue = dimmerLevel.getText().toString();
                dimmerBar.setProgress(Integer.parseInt(dimmerLevelValue));
                //show snackbar message
                saveLightSetting();
                showSnackbarMessage(dimmerLevelValue);
            }
        });
    }

    /**
     * Show snackBar message.
     * @param dimmerLevelValue light dimmer percentage
     */
    private void showSnackbarMessage(String dimmerLevelValue) {

        String snackBarMsg = applianceName + " dimmer level " + dimmerLevelValue +"% saved.";
        FrameLayout coordinatorLayout = (FrameLayout) frgRootView.findViewById(R.id.layoutFragmentKitchenLight);

        Snackbar.make(coordinatorLayout, snackBarMsg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


}
