package com.example.bo.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class sets the main page, offer user to select different UI.
 * Assignment: Project SmartHome
 * Professor: Eric Torunski
 * author: Qiuju Zhu
 */
public class KitchenBase extends AppCompatActivity {

    /**
     * identifier for the method and the application
     */
    private String logTag = "KitchenBase";
    Context ctx;

    /**
     * Set log tag.
     * @param logTag log identifier
     */
    protected void setLogTag(String logTag)
    {
        this.logTag = logTag;
    }

    /**
     * Set the toolBar background colour.
     * @param tbar ToolBar object
     */
    protected void setToolbarColor(Toolbar tbar)
    {
        tbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorSHNavigationBar));

    }

    /**
     * Open houseSetting, Auto, LivingRoom activities
     * @param cls parent
     */
    protected void callActivity(Class<?> cls)    {

        Intent intent = new Intent(getBaseContext(), cls);
        startActivity(intent);
    }

    /**
     * Open kitchen items.
     * @param cls parent
     * @param bun Bundle object
     */
    protected void callActivityWithData(Class<?> cls, Bundle bun)    {

        Intent intnt = new Intent(getBaseContext(), cls);
        intnt.putExtras(bun);
        startActivityForResult(intnt,5);
    }
    //Show help menu on different ui.
    protected void showHelp() {
    }
    //Load the kitchen fragment
    protected void loadKitchenFragment(KitchenFragmentBase frg, int resId, Bundle bun)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        frg.setArguments(bun);
        ft.replace(resId, frg);
        ft.commit();
    }
    /**
     * Start the activity.
     * @param savedInstanceState Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create toolBar by inflating it from XML file.
     * @param m Menu object
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.kitchen_toolbar_menu, m );
        return true;
    }

    /**
     * Direct to different UI from the toolBar
     * @param mi MenuItem object
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {

        int id;
        id = mi.getItemId();

        switch(id) {
            case R.id.kitchen_car:
                Log.d(logTag, "Switch to Automobile from Kitchen.");
                callActivity(AutoListView.class);
                break;
            case R.id.kitchen_house:
                Log.d(logTag, "Switch to House Setting from Kitchen.");
                callActivity(HousesettingDetail.class);
                break;
            case R.id.kitchen_living:
                Log.d(logTag, "Switch to Living Room from Kitchen.");
                callActivity(LivingroomList.class);
                break;
             case R.id.kitchen_help:
               Log.d(logTag, "Help");
               showHelp();

              break;

            default:
                break;
        }
        return true;
    }

}
