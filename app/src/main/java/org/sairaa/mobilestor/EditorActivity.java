package org.sairaa.mobilestor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sairaa.mobilestor.data.MobileContract;
import org.sairaa.mobilestor.data.MobileContract.MobileEntry;
import org.sairaa.mobilestor.data.MobileDbHelper;

public class EditorActivity extends AppCompatActivity {
    private EditText productName;
    private EditText productPrice;
    private EditText productQuantity;
    private EditText supplierName;
    private EditText supplierPhoneNo;
    private Button save;
    private Button cancel;

    private MobileDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();
        if(currentUri == null){
            setTitle("Add Stock");
        }else{
            setTitle("Edit Details");
        }
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.price);
        productQuantity = findViewById(R.id.quantity);
        supplierName = findViewById(R.id.supplier_name);
        supplierPhoneNo = findViewById(R.id.supplier_phone_no);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        mDbHelper = new MobileDbHelper(this);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insetNewMobileStock();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void insetNewMobileStock() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(!productName.getText().toString().trim().isEmpty()
                && !productPrice.getText().toString().trim().isEmpty()
                && !productQuantity.getText().toString().trim().isEmpty()
                && !supplierName.getText().toString().trim().isEmpty()){

            ContentValues values = new ContentValues();
            values.put(MobileEntry.COULMN_PRODUCT_NAME, productName.getText().toString().trim());
            values.put(MobileEntry.COULMN_PRODUCT_PRICE,Integer.parseInt(productPrice.getText().toString().trim()));
            values.put(MobileEntry.COULMN_PRODUCT_QUANTITY,Integer.parseInt(productQuantity.getText().toString().trim()));
            values.put(MobileEntry.COULMN_PRODUCT_SUPPLIER_NAME,supplierName.getText().toString().trim());
            values.put(MobileEntry.COULMN_PRODUCT_SUPPLIER_PHONE_NO,supplierPhoneNo.getText().toString().trim());

            Uri uri = getContentResolver().insert(MobileEntry.CONTENT_URI,values);
            if(uri == null){
                Toast.makeText(EditorActivity.this,getString(R.string.insert_fail),Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(EditorActivity.this,getString(R.string.inserted_successfully)+uri,Toast.LENGTH_SHORT).show();

            Log.v("MainActivity","new Row Id: "+uri);

        }else {
            Toast.makeText(EditorActivity.this,getString(R.string.required_info),Toast.LENGTH_SHORT).show();
        }


    }
}
