package org.sairaa.mobilestor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import org.sairaa.mobilestor.data.MobileContract.MobileEntry;
import org.sairaa.mobilestor.data.MobileDbHelper;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private MobileDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new MobileDbHelper(this);

        // Set up bottom navigation icons to switch between top-level content views with a single tap:
        final BottomNavigationView mBottomNav = findViewById(R.id.bottom_navigation);
        mBottomNav.setSelectedItemId(R.id.main_nav);
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.editor_nav:
                        Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection ={
            MobileEntry._ID,
            MobileEntry.COULMN_PRODUCT_NAME,
            MobileEntry.COULMN_PRODUCT_PRICE,
            MobileEntry.COULMN_PRODUCT_QUANTITY,
            MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME,
            MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO
        };

        Cursor cursor = db.query(
                MobileEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        TextView displayView = (TextView) findViewById(R.id.textView);
        try {
            displayView.setText("The mobile table contains " + cursor.getCount() + " Mobiles.\n\n");
            displayView.append(MobileEntry._ID + " - " +
                    MobileEntry.COULMN_PRODUCT_NAME + " - " +
                    MobileEntry.COULMN_PRODUCT_PRICE + " - " +
                    MobileEntry.COULMN_PRODUCT_QUANTITY + " - " +
                    MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME + " - " +
                    MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO + "\n");
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MobileEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNoColumnIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                long currentPrice = cursor.getLong(priceColumnIndex);
                int  currentStock= cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhoneNo = cursor.getString(supplierPhoneNoColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentStock + " - " +
                        currentSupplierName+ " - " +
                        currentSupplierPhoneNo));
            }

        } finally {

            cursor.close();
        }
    }
}
