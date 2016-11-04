package com.mohamedibrahim.nearbyme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.models.places.Location;
import com.mohamedibrahim.nearbyme.models.places.Venue;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 11/4/2016.
 **/

public class PlacesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "placesManager";
    private static final String TABLE_PLACES = "places";

    // Contacts Table Columns names
    private static final String KEY_PRIMARY_ID = "primary_id";
    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_PLACE_TITLE = "place_title";
    private static final String KEY_PLACE_ADDRESS = "place_Address";
    private static final String KEY_PLACE_DISTANCE = "place_distance";
    private static final String KEY_PLACE_RATE = "place_rate";

    public PlacesDBHelper(Context context) {
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
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // Create tables again
        onCreate(db);
    }

    // Adding new place
    public void addPlace(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, item.getVenue().getId());
        values.put(KEY_PLACE_TITLE, item.getVenue().getName());
        values.put(KEY_PLACE_ADDRESS, item.getVenue().getLocation().getAddress());
        values.put(KEY_PLACE_DISTANCE, item.getVenue().getLocation().getDistance());
        values.put(KEY_PLACE_RATE, item.getVenue().getRating());
        // Inserting Row
        db.insert(TABLE_PLACES, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single place
    public void deletePlace(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, KEY_PLACE_ID + " = ?",
                new String[]{String.valueOf(item.getVenue().getId())});
        db.close();
    }

    public boolean ifPlaceFavorite(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLACES, null, KEY_PLACE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Getting All Places
    public ArrayList<Item> getAllPlaces() {
        ArrayList<Item> placesArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Venue venue = new Venue();
                venue.setId(cursor.getString(1));
                venue.setName(cursor.getString(2));

                Location location = new Location();
                location.setAddress(cursor.getString(3));
                location.setDistance(cursor.getString(4));
                venue.setLocation(location);

                venue.setRating(cursor.getString(5));

                Item item = new Item();
                item.setVenue(venue);

                // Adding place to list
                placesArrayList.add(item);
            } while (cursor.moveToNext());
        }
        // return places list
        return placesArrayList;
    }
}
