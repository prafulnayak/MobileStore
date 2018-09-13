package org.sairaa.mobilestor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.sairaa.mobilestor.data.MobileContract;
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
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView productName = view.findViewById(R.id.product_name);
        TextView totalStock = view.findViewById(R.id.total_stock);
        TextView price = view.findViewById(R.id.product_price);
        Button sale = view.findViewById(R.id.sale);

        int productNameIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_NAME);
        int stockIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_QUANTITY);
        int priceIndex = cursor.getColumnIndex(MobileEntry.COULMN_PRODUCT_PRICE);

        productName.setText(cursor.getString(productNameIndex));
        totalStock.setText(String.valueOf(cursor.getInt(stockIndex)));
//        price.setText(String.valueOf(cursor.getLong(priceIndex)));
        price.setText(String.valueOf(cursor.getDouble(priceIndex)));

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditorActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
