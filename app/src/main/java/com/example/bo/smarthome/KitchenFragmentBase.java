package com.example.bo.smarthome;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class creates the light fragment.
 * Assignment: Project SmartHome
 * Professor: Eric Torunski
 * author: Qiuju Zhu
 * A simple {@link Fragment} subclass.
 */
public class KitchenFragmentBase extends Fragment {

    protected int applianceId = 0;
    protected String applianceName = "";
    protected  View frgRootView;
    protected SQLiteDatabase db = null;

    /**
     * Default constructor
     */
    public KitchenFragmentBase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bun = getArguments();
        applianceId = bun.getInt("applianceId");
        applianceName = bun.getString("applianceName");
        kitchenFragmentOnCreate();
    }

    protected void kitchenFragmentOnCreate() {

    }

    /**
     *set the fragment layout
     * @param inflater correspond layout(xml) file
     * @param container ViewGroup object
     * @param savedInstanceState reference to a Bundle object
     * @return null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }
    @Override
    public void onPause() {
        super.onPause();
        kitchenFragmentOnPause();
    }
    //Save setting to database when pause.
    protected void kitchenFragmentOnPause() {
        saveSettingsToDB();
    }
    //Save setting to database
    protected void saveSettingsToDB() {

    }

    /**
     * Initialize DB instance
     */
    protected void initializeDB()
    {
        KitchenDatabaseHelper dbHelper = new KitchenDatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();
    }

}
