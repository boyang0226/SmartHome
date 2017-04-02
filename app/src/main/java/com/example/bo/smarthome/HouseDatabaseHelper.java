package com.example.bo.smarthome;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Bo on 2017-03-27.
 */

public class HouseDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION_NUM = 3;
    public static final String databaseName = "HouseDatabase";
    public static final String KEY_ID = "_id";

    public static final String KEY_DoorStatus="DoorStatus";
    public static final String KEY_LightStatus=  "LightStatus";
    public static final String KEY_Time="Time";
    public static final String Key_Temp ="Temp";



    public HouseDatabaseHelper(Context ctx) {
         super(ctx, databaseName, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db) //only called if not yet created
    {
        db.execSQL("CREATE TABLE " + databaseName + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DoorStatus + " text,  "
                + KEY_LightStatus + " text, "
                + KEY_Time + "  text,  "
                + Key_Temp + "  text); "


        );

        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + databaseName);
        onCreate(db);

        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }




}


