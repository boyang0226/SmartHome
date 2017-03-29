package com.example.bo.smarthome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AutoDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION_NUM = 1;
    public static final String DATABASE_NAME = "AutoUnits";
    public static final String KEY_ID = "_id";
    public static final String KEY_TYPE = "UnitType";
    public static final String KEY_NAME = "UnitName";
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
        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TYPE + " TEXT, " + KEY_NAME + " text, "
         + KEY_TEMPERATURE + " DOUBLE DEFAULT 0, " + KEY_NORMAL_SWITCH + " BOOLEAN DEFAULT 0, " + KEY_HEAD_SWITCH + " BOOLEAN DEFAULT 0, " + KEY_INSIDE_BRIGHTNESS + " INT DEFAULT 0, "
         + KEY_RADIO_VOLUME + " INT DEFAULT 0, " + KEY_RADIO_MUTE + " BOOLEAN DEFAULT 0, " + KEY_RADIO_STATION_ONE + " DOUBLE DEFAULT 0, "
         + KEY_RADIO_STATION_TWO + " DOUBLE DEFAULT 0, " + KEY_RADIO_STATION_THREE + " DOUBLE DEFAULT 0, " + KEY_RADIO_STATION_FOUR + " DOUBLE DEFAULT 0, "
         + KEY_RADIO_STATION_FIVE + " DOUBLE DEFAULT 0, " + KEY_RADIO_STATION_SIX + " DOUBLE DEFAULT 0);");

        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);

        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

}