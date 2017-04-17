package com.example.bo.smarthome;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * This class creates the light fragment.
 * Assignment: Project SmartHome
 * Professor: Eric Torunski
 * author: Qiuju Zhu
 */

 /*How to set selected item of Spinner by value, not by position? [Webpage]. Retrieved from:
  *http://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position
  */
 /*Android SQLite: Update Statement [Webpage]. Retrieved from:
  *http://stackoverflow.com/questions/5987863/android-sqlite-update-statement
  */
 /*Android AlertDialog Builder [Webpage]. Retrieved from:
  *http://stackoverflow.com/questions/13675822/android-alertdialog-builder
  */
 /*REFRIGERATOR AND FREEZER TEMPERATURE SETTINGS [Webpage]. Retrieved from:
  *http://www.subzero-wolf.com/assistance/answers/refrigerator-and-freezer-temperature-settings
  */

 //A simple {@link Fragment} subclass.
public class KitchenFridgeFragment extends KitchenFragmentBase {

    KitchenMain km;

    private static final String ACTIVITY_NAME = "KitchenFridge_";
    private KitchenFridgeFragment.FridgeSetting fridgeSetting;
    //define fridge ui setting
    private class FridgeSetting {
        private int id = 0;
        private int fridgeTemp = 5;

        public int getFridgeTemp() {
            return fridgeTemp;
        }

        public void setFridgeTemp(int fridgeTemp) {
            this.fridgeTemp = fridgeTemp;
        }

        public int getFreezerTemp() {
            return freezerTemp;
        }

        public void setFreezerTemp(int freezerTemp) {
            this.freezerTemp = freezerTemp;
        }
        //default vaue for freezer
        private int freezerTemp = -20;

        public FridgeSetting()
        {

        }
        public FridgeSetting(int id, int fridgeTemp, int freezerTemp)
        {   this.id = id;
            this.fridgeTemp = fridgeTemp;
            this.freezerTemp = freezerTemp;
        }
        public int getId()
        {
            return this.id;
        }

    }

    /**
     * Default constructor
     */
    public KitchenFridgeFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of KitchenFridgeFragment.
     * @param km KitchenMain object
     */
    public KitchenFridgeFragment(KitchenMain km){
        this.km = km;
    }

