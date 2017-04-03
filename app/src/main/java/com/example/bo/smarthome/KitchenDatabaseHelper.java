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

    private static final int VERSION_NUM = 1;
    public static final String DATABASE_NAME = "SHKitchen.db";
    public static final String KITCHEN_LIGHT_TABLE_NAME = "tbl_light";
    public static final String KITCHEN_APPLIANCE_TABLE_NAME ="tbl_appliances";
    public static final String KITCHEN_FRIDGE_TABLE_NAME = "fridge";
    public static final String KITCHEN_MICROWAVE_TABLE_NAME = "microwave";
    public static final String KEY_ID = "id";
    public static final String KEY_MAINSWITCH = "MAIN_SWITCH";
    public static final String KEY_DIMMER_LEVEL = "DIMMER_LEVEL";
    public static final String KEY_APPLIANCE_TYPE = "Type";
    public static final String KEY_APPLIANCE_NAME = "Name";
    public static final String KEY_APPLIANCE_SETTING = "Setting";

    public KitchenDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(" CREATE TABLE " + KITCHEN_LIGHT_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MAINSWITCH + " INTEGER, " + KEY_DIMMER_LEVEL + " INTEGER);");
        db.execSQL(" CREATE TABLE " + KITCHEN_APPLIANCE_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                        + KEY_APPLIANCE_TYPE + " TEXT, "
                                                                        + KEY_APPLIANCE_NAME + " TEXT, "
                                                                        + KEY_APPLIANCE_SETTING + " TEXT);");

        ContentValues values = new ContentValues();

        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "MICROWAVE" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Panosonic Microwave" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "" );
        db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", values);

        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "FRIDGE" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Samsung Fridge" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "5" );
        db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", values);

        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "FREEZER" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Samsung Freezer" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "-20" );
        db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", values);

        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_TYPE, "LIGHT" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_NAME, "Main Ceiling light" );
        values.put(KitchenDatabaseHelper.KEY_APPLIANCE_SETTING, "60" );
        db.insert(KitchenDatabaseHelper.KITCHEN_APPLIANCE_TABLE_NAME, "", values);

        Log.i("KitchenDatabaseHelper", "Calling onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + KITCHEN_LIGHT_TABLE_NAME);
        onCreate(db);

        Log.i("KitchenDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }

}
