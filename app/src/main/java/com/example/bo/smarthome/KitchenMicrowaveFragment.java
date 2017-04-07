package com.example.bo.smarthome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KitchenMicrowaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KitchenMicrowaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KitchenMicrowaveFragment extends KitchenFragmentBase {

    KitchenMain km;
    private static final String ACTIVITY_NAME = "KitchenMicrowave_";
    private KitchenMicrowaveFragment.MicrowaveSetting microwaveSetting;
    private CountDownTimer counterDownTimer = null;

    private class MicrowaveSetting {
        private int id = 0;
        private int minute = 5;
        private int second = 59;

        public int getMinute() {
            return minute;
        }

        public void minute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public MicrowaveSetting()
        {

        }
        public MicrowaveSetting(int id, int minute, int second)
        {   this.id = id;
            this.minute = minute;
            this.second = second;
        }
        public int getId()
        {
            return this.id;
        }
    }





    public KitchenMicrowaveFragment() {
        // Required empty public constructor
    }

    public KitchenMicrowaveFragment(KitchenMain km){
        this.km = km;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frgRootView = inflater.inflate(R.layout.fragment_kitchen_microwave, container, false);

        final NumberPicker np1 = (NumberPicker) frgRootView.findViewById(R.id.numMinutes);
        final NumberPicker np2= (NumberPicker) frgRootView.findViewById(R.id.numSeconds);
        initNumberPicker(np1,30);
        initNumberPicker(np2,59);

        final Button btnStart = (Button) frgRootView.findViewById(R.id.btnMicrowaveStart);
        final Button btnReset = (Button) frgRootView.findViewById(R.id.btnMicrowaveReset);
        final Button btnStop = (Button) frgRootView.findViewById(R.id.btnMicrowaveStop);

        btnStop.setEnabled(false);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int startMin = np1.getValue();
                int startSec = np2.getValue();

                btnStart.setEnabled(false);
                btnReset.setEnabled(false);
                btnStop.setEnabled(true);

                long futureSec = startMin*60*1000 + startSec * 1000 + 1000;

                counterDownTimer = new CountDownTimer(futureSec, 1000) {

                    public void onTick(long millisUntilFinished) {

                        int curSecVal = np2.getValue();
                        int curMinVal = np1.getValue();
                        int nextSecVal = curSecVal;
                        int nextMinVal = curMinVal;
                        if (curSecVal == 0) {
                            nextSecVal = 59;
                            if (curMinVal == 0)
                            {
                                nextMinVal = 0;
                                nextSecVal = 0;
                            }
                            else
                            {
                                nextMinVal = curMinVal -1;
                            }
                        }
                        else {
                            nextSecVal = curSecVal - 1;
                        }


                        np2.setValue(nextSecVal);
                        np1.setValue(nextMinVal);



                    }

                    public void onFinish() {

                        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(500);

                        btnStart.setEnabled(true);
                        btnStart.setText("Start");
                        btnReset.setEnabled(true);
                        btnStop.setEnabled(false);


                    }
                };

                counterDownTimer.start();


            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setEnabled(true);
                btnReset.setEnabled(true);
                counterDownTimer.cancel();

                int curSecVal = np2.getValue();
                int curMinVal = np1.getValue();

                if (curMinVal> 0 || curSecVal > 0)
                    btnStart.setText("Resume");

            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setEnabled(true);
                btnStart.setText("Start");
                btnStop.setEnabled(false);

                np1.setValue(0);
                np2.setValue(0);
                if (counterDownTimer != null) {
                    counterDownTimer.cancel();
                    counterDownTimer = null;
                }


            }
        });

       //Spinner spnMinute = (Spinner) frgRootView.findViewById(R.id.spnMinute);
       //Spinner spnSecond = (Spinner) frgRootView.findViewById(R.id.spnSecond);

      //initializeSpinnerControls(spnMinute, spnSecond);
//        loadValuesFromDb();
//        setSpinnerValues(spnFridge,spnFreezer);
//
//        attachSpinnerEventHandlers(spnFridge, spnFreezer);
//
//        handleDeleteAppliance();


        return frgRootView;

    }

    public void initNumberPicker(NumberPicker np, int value)
    {
        np.setMaxValue(value);
        np.setMinValue(0);
        np.setValue(0);

        //http://stackoverflow.com/questions/275711/add-leading-zeroes-to-number-in-java
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

    }

    private void initializeSpinnerControls(Spinner spnMinute, Spinner spnSecond) {
//        Integer[] fridgeItems = new Integer[]{1,2,3,4,5,6,7};
//       ArrayAdapter<Integer> fridgeAdapter = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, fridgeItems);
//        spnFridge.setAdapter(fridgeAdapter);
//
//        Integer[] freezerItems = new Integer[]{-20,-19,-18,-17,-16,-15,-14};
//       ArrayAdapter<Integer> freezerAdapter = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, freezerItems);
//       spnFreezer.setAdapter(freezerAdapter);

    }


}
