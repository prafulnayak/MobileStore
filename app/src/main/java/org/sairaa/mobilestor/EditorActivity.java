package org.sairaa.mobilestor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sairaa.mobilestor.data.MobileContract;
import org.sairaa.mobilestor.data.MobileContract.MobileEntry;
import org.sairaa.mobilestor.data.MobileDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private EditText productName;
    private EditText productPrice;
    private EditText productQuantity;
    private EditText supplierName;
    private EditText supplierPhoneNo;
    private Button save;
    private Button cancel;
    private Button stockDown, stockUp;
    private Button call;

    private MobileDbHelper mDbHelper;

    private Uri currentUri;

    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        currentUri = intent.getData();
        if (currentUri == null) {
            setTitle(getString(R.string.add_stock));
        } else {
            setTitle(getString(R.string.edit_stock));
            getLoaderManager().initLoader(1, null, this);
        }
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.price);
        productQuantity = findViewById(R.id.quantity);
        supplierName = findViewById(R.id.supplier_name);
        supplierPhoneNo = findViewById(R.id.supplier_phone_no);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        stockDown = findViewById(R.id.stock_down);
        stockUp = findViewById(R.id.stock_up);
        call = findViewById(R.id.call);
        mDbHelper = new MobileDbHelper(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        stockDown.setOnClickListener(this);
        stockUp.setOnClickListener(this);
        call.setOnClickListener(this);

    }

    private void insetNewMobileStock() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (!productName.getText().toString().trim().isEmpty()
                && !productPrice.getText().toString().trim().isEmpty()
                && !productQuantity.getText().toString().trim().isEmpty()
                && !supplierName.getText().toString().trim().isEmpty()
                && !supplierPhoneNo.getText().toString().trim().isEmpty()) {
            if (supplierPhoneNo.getText().toString().trim().length() == 10) {
                ContentValues values = getContentValuesFromEditText();

                Uri uri = getContentResolver().insert(MobileEntry.CONTENT_URI, values);
                if (uri == null) {
                    Toast.makeText(EditorActivity.this, getString(R.string.insert_fail), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(EditorActivity.this, getString(R.string.inserted_successfully) + uri, Toast.LENGTH_SHORT).show();

                finish();
            } else
                Toast.makeText(EditorActivity.this, getString(R.string.ten_digit), Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(EditorActivity.this, getString(R.string.required_info), Toast.LENGTH_SHORT).show();
        }


    }

    private ContentValues getContentValuesFromEditText() {
        ContentValues values = new ContentValues();
        values.put(MobileEntry.COULMN_PRODUCT_NAME, productName.getText().toString().trim());
        values.put(MobileEntry.COULMN_PRODUCT_PRICE, Double.parseDouble(productPrice.getText().toString().trim()));
        values.put(MobileEntry.COULMN_PRODUCT_QUANTITY, Integer.parseInt(productQuantity.getText().toString().trim()));
        values.put(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME, supplierName.getText().toString().trim());
        values.put(MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO, supplierPhoneNo.getText().toString().trim());
        return values;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MobileEntry._ID,
                MobileEntry.COULMN_PRODUCT_NAME,
                MobileEntry.COULMN_PRODUCT_PRICE,
                MobileEntry.COULMN_PRODUCT_QUANTITY,
                MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME,
                MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO
        };
        return new CursorLoader(this,
                currentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int productNameIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_NAME);
            int stockIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_QUANTITY);
            int priceIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_PRICE);
            int supplierNameIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO);

            productName.setText(cursor.getString(productNameIndex));
            productPrice.setText(String.valueOf(cursor.getDouble(priceIndex)));
            productQuantity.setText(String.valueOf(cursor.getInt(stockIndex)));
            supplierName.setText(cursor.getString(supplierNameIndex));
            phoneNo = cursor.getString(supplierPhoneIndex);
            supplierPhoneNo.setText(phoneNo);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_single_entries);
            menuItem.setVisible(false);
            call.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.action_delete_single_entries:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.unsaved_changes_dialog_msg);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int rowDeleted = getContentResolver().delete(currentUri, null, null);
                        if (rowDeleted != -1) {
                            Toast.makeText(EditorActivity.this, getString(R.string.deleted_success), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(EditorActivity.this, getString(R.string.delete_unsuccessful), Toast.LENGTH_SHORT).show();
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                        finish();

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

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                if (currentUri == null) {
                    insetNewMobileStock();
                } else {
                    if (!productName.getText().toString().trim().isEmpty()
                            && !productPrice.getText().toString().trim().isEmpty()
                            && !productQuantity.getText().toString().trim().isEmpty()
                            && !supplierName.getText().toString().trim().isEmpty()
                            && !supplierPhoneNo.getText().toString().trim().isEmpty()) {
                        if (supplierPhoneNo.getText().toString().trim().length() == 10) {
                            ContentValues values = getContentValuesFromEditText();
                            int rowsAffected = getContentResolver().update(currentUri, values, null, null);
                            if (rowsAffected != -1) {
                                Toast.makeText(EditorActivity.this, getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditorActivity.this, getString(R.string.edit_failed),
                                        Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } else
                            Toast.makeText(EditorActivity.this, getString(R.string.ten_digit), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(EditorActivity.this, getString(R.string.required_info), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.stock_up:
                if(productQuantity.getText().toString().trim().isEmpty()){
                    productQuantity.setText(getString(R.string.zero));
                }
                int stock = Integer.parseInt(productQuantity.getText().toString().trim());
                stock++;
                productQuantity.setText(String.valueOf(stock));
                break;
            case R.id.stock_down:
                if(productQuantity.getText().toString().trim().isEmpty()){
                    productQuantity.setText(getString(R.string.zero));
                }
                int stockD = Integer.parseInt(productQuantity.getText().toString().trim());
                if (stockD >= 1) {
                    stockD--;
                    productQuantity.setText(String.valueOf(stockD));
                } else
                    Toast.makeText(EditorActivity.this, getString(R.string.stock_negative), Toast.LENGTH_SHORT).show();
                break;
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    return;
                }

                break;
            default:

        }
    }
}
