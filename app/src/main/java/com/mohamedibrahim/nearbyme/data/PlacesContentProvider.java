package com.mohamedibrahim.nearbyme.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.mohamedibrahim.nearbyme.data.PlacesContract.PlaceEntry.TABLE_PLACES;

/**
 * Created by Mohamed Ibrahim
 * on 5/5/2017.
 */

public class PlacesContentProvider extends ContentProvider {

    private static final int PLACES = 101;
    private static final int PLACE_WITH_ID = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PlacesContract.AUTHORITY, PlacesContract.PATH_PLACES, PLACES);
        uriMatcher.addURI(PlacesContract.AUTHORITY, PlacesContract.PATH_PLACES + "/#", PLACE_WITH_ID);
        return uriMatcher;
    }

    private PlacesDBHelper mPlacesDBHelper;

    @Override
    public boolean onCreate() {
        mPlacesDBHelper = PlacesDBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db;

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case PLACES:
                db = mPlacesDBHelper.getReadableDatabase();
                retCursor = db.query(TABLE_PLACES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mPlacesDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case PLACES:
                long id = db.insert(TABLE_PLACES, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PlacesContract.PlaceEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db;
        int match = sUriMatcher.match(uri);

        int tasksDeleted;

        switch (match) {
            case PLACES:
                db = mPlacesDBHelper.getWritableDatabase();
                tasksDeleted = db.delete(TABLE_PLACES, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted = 0;
        String table = "";
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PLACES:
                table = TABLE_PLACES;
                break;
        }
        SQLiteDatabase db = mPlacesDBHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues cv : values) {
                long newID = db.insertOrThrow(table, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }

}
