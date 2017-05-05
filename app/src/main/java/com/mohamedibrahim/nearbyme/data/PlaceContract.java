package com.mohamedibrahim.nearbyme.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mohamed Ibrahim
 * on 5/5/2017.
 */

public class PlaceContract {

    public static final String AUTHORITY = "com.mohamedibrahim.nearbyme";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_PLACES = "PLACES";

    public static final class PlaceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();

        public static final String TABLE_PLACES = "places";
        public static final String KEY_PRIMARY_ID = "primary_id";
        public static final String KEY_PLACE_ID = "place_id";
        public static final String KEY_PLACE_TITLE = "place_title";
        public static final String KEY_PLACE_ADDRESS = "place_Address";
        public static final String KEY_PLACE_DISTANCE = "place_distance";
        public static final String KEY_PLACE_RATE = "place_rate";

    }
}
