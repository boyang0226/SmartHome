package com.example.bo.smarthome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AutoDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION_NUM = 1;
    public static final String DATABASE_NAME = "AutoUnits";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "UnitName";
    public static final String KEY_GPS_ENTRY = "GPSEntry";
    public static final String KEY_TEMPERATURE = "Temperature";
    public static final String KEY_NORMAL_SWITCH = "NormalSwitch";
    public static final String KEY_HEAD_SWITCH = "HeadSwitch";
    public static final String KEY_INSIDE_BRIGHTNESS = "InsideBrightness";
    public static final String KEY_RADIO_VOLUME = "RadioVolume";
    public static final String KEY_RADIO_MUTE = "RadioMute";
    public static final String KEY_RADIO_STATION_ONE = "RadioStationOne";
    public static final String KEY_RADIO_STATION_TWO = "RadioStationTwo";
    public static final String KEY_RADIO_STATION_THREE = "RadioStationThree";
    public static final String KEY_RADIO_STATION_FOUR = "RadioStationFour";
    public static final String KEY_RADIO_STATION_FIVE = "RadioStationFive";
    public static final String KEY_RADIO_STATION_SIX = "RadioStationSix";

    public AutoDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db) //only called if not yet created
    {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_GPS_ENTRY + " TEXT, " + KEY_NAME + " TEXT, "
         + KEY_TEMPERATURE + " TEXT, " + KEY_NORMAL_SWITCH + " TEXT, " + KEY_HEAD_SWITCH + " TEXT, " + KEY_INSIDE_BRIGHTNESS + " TEXT, "
         + KEY_RADIO_VOLUME + " TEXT, " + KEY_RADIO_MUTE + " TEXT, " + KEY_RADIO_STATION_ONE + " TEXT, "
         + KEY_RADIO_STATION_TWO + " TEXT, " + KEY_RADIO_STATION_THREE + " TEXT, " + KEY_RADIO_STATION_FOUR + " TEXT, "
         + KEY_RADIO_STATION_FIVE + " TEXT, " + KEY_RADIO_STATION_SIX + " TEXT);");

        Cursor results = db.query(false, AutoDatabaseHelper.DATABASE_NAME,
                new String[] {},
                null, null, null, null, null, null);
//        int rows = results.getCount() ; //number of rows returned
//        if (rows == 0) {
            ContentValues values = new ContentValues();
            values.put(AutoDatabaseHelper.KEY_GPS_ENTRY, "city+hall, ottawa, on");
            values.put(AutoDatabaseHelper.KEY_TEMPERATURE, "20");
            values.put(AutoDatabaseHelper.KEY_NORMAL_SWITCH, "0");
            values.put(AutoDatabaseHelper.KEY_HEAD_SWITCH, "0");
            values.put(AutoDatabaseHelper.KEY_INSIDE_BRIGHTNESS, "20");
            values.put(AutoDatabaseHelper.KEY_RADIO_VOLUME, "20");
            values.put(AutoDatabaseHelper.KEY_RADIO_MUTE, "0");
            values.put(AutoDatabaseHelper.KEY_RADIO_STATION_ONE, "76");
            values.put(AutoDatabaseHelper.KEY_RADIO_STATION_TWO, "45");
            values.put(AutoDatabaseHelper.KEY_RADIO_STATION_THREE, "36");
            values.put(AutoDatabaseHelper.KEY_RADIO_STATION_FOUR, "45");
            values.put(AutoDatabaseHelper.KEY_RADIO_STATION_FIVE, "66");
            values.put(AutoDatabaseHelper.KEY_RADIO_STATION_SIX, "77");
            db.insert(AutoDatabaseHelper.DATABASE_NAME, null, values);
//        }
        Log.i("AutoDatabaseHelper", "Calling onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);

        Log.i("AutoDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
        Log.i("AutoDatabaseHelper", "Calling onDowngrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }
}