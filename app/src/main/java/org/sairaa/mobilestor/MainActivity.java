package org.sairaa.mobilestor;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import org.sairaa.mobilestor.data.MobileContract;
import org.sairaa.mobilestor.data.MobileContract.MobileEntry;
import org.sairaa.mobilestor.data.MobileDbHelper;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int MOBILE_LOADER = 1;
    private ListView listView;
    private MobileCursorAdapter adapter;

    private MobileDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        final View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        adapter = new MobileCursorAdapter(this,null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positin, long id) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                Uri contentPetUri = ContentUris.withAppendedId(MobileEntry.CONTENT_URI, id);

                // set the URI on the dat field of the intent
                intent.setData(contentPetUri);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(MOBILE_LOADER,null,this);

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

                    case R.id.main_nav:
                        // This activity may show search results instead of all products, so we need
                        // to refresh it if user taps a "Home" button
                        listView.setAdapter(adapter);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection ={
                MobileEntry._ID,
                MobileEntry.COULMN_PRODUCT_NAME,
                MobileEntry.COULMN_PRODUCT_PRICE,
                MobileEntry.COULMN_PRODUCT_QUANTITY,
                MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME,
                MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO
        };
        return new CursorLoader(this,
                MobileEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_all_entries);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deleteAll = getContentResolver().delete(MobileEntry.CONTENT_URI,null,null);
                        if(deleteAll != -1){
                            Toast.makeText(MainActivity.this,getString(R.string.deleted_success),Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(MainActivity.this,getString(R.string.delete_unsuccessful),Toast.LENGTH_SHORT).show();
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Keep editing" button, so dismiss the dialog
                        // and continue editing the pet.
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                // Do nothing for now

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
