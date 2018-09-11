package org.sairaa.mobilestor.data;

import android.provider.BaseColumns;

public final class MobileContract {
    public MobileContract() {
    }

    public static final class MobileEntry implements BaseColumns {
        public final static String TABLE_NAME ="mobile_store";

        public final static String _ID = BaseColumns._ID;
        public final static String COULMN_PRODUCT_NAME = "name";
        public final static String COULMN_PRODUCT_PRICE = "price";
        public final static String COULMN_PRODUCT_QUANTITY = "quantity";
        public final static String COULMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
        public final static String COULMN_PRODUCT_SUPPLIER_PHONE_NO = "supplier_number";

    }
}
