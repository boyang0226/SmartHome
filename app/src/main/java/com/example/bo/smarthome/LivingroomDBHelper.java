package com.example.bo.smarthome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Open on 3/21/2017.
 */

public class LivingroomDBHelper  extends SQLiteOpenHelper {
    public static final String TABLENAME = "LivingRoomDevice";
    private static final int DATABASE_VERSION = 2;
    public static final String KEY_ID = "_ID";
    public static final String KEY_Name = "DeviceName";
    public static final String KEY_Switch = "Switch";
    public static final String KEY_Brightness = "Brightness";
    public static final String KEY_Color = "Color";
    public static final String Key_Volume = "Volume";
    public static final String KEY_Channel = "Channel";
    public static final String KEY_Height = "Height";
    public static final String KEY_Frequency = "Frequency";
    public static final String KEY_Type = "Type";

    public LivingroomDBHelper(Context ctx) {
        super(ctx, TABLENAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLENAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, "+
                KEY_Name+ " VARCHAR(256), " + KEY_Switch + " BOOLEAN DEFAULT 0," +
                KEY_Brightness + " INT DEFAULT 0," + KEY_Color + " VARCHAR(10)," +
                Key_Volume + " INT DEFAULT 0," + KEY_Channel + " INT DEFAULT 1," +
                KEY_Height + " INT DEFAULT 0," + KEY_Type + " VARCHAR(20)," +
                KEY_Frequency + " INT DEFAULT 0);");


        Log.i("LivingroomDBHelper", "Calling onCreate");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
        Log.i("LivingroomDBHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
        Log.i("LivingroomDBHelper", "Calling onDowngrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }


}
