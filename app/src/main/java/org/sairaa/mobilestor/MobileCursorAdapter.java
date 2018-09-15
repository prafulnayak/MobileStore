package org.sairaa.mobilestor;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.sairaa.mobilestor.data.MobileContract.MobileEntry;

public class MobileCursorAdapter extends CursorAdapter {

    public MobileCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView productName = view.findViewById(R.id.product_name);
        final TextView totalStock = view.findViewById(R.id.total_stock);
        TextView price = view.findViewById(R.id.product_price);
        Button sale = view.findViewById(R.id.sale);

        final int idColumnIndex = cursor.getInt(cursor.getColumnIndex(MobileEntry._ID));
        int productNameIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_NAME);
        int stockIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_QUANTITY);
        int priceIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_PRICE);

        productName.setText(cursor.getString(productNameIndex));
        final int stock = cursor.getInt(stockIndex);
        final String inStock = context.getString(R.string.in_stock)+String.valueOf(cursor.getInt(stockIndex));
        totalStock.setText(inStock);
        String productPrice =context.getString(R.string.rs)+String.valueOf(cursor.getDouble(priceIndex));
        price.setText(productPrice);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stock >= 1){
                    Uri contentPetUri = ContentUris.withAppendedId(MobileEntry.CONTENT_URI,idColumnIndex );
                    ContentValues values = new ContentValues();
                    values.put(MobileEntry.COULMN_PRODUCT_QUANTITY,stock-1);
                    context.getContentResolver().update(contentPetUri,values,null,null);

                }else {
                    Toast.makeText(context,context.getString(R.string.stock_negative),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
