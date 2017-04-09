package com.example.bo.smarthome;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class KitchenMicrowaveFragment extends KitchenFragmentBase {

    private static final int MAX_MINUTE = 30;
    private static final int MAX_SECOND = 59;

    private class MicrowaveSetting {

        public static final String STATE_START = "START";
        public static final String STATE_STOP = "STOP";
        public static final String STATE_RESET = "RESET";
        private int id = 0;
        private int minute = 0;

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        private int second = 0;
        private String state = "RESET";
        public MicrowaveSetting()
        {
        }
        public MicrowaveSetting(int id, int minute, int second, String state)
        {   this.id = id;
            this.minute = minute;
            this.second = second;
            this.state = state;
        }
        public int getId()
        {
            return this.id;
        }

    }

    KitchenMain km;
    private static final String ACTIVITY_NAME = "KitchenMicrowave_";
    private static  CountDownTimer counterDownTimer = null;
    private static  boolean runningBackground = false;
    private MicrowaveSetting microwaveSetting;
    private static NumberPicker npMinute = null;
    private static NumberPicker npSecond =  null;
    private static Button btnStart;
    private static Button btnReset;
    private static Button btnStop;


    /**
     * Default constructor
     */
    public KitchenMicrowaveFragment() {
        // Required empty public constructor
    }

    /**
     * Constructor take the km parameter
     * @param km KitchenMain object
     */
    public KitchenMicrowaveFragment(KitchenMain km){
        this.km = km;
    }


    @Override
    protected void kitchenFragmentOnCreate() {
        super.kitchenFragmentOnCreate();
        initializeDB();
        loadValuesFromDb();
    }

    private void loadValuesFromDb() {
        String methodName = "ReadMicrowaveSetting";
        microwaveSetting = new KitchenMicrowaveFragment.MicrowaveSetting();
        Cursor results = db.query(false, KitchenDatabaseHelper.KITCHEN_MICROWAVE_TABLE_NAME,
                new String[]{KitchenDatabaseHelper.KEY_ID, KitchenDatabaseHelper.KEY_MICROWAVE_MINUTE, KitchenDatabaseHelper.KEY_MICROWAVE_SECOND, KitchenDatabaseHelper.KEY_MICROWAVE_STATE}, KitchenDatabaseHelper.KEY_ID +"=" + String.valueOf(applianceId), null, null, null, null, null);

        int rows = results.getCount();
        if (rows > 0) {
            results.moveToFirst();
            Log.i(ACTIVITY_NAME + methodName, "SQL MESSAGE:" + "Read record no. "+ results.getInt( results.getColumnIndex( KitchenDatabaseHelper.KEY_ID) ) );

            int tempId = applianceId;
            int tempMinute = results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_MICROWAVE_MINUTE));
            int tempSecond = results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_MICROWAVE_SECOND));
            String tempState = results.getString(results.getColumnIndex(KitchenDatabaseHelper.KEY_MICROWAVE_STATE));
            microwaveSetting = new KitchenMicrowaveFragment.MicrowaveSetting(tempId, tempMinute, tempSecond, tempState);        }

    }

    @Override
    protected void saveSettingsToDB() {
        super.saveSettingsToDB();

        //if (microwaveSetting.getState() != MicrowaveSetting.STATE_START) {
            ContentValues values = new ContentValues();
            values.put(KitchenDatabaseHelper.KEY_MICROWAVE_MINUTE, microwaveSetting.getMinute());
            values.put(KitchenDatabaseHelper.KEY_MICROWAVE_SECOND, microwaveSetting.getSecond());
            values.put(KitchenDatabaseHelper.KEY_MICROWAVE_STATE, microwaveSetting.getState());

            //http://stackoverflow.com/questions/5987863/android-sqlite-update-statement
            if (microwaveSetting.getId() == 0) {
                db.insert(KitchenDatabaseHelper.KITCHEN_MICROWAVE_TABLE_NAME, "", values);
            } else {
                db.update(KitchenDatabaseHelper.KITCHEN_MICROWAVE_TABLE_NAME, values, KitchenDatabaseHelper.KEY_ID + "=" + microwaveSetting.getId(), null);
            }
        //}
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frgRootView = inflater.inflate(R.layout.fragment_kitchen_microwave, container, false);

        return frgRootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        npMinute = (NumberPicker) view.findViewById(R.id.numMinutes);
        npSecond = (NumberPicker) view.findViewById(R.id.numSeconds);
        btnStart = (Button) view.findViewById(R.id.btnMicrowaveStart);
        btnReset = (Button) view.findViewById(R.id.btnMicrowaveReset);
        btnStop = (Button) view.findViewById(R.id.btnMicrowaveStop);

        //initialize ui control to their default state and value
        initNumberPicker(npMinute,MAX_MINUTE);
        initNumberPicker(npSecond,MAX_SECOND);

        //Set control state and value based on the saved values from DB
        //values are retrieved from kitchenFragmentOnCreate method which
        // is called before onViewCreated method
        if (microwaveSetting.getState() != MicrowaveSetting.STATE_START)
            setMinutesSecondsFrom(microwaveSetting);
        setButtonsByState(microwaveSetting.getState());
        //event handlers on this fragment
        handleStartButton();
        handleStopButton();
        handleResetButton();

        handleDeleteAppliance();
        //show current state
        //ShowToaster(String.format("Microwave: %s %s %s %s", microwaveSetting.getId(),
         //       microwaveSetting.getMinute() , microwaveSetting.getSecond() , microwaveSetting.getState()));
    }

    /**
     * Initilize the numberPicker value and format in 2 digits.
     * @param np numberpicker object.
     * @param value the initial value of the numberpicker.
     */
    private void initNumberPicker(NumberPicker np, int value)
    {
        np.setMaxValue(value);
        np.setMinValue(0);
        np.setValue(0);

        /* Add leading zeroes to number in Java? [Webpage]. Retrieved from:
         * http://stackoverflow.com/questions/275711/add-leading-zeroes-to-number-in-java
         */
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

    }
    private void setMinutesSecondsFrom(MicrowaveSetting setting) {
        npMinute.setValue(setting.getMinute());
        npSecond.setValue(setting.getSecond());
    }
    private void setButtonsByState(String state) {
        try {
            switch (state){
                case MicrowaveSetting.STATE_START:
                    setButtonText(btnStart, R.string.kitchen_microwave_start_button_text, false);
                    setButtonText(btnStop, R.string.kitchen_microwave_stop_button_text, true);
                    setButtonText(btnReset, R.string.kitchen_microwave_reset_button_text, false);
                    break;
                case MicrowaveSetting.STATE_RESET:
                    setButtonText(btnStart, R.string.kitchen_microwave_start_button_text, true);
                    setButtonText(btnStop, R.string.kitchen_microwave_stop_button_text, false);
                    setButtonText(btnReset, R.string.kitchen_microwave_reset_button_text, true);
                    break;
                case MicrowaveSetting.STATE_STOP:
                    setButtonText(btnStart, R.string.kitchen_microwave_resume_button_text, true);
                    setButtonText(btnStop, R.string.kitchen_microwave_stop_button_text, false);
                    setButtonText(btnReset, R.string.kitchen_microwave_reset_button_text, true);
                    break;
                default:
                    break;
            }

        } catch (Exception ex)
        {


        }


    }
    private void setButtonText(Button btnStart, int text, boolean enable) {
        btnStart.setText(text);
        btnStart.setEnabled(enable);
    }

    private void handleResetButton() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npMinute.setValue(0);
                npSecond.setValue(0);
                microwaveSetting.setState(MicrowaveSetting.STATE_RESET);
                stopCountDown();
            }
        });
    }

    private void stopCountDown() {
        if (counterDownTimer != null) {
            counterDownTimer.cancel();
            counterDownTimer = null;
        }
        setButtonsByState(microwaveSetting.getState());
        recordValues(npSecond.getValue(), npMinute.getValue());
        saveSettingsToDB();
    }

    private void handleStopButton() {
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microwaveSetting.setState(MicrowaveSetting.STATE_STOP);
                stopCountDown();
            }
        });
    }
    private void handleStartButton() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int startMin = npMinute.getValue();
                int startSec = npSecond.getValue();

                microwaveSetting.setState(MicrowaveSetting.STATE_START);
                setButtonsByState(microwaveSetting.getState());

                long futureSec = startMin * 60 * 1000 + startSec * 1000 + 1000;
                    counterDownTimer = new CountDownTimer(futureSec, 1000) {

                        public void onTick(long millisUntilFinished) {

                            int curSecVal = npSecond.getValue();
                            int curMinVal = npMinute.getValue();
                            int nextSecVal = curSecVal;
                            int nextMinVal = curMinVal;
                            if (curSecVal == 0) {
                                nextSecVal = 59;
                                if (curMinVal == 0) {
                                    nextMinVal = 0;
                                    nextSecVal = 0;
                                } else {
                                    nextMinVal = curMinVal - 1;
                                }
                            } else {
                                nextSecVal = curSecVal - 1;
                            }
                            npSecond.setValue(nextSecVal);
                            npMinute.setValue(nextMinVal);
                            recordValues(nextSecVal, nextMinVal);
                            saveSettingsToDB();
                        }

                        public void onFinish() {

                            recordValues(npSecond.getValue(), npMinute.getValue());
                            microwaveSetting.setState(MicrowaveSetting.STATE_RESET);
                            setButtonsByState(microwaveSetting.getState());
                            saveSettingsToDB();
                            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            v.vibrate(500);
                            //if(!runningBackground)

                        }
                    };

                    counterDownTimer.start();
                    saveSettingsToDB();
            }
        });
    }

    private void recordValues(int nextSecVal, int nextMinVal) {
        microwaveSetting.setMinute(nextMinVal);
        microwaveSetting.setSecond(nextSecVal);
    }

    /**
     * Event handler for delete button
     */
    private void handleDeleteAppliance() {
        Button btnDeleteAppliance = (Button) frgRootView.findViewById(R.id.btnMicrowaveDelete);
        btnDeleteAppliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = getString(R.string.kitchen_light_delete_appliance_dialog_message)  + " " + applianceName + "?";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(msg)
                        .setTitle(R.string.kitchen_microwave_delete_appliance_dialog_title)
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
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });


    }

    /**
     * delete microwave recrod
      */
    private void deleteDatabaseRecords() {
        db.delete(KitchenDatabaseHelper.KITCHEN_MICROWAVE_TABLE_NAME, "id="+applianceId , null);
        db.delete(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "id="+applianceId , null);
   }

    @Override
    public void onPause() {
        recordValues(npSecond.getValue(), npMinute.getValue());
        super.onPause();
        runningBackground = true;
    }

    private void ShowToaster(String msg) {
        CharSequence text = null;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg , duration);
        toast.show();
    }
}
