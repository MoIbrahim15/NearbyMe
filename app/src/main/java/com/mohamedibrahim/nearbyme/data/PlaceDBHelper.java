package com.mohamedibrahim.nearbyme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.KEY_PLACE_ADDRESS;
import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.KEY_PLACE_DISTANCE;
import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.KEY_PLACE_ID;
import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.KEY_PLACE_RATE;
import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.KEY_PLACE_TITLE;
import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.KEY_PRIMARY_ID;
import static com.mohamedibrahim.nearbyme.data.PlaceContract.PlaceEntry.TABLE_PLACES;

/**
 * Created by Mohamed Ibrahim
 * on 11/4/2016.
 **/

public class PlaceDBHelper extends SQLiteOpenHelper {

    private static PlaceDBHelper sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "placesManager";


    public static synchronized PlaceDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PlaceDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private PlaceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PLACES + "("
                + KEY_PRIMARY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PLACE_ID + " TEXT,"
                + KEY_PLACE_TITLE + " TEXT,"
                + KEY_PLACE_ADDRESS + " TEXT,"
                + KEY_PLACE_DISTANCE + " TEXT,"
                + KEY_PLACE_RATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + );
//        onCreate(db);
        //        TODO when updatind DB must make ulter query for updating table instead of drop table and recreeate it again:)
        if (oldVersion < 1) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
        }
    }
}
