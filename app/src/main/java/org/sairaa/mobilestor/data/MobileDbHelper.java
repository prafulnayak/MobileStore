package org.sairaa.mobilestor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.sairaa.mobilestor.data.MobileContract.MobileEntry;

public class MobileDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;


    public MobileDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE pets (_id INTEGER, name TEXT, breed TEXT, gender INTEGER, weight INTEGER);

        String SQL_CREATE_TABLE = "CREATE TABLE "+ MobileEntry.TABLE_NAME+ " ("
                +MobileEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +MobileEntry.COULMN_PRODUCT_NAME+" TEXT NOT NULL, "
                +MobileEntry.COULMN_PRODUCT_PRICE+" REAL NOT NULL DEFAULT 0.00, "
                +MobileEntry.COULMN_PRODUCT_QUANTITY+" INTEGER NOT NULL DEFAULT 0, "
                +MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME+" TEXT NOT NULL, "
                +MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO+" TEXT);";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
