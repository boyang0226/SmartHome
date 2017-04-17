package com.example.bo.smarthome;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * This class shows the main page of the Kitchen.
 * Assignment: Project SmartHome
 * Professor: Eric Torunski
 * author: Qiuju Zhu
 */
/*Android - configure Spinner to use array [Webpage] Retrieved from:
 *http://stackoverflow.com/questions/1587028/android-configure-spinner-to-use-array
 */
/*Android AlertDialog Builder [Webpage]. Retrieved from:
 *http://stackoverflow.com/questions/13675822/android-alertdialog-builder
 */
public class KitchenMain extends KitchenBase {

    protected static final String ACTIVITY_NAME = "KitchenMain_";
    protected static final String TOOLBAR_AREA = "Toolbar";
    protected static final String ONCREATE = "onCreate";
    protected static final String ADDAPPLIANCEBTN = "AddApplianceButton";
    public static final String KITCHEN_MICROWAVE = "MICROWAVE";
    public static final String KITCHEN_FRIDGE = "FRIDGE";
    public static final String KITCHEN_LIGHT = "LIGHT";
    SQLiteDatabase db;
    ArrayList<KitchenAppliance> applianceList = new ArrayList<>();
    Cursor results;
    KitchenListAdapter listAdapter;
    boolean isLandscape = false;
    KitchenFragmentBase kfb = null;
    ArrayList<KitchenApplianceType> applianceTypeList = new ArrayList<>();

    /**
     * Data source for dropdown box
     */
    private class KitchenApplianceType {
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private int id;
        //getter
        public String getApplianceType() {
            return applianceType;
        }
        //setter
        public void setApplianceType(String applianceType) {
            this.applianceType = applianceType;
        }
        //getter
        public String getApplianceTypeDescription() {
            return applianceTypeDescription;
        }
        //setter
        public void setApplianceTypeDescription(String applianceTypeDescription) {
            this.applianceTypeDescription = applianceTypeDescription;
        }

        private String applianceType;
        private String applianceTypeDescription;
        //Constructor
        public KitchenApplianceType(int id, String applianceType, String applianceTypeDescription) {
            this.id = id;
            this.applianceType = applianceType;
            this.applianceTypeDescription = applianceTypeDescription;
        }
        //make a meaningful string
        public String toString()
        {
            return( getApplianceTypeDescription() );
        }
    }
    //Listview data source
    private class KitchenAppliance {
        private int id = 0;
        //getter
        public String getApplianceType() {
            return applianceType;
        }
         //setter
        public void setApplianceType(String applianceType) {
            this.applianceType = applianceType;
        }

        private String applianceType = "";
        //getter
        public String getApplianceName() {
            return applianceName;
        }
        //setter
        public void setApplianceName(String applianceName) {
            this.applianceName = applianceName;
        }
        //getter
        public String getApplianceSetting() {
            return applianceSetting;
        }
        //setter
        public void setApplianceSetting(String applianceSetting) {
            this.applianceSetting = applianceSetting;
        }

        private String applianceName = "";
        private String applianceSetting = "";

        //constructor
        public KitchenAppliance()
        {

        }
        //setter
        public void setId(int id) {
            this.id = id;
        }
        //set the kitchen appliances
        public KitchenAppliance(int id, String applianceType, String applianceName, String applianceSetting)
        {   this.id = id;
            this.applianceType = applianceType;
            this.applianceName = applianceName;
            this.applianceSetting = applianceSetting;
        }
        //getter
        public int getId()
        {
            return id;
        }
    }
    //Populate list
    public class KitchenListAdapter extends ArrayAdapter<KitchenAppliance> {

        private int selectedItem = -1;
        public KitchenListAdapter(Context ctx){
            super(ctx,0);
        }
        //getter
        public long getItemId(int position)
        {
            //results.moveToPosition(position);
            //return results.getLong( results.getColumnIndex(KitchenDatabaseHelper.KEY_ID) );
            return applianceList.get(position).getId();
        }
         //get the list item size
        @Override
        public int getCount() {
            return applianceList.size();
        }

