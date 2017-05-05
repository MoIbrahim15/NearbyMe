package com.mohamedibrahim.nearbyme.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mohamedibrahim.nearbyme.data.PlacesContract;
import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.models.places.Location;
import com.mohamedibrahim.nearbyme.models.places.Venue;

import java.util.ArrayList;

import static com.mohamedibrahim.nearbyme.data.PlacesContract.PlaceEntry.KEY_PLACE_ADDRESS;
import static com.mohamedibrahim.nearbyme.data.PlacesContract.PlaceEntry.KEY_PLACE_DISTANCE;
import static com.mohamedibrahim.nearbyme.data.PlacesContract.PlaceEntry.KEY_PLACE_ID;
import static com.mohamedibrahim.nearbyme.data.PlacesContract.PlaceEntry.KEY_PLACE_RATE;
import static com.mohamedibrahim.nearbyme.data.PlacesContract.PlaceEntry.KEY_PLACE_TITLE;

/**
 * Created by Mohamed Ibrahim
 * on 5/5/2017.
 */

public class DBUtils {

    public static void addPlace(Item item, Context context) {
        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, item.getVenue().getId());
        values.put(KEY_PLACE_TITLE, item.getVenue().getName());
        values.put(KEY_PLACE_ADDRESS, item.getVenue().getLocation().getAddress());
        values.put(KEY_PLACE_DISTANCE, item.getVenue().getLocation().getDistance());
        values.put(KEY_PLACE_RATE, item.getVenue().getRating());

        context.getContentResolver().insert(PlacesContract.PlaceEntry.CONTENT_URI,
                values);
    }

    public static void deletePlace(Item item,Context context) {
        context.getContentResolver().delete(PlacesContract.PlaceEntry.CONTENT_URI,
                PlacesContract.PlaceEntry.KEY_PLACE_ID + "=?", new String[]{String.valueOf(item.getVenue().getId())});

    }

    public static boolean ifPlaceFavorite(String id,Context context) {

        Cursor cursor = context.getContentResolver().query(PlacesContract.PlaceEntry.CONTENT_URI,
                null,
                PlacesContract.PlaceEntry.KEY_PLACE_ID + "=?",
                new String[]{String.valueOf(id)},
                null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getCount();
            cursor.close();
        }
        return count > 0;
    }

    // Getting All Places
    public static ArrayList<Item> getAllPlaces(Context context) {
        ArrayList<Item> items = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(PlacesContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
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
                items.add(item);

            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return items;
    }
}
