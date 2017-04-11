package com.example.bo.smarthome;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Bo on 2017-03-27.
 *
 *  This class is the database for the HouseSetting part
 */

public class HouseDatabaseHelper extends SQLiteOpenHelper {

    //column information inside the the database

    public static final int VERSION_NUM = 3;
    public static final String DATABASE_NAME = "HouseDatabase";
    public static final String KEY_ID = "_id";
    public static final String KEY_DoorSwitch="DoorSwitch";
    public static final String KEY_LightSwitch="LightSwitch";
    public static final String KEY_Time="Time";
    public static final String Key_Temp ="Temp";




    public HouseDatabaseHelper(Context ctx) {

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    public void onCreate(SQLiteDatabase db)
    //only called if not yet created
    {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DoorSwitch + " BOOLEAN DEFAULT 0, "
                + KEY_LightSwitch+ " BOOLEAN DEFAULT 0, "
                + KEY_Time + " TEXT, "
                + Key_Temp + " TEXT); ");



        ContentValues values = new ContentValues();
        values.put(HouseDatabaseHelper.KEY_DoorSwitch, true);
        values.put(HouseDatabaseHelper.KEY_DoorSwitch, true);
        values.put(HouseDatabaseHelper.KEY_Time, "9:00");
        values.put(HouseDatabaseHelper.Key_Temp, "32");

    db.insert(HouseDatabaseHelper.DATABASE_NAME, null, values);

 }



    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);

        Log.i("HouseDatabasehelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
        Log.i("HouseDatabasehelper", "Calling onDowngrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }


}


