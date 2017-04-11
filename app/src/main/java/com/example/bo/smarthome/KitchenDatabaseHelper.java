package com.example.bo.smarthome;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by qiu on 2017-04-01.
 */

public class KitchenDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Define table name and column.
     */
    private static final int VERSION_NUM = 4;
    public static final String DATABASE_NAME = "SHKitchen.db";
    public static final String KITCHEN_LIGHT_TABLE_NAME = "tbl_light";
    public static final String KITCHEN_APPLIANCE_TABLE_NAME ="tbl_appliances";
    public static final String KITCHEN_FRIDGE_TABLE_NAME = "tbl_fridge";
    public static final String KITCHEN_MICROWAVE_TABLE_NAME = "tbl_microwave";
    public static final String KEY_ID = "id";
    public static final String KEY_MAINSWITCH = "MAIN_SWITCH";
    public static final String KEY_DIMMER_LEVEL = "DIMMER_LEVEL";
    public static final String KEY_APPLIANCE_TYPE = "Type";
    public static final String KEY_APPLIANCE_NAME = "Name";
    public static final String KEY_APPLIANCE_SETTING = "Setting";
    public static final String KEY_FRIDGE_SETTING = "FridgeSetting";
    public static final String KEY_FREEZER_SETTING = "FreezerSetting";
    public static final String KEY_MICROWAVE_MINUTE = "Minute";
    public static final String KEY_MICROWAVE_SECOND = "Second";
    public static final String KEY_MICROWAVE_STATE = "State";


    /**
     * Constructor for the kitchen database.
     * @param ctx application context
     */
    public KitchenDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Create kitchen database
     * @param db instance of SQLiteDatabase
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL(" CREATE TABLE " + KITCHEN_LIGHT_TABLE_NAME + "(" + KEY_ID + " INTEGER, " + KEY_MAINSWITCH + " INTEGER, " + KEY_DIMMER_LEVEL + " INTEGER);");
        db.execSQL(" CREATE TABLE " + KITCHEN_FRIDGE_TABLE_NAME + "(" + KEY_ID + " INTEGER, " + KEY_FRIDGE_SETTING + " INTEGER, " + KEY_FREEZER_SETTING + " INTEGER);");
        db.execSQL(" CREATE TABLE " + KITCHEN_MICROWAVE_TABLE_NAME + "(" + KEY_ID + " INTEGER, " + KEY_MICROWAVE_MINUTE + " INTEGER, " + KEY_MICROWAVE_SECOND + " INTEGER, " + KEY_MICROWAVE_STATE + " TEXT);");
        db.execSQL(" CREATE TABLE " + KITCHEN_APPLIANCE_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                        + KEY_APPLIANCE_TYPE + " TEXT, "
                                                                        + KEY_APPLIANCE_NAME + " TEXT, "
                                                                        + KEY_APPLIANCE_SETTING + " TEXT);");

        ContentValues applianceValues = new ContentValues();
        ContentValues fridgeValues = new ContentValues();
        ContentValues lightValues = new ContentValues();
        ContentValues microwaveValues = new ContentValues();
        //Microwave database
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "MICROWAVE" );
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Panosonic Microwave" );
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "" );
        long microwaveId = db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", applianceValues);
        microwaveValues.put(KitchenDatabaseHelper.KEY_ID, microwaveId);
        microwaveValues.put(KitchenDatabaseHelper.KEY_MICROWAVE_MINUTE, 0);
        microwaveValues.put(KitchenDatabaseHelper.KEY_MICROWAVE_SECOND, 0);
        microwaveValues.put(KitchenDatabaseHelper.KEY_MICROWAVE_STATE, "RESET");
        db.insert(KitchenDatabaseHelper.KITCHEN_MICROWAVE_TABLE_NAME, "", microwaveValues);
        //Fridge database
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "FRIDGE" );
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Samsung Fridge" );
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "5|-20" );
        long fridgeId = db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", applianceValues);
        fridgeValues.put(KitchenDatabaseHelper.KEY_ID, fridgeId);
        fridgeValues.put(KitchenDatabaseHelper.KEY_FRIDGE_SETTING, 5);
        fridgeValues.put(KitchenDatabaseHelper.KEY_FREEZER_SETTING, -20);
        db.insert(KitchenDatabaseHelper.KITCHEN_FRIDGE_TABLE_NAME, "", fridgeValues);
        //Light database
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "LIGHT" );
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Main Ceiling light" );
        applianceValues.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "60" );
        long lightId = db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", applianceValues);
        lightValues.put(KitchenDatabaseHelper.KEY_ID, lightId);
        lightValues.put(KitchenDatabaseHelper.KEY_MAINSWITCH, 0);
        lightValues.put(KitchenDatabaseHelper.KEY_DIMMER_LEVEL, 60);
        db.insert(KitchenDatabaseHelper.KITCHEN_LIGHT_TABLE_NAME, "", lightValues);

        Log.i("KitchenDatabaseHelper", "Calling onCreate");
    }

    /**
     * Create new database
     * @param db instance of SQLiteDatabase
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + KITCHEN_LIGHT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KITCHEN_FRIDGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KITCHEN_MICROWAVE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + KITCHEN_APPLIANCE_TABLE_NAME);
        onCreate(db);

        Log.i("KitchenDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }

}
