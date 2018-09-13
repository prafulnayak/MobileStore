package org.sairaa.mobilestor.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MobileContract {
    public static final String CONTENT_AUTHORITY = "org.sairaa.mobilestor";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "mobile_store";
    public MobileContract() {
    }

    public static final class MobileEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public final static String TABLE_NAME ="mobile_store";

        public final static String _ID = BaseColumns._ID;
        public final static String COULMN_PRODUCT_NAME = "name";
        public final static String COULMN_PRODUCT_PRICE = "price";
        public final static String COULMN_PRODUCT_QUANTITY = "quantity";
        public final static String COULMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
        public final static String COULMN_PRODUCT_SUPPLIER_PHONE_NO = "supplier_number";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

    }
}