        //get list item position
        @Nullable
        @Override
        public KitchenAppliance getItem(int position) {
            return applianceList.get(position);
        }
        //get list item view
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = KitchenMain.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.kitchen_main_listview_row,null);
            TextView message = (TextView)result.findViewById(R.id.txtKitchenMainListViewRow);
            KitchenAppliance ka = getItem(position);
            String listItemText;
            if (position==selectedItem)
                 listItemText = ka.getApplianceName() + " - " + ka.getApplianceType() + "(*)";
            else
                 listItemText = ka.getApplianceName() + " - " + ka.getApplianceType();

            message.setText(listItemText);
            return result;
        }
        //set selected item of the list
        public void setSelectedItem(int selecteItem)
        {
            this.selectedItem = selecteItem;
        }
    }

    /**
     * AsyncTask
     */
    public class KitchenApplianceQuery extends AsyncTask<String, Integer, String> {
        //run small group in background
        @Override
        protected String doInBackground(String ... params) {


            results = db.query(false, KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME,
                    new String[]{KitchenDatabaseHelper.KEY_ID,
                                KitchenDatabaseHelper.KEY_APPLIANCE_TYPE,
                                KitchenDatabaseHelper.KEY_APPLIANCE_NAME,
                                KitchenDatabaseHelper.KEY_APPLIANCE_SETTING,},null, null, null, null, null, null);
            int rows = results.getCount();
            int progressStep = 100/rows-1;
            int progress = 0;
            results.moveToFirst();
            publishProgress(progress);
            while(!results.isAfterLast()) {

                KitchenAppliance ka = new KitchenAppliance( results.getInt(results.getColumnIndex(KitchenDatabaseHelper.KEY_ID)),
                        results.getString(results.getColumnIndex(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE)),
                        results.getString(results.getColumnIndex(KitchenDatabaseHelper.KEY_APPLIANCE_NAME)),
                        results.getString(results.getColumnIndex(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING)));
                applianceList.add(ka);
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( KitchenDatabaseHelper.KEY_APPLIANCE_NAME) ) );
                results.moveToNext();
                progress += progressStep;
                publishProgress(progress);


                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

             return "";
        }

        /**
         *  ProgressUpdate.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar pb = (ProgressBar) findViewById(R.id.prgKitchenApplianceBar);
            if (pb != null) {
                pb.setProgress(values[0]);
                pb.setVisibility(View.VISIBLE);
            }
            listAdapter.notifyDataSetChanged();
        }
        //postExecute.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgressBar pb = (ProgressBar) findViewById(R.id.prgKitchenApplianceBar);
            if (pb != null) {
                pb.setVisibility(View.INVISIBLE);
            }
            //listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Show the Help message.
     */
    @Override
    protected void showHelp()
    {
        android.app.AlertDialog.Builder kitchenbase_builder = new android.app.AlertDialog.Builder(KitchenMain.this);
        kitchenbase_builder.setTitle(R.string.kitchen_toobar_welcome_text)
                .setMessage(R.string.kitchen_toolbar_instructon_text)
                .setNegativeButton(R.string.kitchen_toolbar_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("No", "No");
                    }
                });
        kitchenbase_builder.create().show();
    }

    /**
     * Create the layout.
     * @param savedInstanceState Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String logTag = ACTIVITY_NAME + ONCREATE;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_main);
        //set the toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarkitchenmain);
        setToolbarColor(toolbar);
        setSupportActionBar(toolbar);

        //add appliance category
        String microwave = getString(R.string.kitchen_microwave);
        String refrigerator = getString(R.string.kitchen_refrigerator);
        String light = getString(R.string.kitchen_light);
        applianceTypeList.add( new KitchenApplianceType(1,"MICROWAVE",microwave));
        applianceTypeList.add( new KitchenApplianceType(2,"FRIDGE",refrigerator));
        applianceTypeList.add( new KitchenApplianceType(3,"LIGHT",light));

        isLandscape = IsLandscapeLayout();

        ProgressBar pb = (ProgressBar) findViewById(R.id.prgKitchenApplianceBar);
        pb.setProgressTintList(ColorStateList.valueOf(Color.RED));

        populateListView(logTag);
        //open datebase
        KitchenDatabaseHelper dbHelper = new KitchenDatabaseHelper(KitchenMain.this);
        db = dbHelper.getWritableDatabase();

        new KitchenApplianceQuery().execute("");
        //Handle list view item select.
        handleListViewItemClick(logTag);
        //Add button handle. handle add appliance button
        addAppliance(logTag);
    }

    /**
     * Show activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        applianceList.clear();
        new KitchenApplianceQuery().execute("");

    }
    // Landscape layout.
    private boolean IsLandscapeLayout()
    {
        FrameLayout f = (FrameLayout) findViewById(R.id.frmKitchenDetail);
        return (f != null);
    }

    /**
     * Choose an activity
     * @param logTag log identifier
     */
    private void handleListViewItemClick(String logTag) {

        final ListView applianceListView = (ListView) findViewById(R.id.lvKitchenAppliance);
        if (applianceListView != null)
            applianceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String tag = ACTIVITY_NAME + "ListView Clicked.";
                //Bundle bun = new Bundle();
                //bun.putLong("ID", id );
                //String msg = chatList.getItemAtPosition(position).toString();
                //bun.putString("Msg", msg );

                //String selectedAppliance = ((TextView) view).getText().toString();
                KitchenAppliance selectedAppliance = listAdapter.getItem(position);

                String selectedApplianceName = selectedAppliance.getApplianceName();
                String selectedApplianceType = selectedAppliance.getApplianceType();
                int applianceId = selectedAppliance.getId();

                // data for fragment or next activity
                Bundle bun = new Bundle();
                bun.putInt("applianceId", applianceId );
                bun.putString("applianceName", selectedApplianceName);

                KitchenFragmentBase kitchenFragment;
                switch (selectedApplianceType) {
                    case "MICROWAVE":
                        Log.d(tag, "Clicked Microwave.");
                        kitchenFragment = new KitchenMicrowaveFragment(KitchenMain.this);
                        CallFragmentOrActivity(kitchenFragment,KitchenMicrowaveDetail.class,bun);
                        break;
                    case "FRIDGE":
                        Log.d(tag, "Clicked Fridge.");
                        kitchenFragment = new KitchenFridgeFragment(KitchenMain.this);
                        CallFragmentOrActivity(kitchenFragment,KitchenFridgeDetail.class,bun);
                        break;
                    case "LIGHT":
                        Log.d(tag, "Clicked on LIGHT device.");
                        kitchenFragment = new KitchenLightFragment(KitchenMain.this);
                        CallFragmentOrActivity(kitchenFragment,KitchenLightDetail.class,bun);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * Handle replace/add situation in fragment.
     * @param f kitchenFragementBase object
     * @param cls view object
     * @param bun Bundle object
     */
    private void CallFragmentOrActivity(KitchenFragmentBase f, Class<?> cls, Bundle bun)
    {
        if (isLandscape)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            f.setArguments(bun);
            if (kfb != null) {
                ft.replace(R.id.frmKitchenDetail, f);
                kfb = f;
            }
            else {
                ft.add(R.id.frmKitchenDetail, f);
                kfb = f;
            }
            ft.commit();
        }
        else {
            callActivityWithData(cls, bun);
        }
    }

    /**
     * Display the list view.
     * @param logTag log identifier
     */
    private void populateListView(String logTag) {

        Log.d(logTag, "Populating appliance list view.");

        ListView kitchenListview = (ListView) findViewById(R.id.lvKitchenAppliance);

        listAdapter = new KitchenListAdapter(this);
        if (kitchenListview != null)
                kitchenListview.setAdapter(listAdapter);
    }

    /**
     * This method is for add an appliance
     * @param logTag log identifier
     */
    private void addAppliance(String logTag) {

        Log.d(logTag, "Add appliance button event handler.");

        Button btnAddAppliance = (Button) findViewById(R.id.btnKitchenMainAdd);
        if (btnAddAppliance != null)
        btnAddAppliance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String logTag1 = ADDAPPLIANCEBTN;
                Log.d(logTag1, "Add Appliance button clicked.");

                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(KitchenMain.this);
                LayoutInflater inflater = KitchenMain.this.getLayoutInflater();

                final View dlgView = inflater.inflate(R.layout.kitchen_main_dialog_custom, null);

                Spinner spinner = (Spinner)dlgView.findViewById(R.id.spnKitchenAddAppliance);

                //Create and fill an ArrayAdapter with a bunch of "State" objects
                ArrayAdapter<KitchenApplianceType> spinnerArrayAdapter = new ArrayAdapter(KitchenMain.this,
                        android.R.layout.simple_spinner_item, applianceTypeList.toArray());

                //Tell the spinner about our adapter
                spinner.setAdapter(spinnerArrayAdapter);

                dlgBuilder.setView(dlgView)
                        .setPositiveButton(R.string.dialog_positive_text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Spinner spnAppliance = (Spinner) dlgView.findViewById(R.id.spnKitchenAddAppliance);
                                KitchenApplianceType newAppliancetype = (KitchenApplianceType)spnAppliance.getSelectedItem();
                                EditText applianceName = (EditText) dlgView.findViewById(R.id.edtApplianceName);

                                if (applianceName.getText().toString() != "")
                                {

                                    KitchenAppliance ka = new KitchenAppliance(0,newAppliancetype.getApplianceType(),
                                            applianceName.getText().toString(), "");

                                    ContentValues applianceValues = new ContentValues();
                                    applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, ka.getApplianceType() );
                                    applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, ka.getApplianceName() );
                                    applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, ka.getApplianceSetting() );
                                    Long newId = db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", applianceValues);
                                    ka.setId(Integer.parseInt(newId.toString()));
                                    if (newId > 0) {
                                        ContentValues values = new ContentValues();
                                        if (ka.getApplianceType() == KITCHEN_LIGHT) {
                                            values.put(KitchenDatabaseHelper.KEY_ID, newId);
                                            values.put(KitchenDatabaseHelper.KEY_MAINSWITCH, 0);
                                            values.put(KitchenDatabaseHelper.KEY_DIMMER_LEVEL, 60);
                                            db.insert(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, "", values);
                                        } else if (ka.getApplianceType() == KITCHEN_FRIDGE){
                                            values.put(KitchenDatabaseHelper.KEY_ID, newId);
                                            values.put(KitchenDatabaseHelper.KEY_FRIDGE_SETTING, 5);
                                            values.put(KitchenDatabaseHelper.KEY_FREEZER_SETTING, -20);
                                            db.insert(KitchenDatabaseHelper.KITCHEN_FRIDGE_TABLE_NAME, "", values);
                                        } else if (ka.getApplianceType() == KITCHEN_MICROWAVE) {
                                            values.put(KitchenDatabaseHelper.KEY_ID, newId);
                                            values.put(KitchenDatabaseHelper.KEY_MICROWAVE_MINUTE, 0);
                                            values.put(KitchenDatabaseHelper.KEY_MICROWAVE_SECOND, 0);
                                            values.put(KitchenDatabaseHelper.KEY_MICROWAVE_STATE, "RESET");
                                            db.insert(KitchenDatabaseHelper.KITCHEN_MICROWAVE_TABLE_NAME, "", values);
                                        }
                                        applianceList.add(ka);
                                        listAdapter.notifyDataSetChanged();
                                        //Handle landscape view
                                        if (isLandscape) {
                                            selectListViewItem(applianceList.size() - 1);
                                            listAdapter.setSelectedItem(applianceList.size() - 1);
                                        }
                                    }
                                } else {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KitchenMain.this);
                                        //Chain together various setter methods to set the dialog characteristics
                                            builder.setMessage(R.string.kitchen_main_add_appliance_dialog_warning_msg) //Add a dialog message to strings.xml
                                            .setTitle(R.string.kitchen_main_add_appliance_dialog_title)
                                            .setPositiveButton(R.string.dialog_positive_text_ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            })
                                            .show();
                                }

                            }
                        }).setNegativeButton(R.string.dialog_negative_text_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) { }
                });

                AlertDialog dlgAddAppliance = dlgBuilder.create();
                dlgAddAppliance.show();
            }
        });
    }

    /**
     * Select an appliance.
     * @param position item position
     */
    private void selectListViewItem(int position)
    {
        ListView kitchenListview = (ListView) findViewById(R.id.lvKitchenAppliance);
        kitchenListview.performItemClick(
                kitchenListview.getAdapter().getView(position, null, null),
                position,
                kitchenListview.getAdapter().getItemId(position));
    }

    /**
     * Remove fragement.
     */
    public void removeFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(kfb);
        ft.commit();
        applianceList.clear();
        new KitchenApplianceQuery().execute("");
    }

}
