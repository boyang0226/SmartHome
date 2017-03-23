package com.example.bo.smarthome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Open on 3/21/2017.
 */

public class LivingroomDatabaseHelper  extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "MyDatabase";
    private static final int DATABASE_VERSION = 2;
    public static final String KEY_ID = "_ID";
    public static final String KEY_MESSAGE = "Message";

    public LivingroomDatabaseHelper(Context ctx) {
        super(ctx, TABLE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, "+
                KEY_MESSAGE+ " VARCHAR(256));");
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
