package org.sairaa.mobilestor.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.sairaa.mobilestor.data.MobileContract.MobileEntry;

public class MobileProvider extends ContentProvider {

    private static final String LOG_TAG = MobileProvider.class.getSimpleName();
    private MobileDbHelper mobileDbHelper;

    private static final int MOBILES = 100;

    private static final int MOBILES_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MobileContract.CONTENT_AUTHORITY, MobileContract.PATH_PRODUCTS, MOBILES);
        sUriMatcher.addURI(MobileContract.CONTENT_AUTHORITY, MobileContract.PATH_PRODUCTS + "/#", MOBILES_ID);
    }
    @Override
    public boolean onCreate() {
        mobileDbHelper = new MobileDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection, // The columns to return
                        String selection, // The columns for the WHERE clause
                        String[] selectionArgs, // The values for the WHERE clause
                        String sortOrder){  //The Sort Order
        SQLiteDatabase database = mobileDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case MOBILES:
                cursor = database.query(MobileEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOBILES_ID:
                selection = MobileEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MobileEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOBILES:
                return MobileEntry.CONTENT_LIST_TYPE;
            case MOBILES_ID:
                return MobileEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + sUriMatcher.match(uri));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = mobileDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MOBILES:
                return insertMobileInfo(uri,contentValues);

        }
        return null;
    }

    private Uri insertMobileInfo(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(MobileEntry.COULMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Products requires a name");
        }

        Long price = contentValues.getAsLong(MobileEntry.COULMN_PRODUCT_PRICE);
        if (price != null && price < 1) {
            throw new IllegalArgumentException("Product requires +ve price");

        }
        Long qty = contentValues.getAsLong(MobileEntry.COULMN_PRODUCT_QUANTITY);
        if (qty != null && qty < 0) {
            throw new IllegalArgumentException("Product requires valid stock");

        }
        String sName = contentValues.getAsString(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME);
        if (sName == null) {
            throw new IllegalArgumentException("Supplier requires a name");
        }
        SQLiteDatabase database = mobileDbHelper.getReadableDatabase();
        long newRowId = database.insert(MobileEntry.TABLE_NAME,null,contentValues);
        if (newRowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mobileDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (sUriMatcher.match(uri)){
            case MOBILES:
                rowsDeleted = database.delete(MobileEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case MOBILES_ID:
                selection = MobileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MobileEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)){
            case MOBILES:
                return updateMobileInfo(uri, contentValues, selection, selectionArgs);

            case MOBILES_ID:
                selection = MobileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMobileInfo(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateMobileInfo(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if(contentValues.containsKey(MobileEntry.COULMN_PRODUCT_NAME)){
            String name = contentValues.getAsString(MobileEntry.COULMN_PRODUCT_NAME);
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Products requires a name");
            }
        }
        if(contentValues.containsKey(MobileEntry.COULMN_PRODUCT_PRICE)){
            Long price = contentValues.getAsLong(MobileEntry.COULMN_PRODUCT_PRICE);
            if (price != null && price < 1) {
                throw new IllegalArgumentException("Product requires +ve price");

            }
        }
        if(contentValues.containsKey(MobileEntry.COULMN_PRODUCT_QUANTITY)){
            Long qty = contentValues.getAsLong(MobileEntry.COULMN_PRODUCT_QUANTITY);
            if (qty != null && qty < 0) {
                throw new IllegalArgumentException("Product requires valid stock");

            }
        }
        if(contentValues.containsKey(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME)){
            String sName = contentValues.getAsString(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME);
            if (sName.isEmpty()) {
                throw new IllegalArgumentException("Supplier requires a name");
            }
        }

        SQLiteDatabase database = mobileDbHelper.getReadableDatabase();

        int rowsUpdated = database.update(MobileEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