    /**
     * Inflate the UI fragment
     * @param inflater correspond layout(xml) file
     * @param container ViewGroup object
     * @param savedInstanceState reference to a Bundle object
     * @return frgRootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        frgRootView = inflater.inflate(R.layout.fragment_kitchen_fridge, container, false);

        Spinner spnFridge = (Spinner) frgRootView.findViewById(R.id.spnFridgeTemp);
        Spinner spnFreezer = (Spinner) frgRootView.findViewById(R.id.spnFreezerTemp);

        initializeSpinnerControls(spnFridge, spnFreezer);
        initializeDB();
        loadValuesFromDb();
        setSpinnerValues(spnFridge,spnFreezer);
        attachSpinnerEventHandlers(spnFridge, spnFreezer);
        handleDeleteAppliance();
        return frgRootView;

    }
    /**
     * Spinner handler
     * @param spnFridge fridge spinner
     * @param spnFreezer freezer spinner
     */
    private void attachSpinnerEventHandlers(Spinner spnFridge, Spinner spnFreezer) {

        spnFridge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int val = Integer.parseInt(parentView.getSelectedItem().toString());

//                CharSequence text = null;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(getContext(), parentView.getSelectedItem().toString() , duration);
//                toast.show();
                fridgeSetting.setFridgeTemp(val);
                saveFridgeSetting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
            spnFreezer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int val = Integer.parseInt(parentView.getSelectedItem().toString());
//                CharSequence text = null;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(getContext(), parentView.getSelectedItem().toString() , duration);
//                toast.show();

                fridgeSetting.setFreezerTemp(val);
                saveFridgeSetting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    /**
     * Define the range of fridge temperature and freezer temperature.
     * @param spnFridge fridge spinner
     * @param spnFreezer freezer spinner
     */
    private void initializeSpinnerControls(Spinner spnFridge, Spinner spnFreezer)
    {
        Integer[] fridgeItems = new Integer[]{1,2,3,4,5,6,7};
        ArrayAdapter<Integer> fridgeAdapter = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, fridgeItems);
        spnFridge.setAdapter(fridgeAdapter);

        Integer[] freezerItems = new Integer[]{-20,-19,-18,-17,-16,-15,-14};
        ArrayAdapter<Integer> freezerAdapter = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item, freezerItems);
        spnFreezer.setAdapter(freezerAdapter);

    }

    /**
     * Set the spinner values.
     * @param spnFridge fridge spinner
     * @param spnFreezer freezer spinner
     */
    private void setSpinnerValues(Spinner spnFridge, Spinner spnFreezer) {

        ArrayAdapter<Integer> fridgeAdapter = (ArrayAdapter<Integer>)spnFridge.getAdapter();
        int position = fridgeAdapter.getPosition(fridgeSetting.getFridgeTemp());
        spnFridge.setSelection(position);

        ArrayAdapter<Integer> freezerAdapter = (ArrayAdapter<Integer>)spnFreezer.getAdapter();
        position = freezerAdapter.getPosition(fridgeSetting.getFreezerTemp());
        spnFreezer.setSelection(position);

    }

    /**
     * Load the values from the database.
     */
    private void loadValuesFromDb() {
        String methodName = "ReadSetting";
        fridgeSetting = new KitchenFridgeFragment.FridgeSetting();
        Cursor results = db.query(false, KitchenDatabaseHelper.KITCHEN_FRIDGE_TABLE_NAME,
                new String[]{KitchenDatabaseHelper.KEY_ID, KitchenDatabaseHelper.KEY_FRIDGE_SETTING, KitchenDatabaseHelper.KEY_FREEZER_SETTING}, KitchenDatabaseHelper.KEY_ID +"=" + String.valueOf(applianceId), null, null, null, null, null);

        int rows = results.getCount();
        if (rows > 0) {
            results.moveToFirst();
            Log.i(ACTIVITY_NAME + methodName, "SQL MESSAGE:" + "Read record no. "+ results.getInt( results.getColumnIndex( KitchenDatabaseHelper.KEY_ID) ) );

            int tempId = applianceId;
            int tempFridgeTemp = results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_FRIDGE_SETTING));
            int tempFreezerTemp = results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_FREEZER_SETTING));
            fridgeSetting = new KitchenFridgeFragment.FridgeSetting(tempId, tempFridgeTemp, tempFreezerTemp);
        }
    }

    /**
     * Save the settings of the refrigerator.
     */
    private void saveFridgeSetting() {

        ContentValues values = new ContentValues();
        values.put(KitchenDatabaseHelper.KEY_FRIDGE_SETTING, fridgeSetting.getFridgeTemp());
        values.put(KitchenDatabaseHelper.KEY_FREEZER_SETTING, fridgeSetting.getFreezerTemp());


        if (fridgeSetting.getId() == 0)
        {
            db.insert(KitchenDatabaseHelper.KITCHEN_FRIDGE_TABLE_NAME, "", values);
        }
        else
        {
            db.update(KitchenDatabaseHelper.KITCHEN_FRIDGE_TABLE_NAME, values, KitchenDatabaseHelper.KEY_ID + "=" + fridgeSetting.getId(),null );

        }
    }

    /**
     * Handle the delete button when deleting a fridge item.
     */
    private void handleDeleteAppliance() {
        Button btnDeleteAppliance = (Button) frgRootView.findViewById(R.id.btnDeleteKitchenFridge);
        btnDeleteAppliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = getString(R.string.kitchen_light_delete_appliance_dialog_message)  + " " + applianceName + "?";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //Chain together various setter methods to set the dialog characteristics
                builder.setMessage(msg)
                        .setTitle(R.string.kitchen_fridge_delete_appliance_dialog_title)
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
     * Handle delete the records in database.
     */
    private void deleteDatabaseRecords() {
        db.delete(KitchenDatabaseHelper.KITCHEN_FRIDGE_TABLE_NAME, "id="+applianceId , null);
        db.delete(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "id="+applianceId , null);
    }
}
