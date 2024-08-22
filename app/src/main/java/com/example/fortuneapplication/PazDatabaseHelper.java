package com.example.fortuneapplication;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class PazDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "PAZ_DIST.db";
    protected static final String TABLE_NAME = "Item_Table";
    protected static final String ITEMID = "item_id";
    protected static final String ITEM_CODE = "item_code";
    protected static final String ITEM_DESCRIPTION = "item_description";
    protected static final String ITEM_RATE = "item_rate";
    protected static final String ITEM_GROUP = "item_group";
    protected static final String ITEM_QUANTITY = "item_quantity";
    protected static final String ITEM_UNIT_MEASURE = "item_unit_measure";
    private static final String ITEM_VENDOR = "item_vendor";
    private static final String INACTIVE = "inactive";
    //**SYNC HISTORY TABLE
    protected static final String SYNC_HISTORY_TABLE = "sync_history";
    protected static final String SYNC_HISTORY_TABLE_ID = "id";
    protected static final String SYNC_HISTORY_TABLE_NAME = "tabe_name";
    protected static final String SYNC_HISTORY_TABLE_DATE = "date";

    //*CUSTOMER TABLE //*
    protected static final String CUSTOMER_TABLE = "Customer_Table";
    protected static final String CUSTOMER_ID = "customer_id";
    protected static final String CUSTOMER_NAME = "customer_name";
    protected static final String CUSTOMER_ADDRESS = "customer_address";
    private static final String CONTACT_PERSON = "contact_person";
    protected static final String MOBILE_NO = "mobile_no";
    private static final String TELEPHONE_NO = "telephone_no";
    private static final String PAYMENT_TERMS_ID = "payment_terms_id";
    private static final String SALES_REP_ID = "sales_rep_id";
    private static final String PRICE_LEVEL_ID = "price_level_id";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String PIN_FLAG = "pin_flag";
    private static final String VERIFY = "verify";

    //*CUSTOMER TABLE //*
    protected static final String CUSTOMER_COVERAGE_TABLE = "customer_coverage_plan";
    protected static final String COVERAGE_ID = "id";
    protected static final String COVERAGE_CUSTOMER_ID = "customer_id";
    protected static final String COVERAGE_SALESREP_ID = "salesrep_id";
    protected static final String COVERAGE_DAY = "coverage_day";
    protected static final String COVERAGE_FORECAST = "frequency";
    protected static final String COVERAGE_FORECAST_WEEK_SCHEDULE = "frequency_week_schedule";


    //* SALES_REP_TABLE //*
    public static final String SALESREP_TABLE = "Sales_Rep_Table";
    static final String SALESREP_ID = "salesrep_id";
    protected static final String SALESREP_NAME = "salesrep_name";
    private static final String SALESREP_ADDRESS = "salesrep_address";
    private static final String SALESREP_CONTACT = "salesrep_contact";

    //*Location_table//*
    protected static final String LOCATION_TABLE = "Location_Table";
    protected static final String LOCATION_ID = "location_id";
    protected static final String LOCATION_NAME = "location_name";

    //* ORDER ITEM TABLE//*
    private static final String ORDER_ITEM_TABLE = "Order_Item_Table";
    private static final String ORDER_ITEM_ID = "id";
    private static final String ORDER_PRICELEVEL_ID = "price_level_id";
    private static final String ORDER_ITEM_CODE = "order_item_code";
    private static final String ORDER_ITEM_DESCRIPTION = "order_item_description";
    private static final String ORDER_UNIT_BASE = "unit_base";
    private static final String ORDER_ITEM_UOM = "order_item_uom";
    private static final String ORDER_ITEM_PRICE = "order_item_price";
    private static final String ORDER_ITEM_QUANTITY = "order_item_quantity";
    private static final String ORDER_ITEM_TOTAL = "order_item_total";

    //*Unit table//?*
    protected static final String UNIT_MEASURE_TABLE = "Unit_Measure_Table";
    protected static final String UNIT_M_ID = "umt_id";
    protected static final String ITM = "itm_id";

    protected static final String NAME = "name";
    protected static final String QUANTITY = "quantity";
    protected static final String UNIT_ID = "unit_id";

    //payment terms table
    private static final String PAYMENT_TERMS_TABLE = "Payment_Terms";
    private static final String PTERMS_ID = "pterms_id";
    private static final String PTERMS_CODE = "pterms_code";
    private static final String PTERMS_DESCRIPTION = "pterms_des";
    private static final String PTERMS_NET_DUE = "pterms_net_due";

    //PRICE LEVEL LINES//
    protected static final String PRICE_LEVEL_LINES_TABLE = "Price_Level_Lines_table";
    protected static final String PRICE_LEVEL_LINES_ID = "price_level_lines_id";
    protected static final String PRI_LEVEL_ID = "pri_level_id";
    protected static final String PRITEM_ID = "prItem_id";
    protected static final String PRCUSTOM_PRICE = "prcustom_price";

    // PRICE LEVEL//
    protected static final String PRICE_LEVEL_TABLE = "Price_Level_Table";
    protected static final String PRICE_LEVELID = "price_level_id";
    protected static final String PRICE_LEVEL_CODE = "price_level_code";
    protected static final String PRICE_LEVEL_DESCRIPTION = "price_level_des";
    private static final String TAG = "MyActivity";


    // SALES ORDER TABLE //
    protected static final String SALES_ORDER_TABLE = "Sales_Order_table";
    protected static final String SALES_ORDERID = "Sales_OrderID";
    protected static final String CODE = "code";
    private static final String DATE = "date";
    protected static final String SOCUSTOMER_ID = "customer_id";
    protected static final String SOLOCATION_ID = "location_id";
    protected static final String SOSALESREPID = "sales_rep_id";
    private static final String DATE_NEEDED = "date_needed";
    private static final String PO_NUMBER = "po_number";
    private static final String SHIP_VIA_ID = "ship_via_id";
    protected static final String AMOUNT = "amount";
    private static final String NOTES = "notes";
    private static final String CUSTOM_FIELD1 = "custom_field1";
    private static final String CUSTOM_FIELD2 = "custom_field2";
    private static final String CUSTOM_FIELD3 = "custom_field3";
    private static final String CUSTOM_FIELD4 = "custom_field4";
    private static final String CUSTOM_FIELD5 = "custom_field5";
    private static final String BEGIN_ORDER = "begin_order";
    private static final String END_ORDER = "end_order";
    private static final String POSTED = "posted";

    // SALES ORDER ITEMS //
    protected static final String SALES_ORDER_ITEMS_TABLE = "Sales_Order_Items_Table";
    protected static final String SOIID = "Id";
    protected static final String SALES_ORDERITEMID = "sales_order_id";
    protected static final String SOI_ITEM_ID = "item_id";
    protected static final String SOI_QUANTITY = "quantity";
    protected static final String SOI_UNIT_BASE_QUANTITY = "unit_base_quantity";
    protected static final String SOI_UOM = "uom";
    protected static final String SOI_RATE = "rate";
    protected static final String SOI_AMOUNT = "amount";
    private static final String TAXABLE = "taxable";
    private static final String TAXABLE_AMOUNT = "taxable_amount";
    private static final String TAX_AMOUNT = "tax_amount";
    private static final String SOI_PRICE_LEVEL_ID = "price_level_id";
    private static final String SOI_CUSTOM_FIELD1 = "soi_custom_field1";
    private static final String SOI_CUSTOM_FIELD2 = "soi_custom_field2";
    private static final String SOI_CUSTOM_FIELD3 = "soi_custom_field3";
    private static final String SOI_CUSTOM_FIELD4 = "soi_custom_field4";
    private static final String SOI_CUSTOM_FIELD5 = "soi_custom_field5";
    //Dashboard table
    protected static final String DASHBOARD_TABLE = "Dashboard_Table";
    protected static final String DASHBOARDID = "Id";
    protected static final String D_CODE = "d_code";
    protected static final String D_CUSTOMER_NAME = "d_customer";
    protected static final String D_TOTAL = "d_total";
    protected static final String D_DATE = "d_date";

    //Connection table
    protected static final String CONNECTION_TABLE  = "Connection_Table";
    protected static final String CONECTIONID = "id";
    protected static final String CONNECTION_NAME = "conection_name";
    protected static final String CONNECTION_IP = "conenction_ip";

    //SYSTEM SETTINGS TABLE
    protected static final String SYSTEM_SETTINGS = "SYSTEM_SETTINGS";
    protected static final String SYSTEM_SETTINGS_ID = "id";
    protected static final String SYSTEM_SETTINGS_NAME = "NAME";
    protected static final String SYSTEM_SETTINGS_VALUE = "VALUE";


    protected static final String CUSTOMER_SKIP_TABLE  = "customer_skip_table";
    protected static final String CUSTOMER_SKIP_TABLE_ID = "id";
    protected static final String  CUSTOMER_SKIP_TABLE_CUSTOMER_ID = "customer_id";
    protected static final String  CUSTOMER_SKIP_TABLE_DATETIME = "datetime";
    protected static final String  CUSTOMER_SKIP_TABLE_REASON = "reason";

    protected static final String REQUEST_REPIN_TABLE  = "request_repin_table";
    protected static final String REQUEST_REPIN_ID = "id";
    protected static final String REQUEST_REPIN_CUSTOMER_ID = "customer_id";
    protected static final String REQUEST_REPIN_DATE = "date";
    protected static final String REQUEST_REPIN_STATUS = "status";
    LocalDate datenow = LocalDate.now();


    public PazDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                ITEMID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ITEM_CODE + " TEXT, " +
                ITEM_DESCRIPTION + " TEXT, " +
                ITEM_RATE + " TEXT, " +
                ITEM_GROUP + " TEXT, " +
                ITEM_QUANTITY + " TEXT, " +
                ITEM_UNIT_MEASURE + " TEXT, " +
                ITEM_VENDOR + " TEXT," +
                INACTIVE + " INTEGER " +
                ")";

        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_TABLE +
                "(" +
                CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CUSTOMER_NAME + " TEXT," +
                CUSTOMER_ADDRESS + " TEXT," +
                CONTACT_PERSON + " TEXT," +
                MOBILE_NO + " TEXT," +
                TELEPHONE_NO + " TEXT," +
                PAYMENT_TERMS_ID + " INTEGER," +
                SALES_REP_ID + " INTEGER," +
                PRICE_LEVEL_ID + " INTEGER," +
                LONGITUDE + " TEXT,"+
                LATITUDE + " TEXT,"+
                PIN_FLAG + " INT DEFAULT 0,"+
                VERIFY + " INT DEFAULT 0"+
                ")";

        String createTableQuery = "CREATE TABLE " + SALESREP_TABLE + " (" +
                SALESREP_ID + " INTEGER PRIMARY KEY, " +
                SALESREP_NAME + " TEXT, " +
                SALESREP_ADDRESS + " TEXT, " +
                SALESREP_CONTACT + " TEXT, " +
                "password TEXT DEFAULT '1')";

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATION_TABLE + " (" +
                LOCATION_ID + " INTEGER PRIMARY KEY, " +
                LOCATION_NAME + " TEXT)";

        String createItem = "CREATE TABLE " + ORDER_ITEM_TABLE + "(" +
                ORDER_ITEM_ID + " INTEGER PRIMARY KEY," +
                ORDER_PRICELEVEL_ID + " INTEGER, " +
                ORDER_ITEM_CODE + " TEXT NOT NULL," +
                ORDER_ITEM_DESCRIPTION + " TEXT NOT NULL," +
                ORDER_ITEM_UOM + " TEXT NOT NULL," +
                ORDER_UNIT_BASE + " INTEGER, " +
                ORDER_ITEM_PRICE + " REAL NOT NULL," +
                ORDER_ITEM_QUANTITY + " INTEGER NOT NULL," +
                ORDER_ITEM_TOTAL + " REAL NOT NULL" +
                ")";

        String CREATE_UNIT_MEASURE_TABLE = "CREATE TABLE " + UNIT_MEASURE_TABLE + " (" +
                UNIT_M_ID + " INTEGER PRIMARY KEY," +
                ITM + " INTEGER ," +
                NAME + " TEXT ," +
                QUANTITY + " TEXT," +
                UNIT_ID + " INTEGER)";

        String CREATE_PAYMENT_TERMS_TABLE = "CREATE TABLE " + PAYMENT_TERMS_TABLE + "("
                + PTERMS_ID + " INTEGER PRIMARY KEY,"
                + PTERMS_CODE + " TEXT,"
                + PTERMS_DESCRIPTION + " TEXT,"
                + PTERMS_NET_DUE + " INTEGER)";

        String CREATE_PRICE_LEVEL_LINES_TABLE = "CREATE TABLE " + PRICE_LEVEL_LINES_TABLE + " (" +
                PRICE_LEVEL_LINES_ID + " INTEGER, " +
                PRI_LEVEL_ID + " INTEGER, " +
                PRITEM_ID + " INTEGER, " +
                PRCUSTOM_PRICE + " REAL" +
                ");";


        String CREATE_PRICE_LEVEL_TABLE = "CREATE TABLE " + PRICE_LEVEL_TABLE + " (" +
                PRICE_LEVELID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRICE_LEVEL_CODE + " TEXT, " +
                PRICE_LEVEL_DESCRIPTION + " TEXT" +
                ");";


        String CREATE_SALES_ORDER_TABLE = "CREATE TABLE " + SALES_ORDER_TABLE + "(" +
                SALES_ORDERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CODE + " TEXT, " +
                DATE + " TEXT, " +
                SOCUSTOMER_ID + " INTEGER, " +
                SOLOCATION_ID + " INTEGER, " +
                SOSALESREPID + " INTEGER, " +
                DATE_NEEDED + " TEXT, " +
                PO_NUMBER + " INTEGER, " +
                SHIP_VIA_ID + " INTEGER, " +
                AMOUNT + " REAL NOT NULL," +
                NOTES + " TEXT, " +
                CUSTOM_FIELD1 + " TEXT, " +
                CUSTOM_FIELD2 + " TEXT, " +
                CUSTOM_FIELD3 + " TEXT, " +
                CUSTOM_FIELD4 + " TEXT, " +
                CUSTOM_FIELD5 + " TEXT, " +
                BEGIN_ORDER + " TEXT, " +
                END_ORDER + " TEXT, "+
                "status INTEGER DEFAULT '0'," +
                "posted INTEGER DEFAULT '0')";

               // ");";

        String CREATE_SALES_ORDER_ITEMS_TABLE = "CREATE TABLE " + SALES_ORDER_ITEMS_TABLE + " (" +
                SOIID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SALES_ORDERITEMID + " INTEGER, " +
                SOI_ITEM_ID + " INTEGER, " +
                SOI_QUANTITY + " INTEGER, " +
                SOI_UNIT_BASE_QUANTITY + " DOUBLE, " +
                SOI_UOM + " TEXT, " +
                SOI_RATE + " DOUBLE, " +
                SOI_AMOUNT + " DOUBLE, " +
                TAXABLE + " DOUBLE, " +
                TAXABLE_AMOUNT + " DOUBLE, " +
                TAX_AMOUNT + " DOUBLE, " +
                SOI_PRICE_LEVEL_ID + " INTEGER, " +
                SOI_CUSTOM_FIELD1 + " TEXT, " +
                SOI_CUSTOM_FIELD2 + " TEXT, " +
                SOI_CUSTOM_FIELD3 + " TEXT, " +
                SOI_CUSTOM_FIELD4 + " TEXT, " +
                SOI_CUSTOM_FIELD5 + " TEXT " +
                ");";

        String CREATE_DASHBOARDTABLE = "CREATE TABLE " + DASHBOARD_TABLE + " (" +
                DASHBOARDID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                D_CODE + " TEXT, " +
                D_CUSTOMER_NAME + " TEXT, " +
                D_TOTAL + " REAL," +
                D_DATE + " TEXT " +
                ");";

        String CREATE_CONNECTIONTABLE = "CREATE TABLE " + CONNECTION_TABLE + " (" +
                CONECTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONNECTION_NAME + " TEXT, " +
                CONNECTION_IP + " TEXT, " +
                "defaultconn TEXT DEFAULT '0')";

        String CREATE_SYSTEM_SETTINGS_TABLE = "CREATE TABLE " + SYSTEM_SETTINGS + " (" +
                SYSTEM_SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SYSTEM_SETTINGS_NAME + " TEXT , " +
                SYSTEM_SETTINGS_VALUE+ " TEXT DEFAULT '', UNIQUE("+SYSTEM_SETTINGS_NAME+")" +
                ");";

        String CREATE_SYNC_HISTORY_TABLE="CREATE TABLE " +SYNC_HISTORY_TABLE + " ("+
                SYNC_HISTORY_TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                SYNC_HISTORY_TABLE_NAME+" TEXT, "+
                SYNC_HISTORY_TABLE_DATE+" TEXT "+
                ")";

        String CREATE_CUSTOMERS_COVERAGE_TABLE="CREATE TABLE " +CUSTOMER_COVERAGE_TABLE + " ("+
                COVERAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COVERAGE_CUSTOMER_ID + " INTEGER, " +
                COVERAGE_SALESREP_ID + " INTEGER, " +
                COVERAGE_DAY + " TEXT, " +
                COVERAGE_FORECAST + " TEXT, " +
                COVERAGE_FORECAST_WEEK_SCHEDULE + " TEXT " +
                ")";

        String CREATE_CUSTOMER_SKIP_TABLE ="CREATE TABLE " +CUSTOMER_SKIP_TABLE  + " ("+
                CUSTOMER_SKIP_TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CUSTOMER_SKIP_TABLE_CUSTOMER_ID + " INTEGER, " +
                CUSTOMER_SKIP_TABLE_DATETIME + " TEXT, " +
                CUSTOMER_SKIP_TABLE_REASON + " TEXT, " +
                "status TEXT DEFAULT '0')";

        String CREATE_REQUEST_REPIN_TABLE ="CREATE TABLE " +REQUEST_REPIN_TABLE  + " ("+
                REQUEST_REPIN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                REQUEST_REPIN_CUSTOMER_ID + " INTEGER, " +
                REQUEST_REPIN_DATE + " TEXT, " +
                REQUEST_REPIN_STATUS + " TEXT) ";


        String INSERT_SYNC_HISTORY ="INSERT INTO "+SYNC_HISTORY_TABLE+" VALUES " +
                "(NULL,'ITEM',''),"+
                "(NULL,'CUSTOMER',''),"+
                "(NULL,'LOCATION',''),"+
                "(NULL,'SALESREP',''),"+
                "(NULL,'PAYMENTTERMS',''),"+
                "(NULL,'PRICELEVEL',''),"+
                "(NULL,'PRICELEVELLINES',''),"+
                "(NULL,'UOM',''),"+
                "(NULL,'COVERAGE','')"+
                ""
                ;
//        String  ALTER_ITEM_TABLE = "ALTER TABLE " + SALES_ORDER_TABLE + " ADD COLUMN " +
//                "posted" + " INTEGER DEFAULT 0";

        String INSERT_SYSTEM_SETTINGS ="INSERT INTO "+SYSTEM_SETTINGS+" VALUES (NULL,'SALES_TYPE',''), "
                +"(NULL,'DEFAULT_SALES_REP_ID',''),"
                +"(NULL,'ALLOW_PIN_VERIFY','0'),"
                +"(NULL,'ALLOW_STRICT_COVERAGE','Allow'),"
                +"(NULL,'BLUETOOTH_DEVICE','')";

        db.execSQL(CREATE_CONNECTIONTABLE);
        db.execSQL(CREATE_DASHBOARDTABLE);
        db.execSQL(CREATE_SALES_ORDER_ITEMS_TABLE);
        db.execSQL(CREATE_SALES_ORDER_TABLE);
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(createTableQuery);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(createItem);
        db.execSQL(CREATE_UNIT_MEASURE_TABLE);
        db.execSQL(CREATE_PAYMENT_TERMS_TABLE);
        db.execSQL(CREATE_PRICE_LEVEL_TABLE);
        db.execSQL(CREATE_PRICE_LEVEL_LINES_TABLE);
        db.execSQL(CREATE_SYSTEM_SETTINGS_TABLE);
        db.execSQL(INSERT_SYSTEM_SETTINGS);
        db.execSQL(CREATE_SYNC_HISTORY_TABLE);
        db.execSQL(INSERT_SYNC_HISTORY);
        db.execSQL(CREATE_CUSTOMERS_COVERAGE_TABLE);
        db.execSQL(CREATE_CUSTOMER_SKIP_TABLE);
        db.execSQL(CREATE_REQUEST_REPIN_TABLE);
//        db.execSQL(ALTER_ITEM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +CONNECTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DASHBOARD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SALESREP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + UNIT_MEASURE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRICE_LEVEL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRICE_LEVEL_LINES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SALES_ORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SALES_ORDER_ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SYSTEM_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_SKIP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REQUEST_REPIN_STATUS);
        onCreate(db);
    }


//    public long storeBook(Dashboardne dashboardne) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        double bookTotal = Double.parseDouble(String.valueOf(dashboardne.getBooktotal()));
//        values.put(D_TOTAL_BOOK, bookTotal);
//        long rowId = db.insert(DASHBOARD_TABLE, null, values);
//        db.close(); // Close the database
//
//        return rowId;
//    }
    //store connection detailes
    public long storeConnection(CONNECT connect){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONNECTION_NAME,connect.getName());
        values.put(CONNECTION_IP, connect.getIp());
        long rowdi = db.insert(CONNECTION_TABLE,null, values);
      //  db.close();
        return rowdi;

    }

    //*store JSON ITEM DATA //*
    public boolean StoreData(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEMID, item.getId());
        values.put(ITEM_CODE, item.getCode());
        values.put(ITEM_DESCRIPTION, item.getDescription());
        values.put(ITEM_RATE, item.getRate());
        values.put(ITEM_GROUP, item.getGroup());
        values.put(ITEM_QUANTITY, item.getQuantity());
        values.put(ITEM_UNIT_MEASURE, item.getUnitquant());
        values.put(ITEM_VENDOR, item.getVendor());
        values.put(INACTIVE, item.getInactive());
        db.insertWithOnConflict(TABLE_NAME, null, values,db.CONFLICT_REPLACE);
        return false;
    }

    //*store JSON CUSTOMER DATA //*
    public boolean StroreCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CUSTOMER_ID, customer.getId());
        values.put(CUSTOMER_NAME, customer.getCustomername());
        values.put(CUSTOMER_ADDRESS, customer.getPostaladdress());
        values.put(CONTACT_PERSON, customer.getContactperson());
        values.put(MOBILE_NO, customer.getMobilenumber());
        values.put(TELEPHONE_NO, customer.getTelephonenumber());
        values.put(PAYMENT_TERMS_ID, customer.getPaymenttermsid());
        values.put(SALES_REP_ID, customer.getSalesrepid());
        values.put(PRICE_LEVEL_ID, customer.getPricelevelid());
        values.put(LONGITUDE, customer.getLongitude());
        values.put(LATITUDE,customer.getLatitude());
        values.put(VERIFY,customer.getValidated());
        db.insertWithOnConflict(CUSTOMER_TABLE, null, values,db.CONFLICT_REPLACE);
        return false;
    }

    public boolean StoreCoverage(Coverage coverage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COVERAGE_ID, coverage.get_id());
        values.put(COVERAGE_CUSTOMER_ID, coverage.get_customer_id());
        values.put(COVERAGE_SALESREP_ID, coverage.get_salesrep_id());
        values.put(COVERAGE_DAY, coverage.get_day());
        values.put(COVERAGE_FORECAST, coverage.get_frequency());
        values.put(COVERAGE_FORECAST_WEEK_SCHEDULE, coverage.get_frequency_week_schedule());
        db.insertWithOnConflict(CUSTOMER_COVERAGE_TABLE, null, values,db.CONFLICT_REPLACE);
        return false;
    }

    // sales_rep_table //*
    public boolean storeSalesRep(SalesRepList salesRepList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SALESREP_ID, salesRepList.getSrid());
        values.put(SALESREP_NAME, salesRepList.getSrname());
        values.put(SALESREP_ADDRESS, salesRepList.getSraddress());
        values.put(SALESREP_CONTACT, salesRepList.getSrmoble());
        db.insert(SALESREP_TABLE, null, values);
        return false;
    }

    //*LOCATION TABLE//*
    public boolean storeLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCATION_ID, location.getLocid());
        values.put(LOCATION_NAME, location.getLocname());
        db.insert(LOCATION_TABLE, null, values);
        return false;
    }

    //* UM TABLE //*
    public boolean storeUm(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITM, unit.getItem_id());
        values.put(NAME, unit.getName());
        values.put(QUANTITY, unit.getQuantity());
        values.put(UNIT_ID, unit.getUnit_id());
        db.insert(UNIT_MEASURE_TABLE, null, values);
        return false;
    }

    //*ORDER_ITEM_TABLE//*
    public long storeOrderItem(Item2 item2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_ITEM_ID, item2.getId());
        values.put(ORDER_PRICELEVEL_ID, item2.getPricelvlid());
        values.put(ORDER_ITEM_CODE, item2.getCode());
        values.put(ORDER_ITEM_DESCRIPTION, item2.getDescription());
        values.put(ORDER_ITEM_UOM, item2.getUnitmeasure());
        values.put(ORDER_UNIT_BASE, item2.getUnitbase());
        values.put(ORDER_ITEM_PRICE, item2.getPrice());
        values.put(ORDER_ITEM_QUANTITY, item2.getQuantity());
        values.put(ORDER_ITEM_TOTAL, item2.getTotal());

        long itemId = db.insert(ORDER_ITEM_TABLE, null, values);
        // db.close();
        return itemId;
    }

    public boolean storePT(PaymenTerm paymenTerm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PTERMS_ID, paymenTerm.getId());
        values.put(PTERMS_CODE, paymenTerm.getCode());
        values.put(PTERMS_DESCRIPTION, paymenTerm.getDescription());
        values.put(PTERMS_NET_DUE, paymenTerm.getNetdue());
        db.insert(PAYMENT_TERMS_TABLE, null, values);
        return false;
    }

    public boolean storepriceLevel(NewPriceLvl newPriceLvl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRICE_LEVELID, newPriceLvl.getPid());
        values.put(PRICE_LEVEL_CODE, newPriceLvl.getPcode());
        values.put(PRICE_LEVEL_DESCRIPTION, newPriceLvl.getPdescription());
        db.insert(PRICE_LEVEL_TABLE, null, values);
        return false;
    }

    public boolean storePLVLines(PlevelLines_list plevelLines_list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRICE_LEVEL_LINES_ID, plevelLines_list.getId());
        values.put(PRI_LEVEL_ID, plevelLines_list.getPricelvl_id());
        values.put(PRITEM_ID, plevelLines_list.getItem_id());
        values.put(PRCUSTOM_PRICE, plevelLines_list.getCustomprice());

        long newRowId = db.insert(PRICE_LEVEL_LINES_TABLE, null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            // Insertion successful, return true
            return true;
        } else {
            // Insertion failed, return false
            return false;
        }

//        values.put(PRICE_LEVEL_LINES_ID, plevelLines_list.getId());
//        values.put(PRI_LEVEL_ID, plevelLines_list.getPricelvl_id());
//        values.put(PRITEM_ID, plevelLines_list.getItem_id());
//        values.put(PRCUSTOM_PRICE, plevelLines_list.getCustomprice());
//        db.insert(PRICE_LEVEL_LINES_TABLE, null, values);
//        return false;
    }

    public long inserSO(SALESORDER salesorder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //  values.put(SALES_ORDERID, salesorder.getSalesorderid());
        values.put(CODE, salesorder.getCode());
        values.put(DATE, salesorder.getDate());
        values.put(SOCUSTOMER_ID, salesorder.getCustomerid());
        values.put(SOLOCATION_ID, salesorder.getLocationid());
        values.put(SOSALESREPID, salesorder.getSalesrepid());
        values.put(DATE_NEEDED, salesorder.getDateneeded());
        values.put(PO_NUMBER, salesorder.getPonumber());
        values.put(SHIP_VIA_ID, salesorder.getShipvia());
        values.put(AMOUNT, salesorder.getAmount());
        values.put(NOTES, salesorder.getNotes());
        values.put(CUSTOM_FIELD1, salesorder.getCustom1());
        values.put(CUSTOM_FIELD2, salesorder.getCustom2());
        values.put(CUSTOM_FIELD3, salesorder.getCustom3());
        values.put(CUSTOM_FIELD4, salesorder.getCustom4());
        values.put(CUSTOM_FIELD5, salesorder.getCustom5());
        values.put(BEGIN_ORDER, salesorder.get_begin_order());
        values.put(END_ORDER,salesorder.get_end_order());
        long sioid = db.insert(SALES_ORDER_TABLE, null, values);
        return sioid;

    }

    public long inserITEMSO(SALESORDERITEMS salesorderitems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SALES_ORDERITEMID, salesorderitems.getSalesorderid());
        values.put(SOI_ITEM_ID, salesorderitems.getSoiitemid());
        values.put(SOI_QUANTITY, salesorderitems.getSoiquantity());
        values.put(SOI_UOM, salesorderitems.getUom());
        values.put(SOI_UNIT_BASE_QUANTITY, salesorderitems.getSoiunitbasequantity());
        values.put(SOI_RATE, salesorderitems.getSoirate());
        values.put(SOI_AMOUNT, salesorderitems.getSoiamount());
        values.put(TAXABLE, salesorderitems.getTaxable());
        values.put(TAXABLE_AMOUNT, salesorderitems.getTaxableamount());
        values.put(TAX_AMOUNT, salesorderitems.getTaxamount());
        values.put(SOI_PRICE_LEVEL_ID, salesorderitems.getSoipricelevelid());
        values.put(SOI_CUSTOM_FIELD1, salesorderitems.getSoicustomfield1());
        values.put(SOI_CUSTOM_FIELD2, salesorderitems.getSoicustomfield2());
        values.put(SOI_CUSTOM_FIELD3, salesorderitems.getSoicustomfield3());
        values.put(SOI_CUSTOM_FIELD4, salesorderitems.getSoicustomfield4());
        values.put(SOI_CUSTOM_FIELD5, salesorderitems.getSoicustomfield5());
        long soiitemid = db.insert(SALES_ORDER_ITEMS_TABLE, null, values);
        return soiitemid;

    }
    public void SkipCustomerOrder(SALESORDER salesorder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //  values.put(SALES_ORDERID, salesorder.getSalesorderid());
        Log.d("SkipCustomerId", salesorder.get_end_order());
        values.put(CUSTOMER_SKIP_TABLE_CUSTOMER_ID, salesorder.getCustomerid());
        values.put(CUSTOMER_SKIP_TABLE_DATETIME,salesorder.get_end_order());
        values.put(CUSTOMER_SKIP_TABLE_REASON,salesorder.get_reason());
        db.insert(CUSTOMER_SKIP_TABLE,null,values);
    }

    @SuppressLint("Range")
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        String query = "SELECT " +
                TABLE_NAME + "." + ITEM_CODE + ", " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
                TABLE_NAME + "." + ITEM_RATE + ", " +
                TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
                TABLE_NAME + "." + ITEM_QUANTITY + ", " +
                TABLE_NAME + "." + ITEM_GROUP + ", " +
                UNIT_MEASURE_TABLE + "." + NAME + ", " +
                UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
                UNIT_MEASURE_TABLE + "." + UNIT_ID +
                " FROM " + TABLE_NAME +
                " LEFT JOIN " + UNIT_MEASURE_TABLE +
                " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + ITM+
                " WHERE " + INACTIVE+"=0 GROUP by "+ITEM_CODE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();

//                item.setId(cursor.getString(cursor.getColumnIndex(ITEMID)));
                item.setCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
                item.setRate(cursor.getString(cursor.getColumnIndex(ITEM_RATE)));
                item.setUnitquant(cursor.getString(cursor.getColumnIndex(ITEM_UNIT_MEASURE)));
                item.setQuantity(cursor.getString(cursor.getColumnIndex(ITEM_QUANTITY)));
                item.setGroup(cursor.getString(cursor.getColumnIndex(ITEM_GROUP)));

                Unit unit = new Unit();
                unit.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                unit.setQuantity(cursor.getString(cursor.getColumnIndex(QUANTITY)));
                unit.setUnit_id(cursor.getString(cursor.getColumnIndex(UNIT_ID)));

                item.setUnit(unit);

                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return itemList;
    }

    /////////////////////////////
//    @SuppressLint("Range")
//    public ArrayList<Item> getAllupdatedItems() {
//        ArrayList<Item> itemList = new ArrayList<>();
//        String query = "SELECT " +
//                TABLE_NAME + "." + ITEM_CODE + ", " +
//                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
//                TABLE_NAME + "." + ITEM_RATE + ", " +
//                TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
//                TABLE_NAME + "." + ITEM_QUANTITY + ", " +
//                UNIT_MEASURE_TABLE + "." + NAME + ", " +
//                UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
//                UNIT_MEASURE_TABLE + "." + UNIT_ID + ", " +
//                PRICE_LEVEL_TABLE + "." + PRICE_LEVEL_CODE + ", " +
//
//                PRICE_LEVEL_TABLE + "." + PRICE_LEVEL_DESCRIPTION +
//                " FROM " + TABLE_NAME +
//                " JOIN " + UNIT_MEASURE_TABLE +
//                " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + ITEM_ID +
//                " LEFT JOIN " + PRICE_LEVEL_LINES_TABLE +
//                " ON " + PRICE_LEVEL_LINES_TABLE + "." + PRITEM_ID +
//                " = " + TABLE_NAME + "." + ITEMID +
//                " LEFT JOIN " + PRICE_LEVEL_TABLE +
//                " ON " + PRICE_LEVEL_TABLE + "." + PRICE_LEVELID +
//                " = " + PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                Item item = new Item();
//                item.setCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
//                item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
//                item.setRate(cursor.getString(cursor.getColumnIndex(ITEM_RATE)));
//                item.setUnitquant(cursor.getString(cursor.getColumnIndex(ITEM_UNIT_MEASURE)));
//                item.setQuantity(cursor.getString(cursor.getColumnIndex(ITEM_QUANTITY)));
//
//                Unit unit = new Unit();
//                unit.setName(cursor.getString(cursor.getColumnIndex(NAME)));
//                unit.setQuantity(cursor.getString(cursor.getColumnIndex(QUANTITY)));
//                unit.setUnit_id(cursor.getString(cursor.getColumnIndex(UNIT_ID)));
//
//                NewPriceLvl newPriceLvl = new NewPriceLvl();
//                newPriceLvl.setPcode(cursor.getString(cursor.getColumnIndex(PRICE_LEVEL_CODE)));
//                newPriceLvl.setPdescription(cursor.getString(cursor.getColumnIndex(PRICE_LEVEL_DESCRIPTION)));
//
//                item.setUnit(unit);
//
//                // Check if the price level exists
//                if (newPriceLvl.getPcode() != null) {
//                    item.setNewPriceLvl(newPriceLvl);
//                }
//
//                itemList.add(item);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return itemList;
//    }

///////////////////////////

    //* FETCH DATA FROM CUSTOMER DATABASE TO RECYCLE //*
//    @SuppressLint("Range")
//    public ArrayList<Customer> getAllCustomer() {
//        ArrayList<Customer> customerList = new ArrayList<>();
//        String query = "SELECT * FROM " + CUSTOMER_TABLE;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//
//            do {
//                Customer customer = new Customer();
//                customer.setId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
//                customer.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
//                customer.setPostaladdress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
//                customer.setMobilenumber(cursor.getString(cursor.getColumnIndex(MOBILE_NO)));
//                customer.setContactperson(cursor.getString(cursor.getColumnIndex(CONTACT_PERSON)));
//
//                customerList.add(customer);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return customerList;
//    }

    //fetch data from salesrep table //
    @SuppressLint("Range")
    public ArrayList<SalesRepList> getAllSalesRep() {
        ArrayList<SalesRepList> salesRepList = new ArrayList<>();
        String query = "SELECT * FROM " + SALESREP_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            do {
                SalesRepList salesRepList1 = new SalesRepList();
                salesRepList1.setSrid(cursor.getString(cursor.getColumnIndex(SALESREP_ID)));
                salesRepList1.setSrname(cursor.getString(cursor.getColumnIndex(SALESREP_NAME)));
                salesRepList1.setSraddress(cursor.getString(cursor.getColumnIndex(SALESREP_ADDRESS)));
                salesRepList1.setSrmoble(cursor.getString(cursor.getColumnIndex(SALESREP_CONTACT)));

                salesRepList.add(salesRepList1);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return salesRepList;
    }
    // FETCH DATA FROM LOCATION TABLE //

    @SuppressLint("Range")
    public ArrayList<Location> getAllLocation() {
        ArrayList<Location> locations = new ArrayList<>();
        String query = "SELECT * FROM " + LOCATION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            do {
                Location location = new Location();
                location.setLocid(cursor.getString(cursor.getColumnIndex(LOCATION_ID)));
                location.setLocname(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));

                locations.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return locations;
    }

    //*FETCH DATA IN ORDER TABLE//*
    @SuppressLint("Range")
    public ArrayList<Item2> getAllOrderItem() {
        ArrayList<Item2> item2s = new ArrayList<>();
        String query = "SELECT * FROM " + ORDER_ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            do {
                Item2 item2 = new Item2();
                item2.setCode(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_CODE)));
                item2.setDescription(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_DESCRIPTION)));
                item2.setUnitmeasure(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_UOM)));
                item2.setUnitbase(cursor.getString(cursor.getColumnIndex(ORDER_UNIT_BASE)));
                item2.setPrice(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_PRICE)));
                item2.setQuantity(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_QUANTITY)));
                item2.setTotal(cursor.getDouble(cursor.getColumnIndex(ORDER_ITEM_TOTAL)));

                item2s.add(item2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return item2s;
    }

    @SuppressLint("Range")
    public ArrayList<Customer> getAllCustomer() {
        ArrayList<Customer> customerlist = new ArrayList<>();
        String query = "SELECT " +
                CUSTOMER_TABLE + "." + CUSTOMER_ID + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_NAME + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_ADDRESS + ", " +
                CUSTOMER_TABLE + "." + CONTACT_PERSON + ", " +
                CUSTOMER_TABLE + "." + MOBILE_NO + ", " +
                CUSTOMER_TABLE + "." + TELEPHONE_NO + ", " +
                CUSTOMER_TABLE + "." + PAYMENT_TERMS_ID + ", " +
                CUSTOMER_TABLE + "." + SALES_REP_ID + ", " +
                CUSTOMER_TABLE + "." + PRICE_LEVEL_ID + ", " +

                PAYMENT_TERMS_TABLE + "." + PTERMS_CODE + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_DESCRIPTION + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_NET_DUE +
                " FROM " + CUSTOMER_TABLE +
                " LEFT JOIN " + PAYMENT_TERMS_TABLE +
                " ON " + CUSTOMER_TABLE + "." + PAYMENT_TERMS_ID + " = " + PAYMENT_TERMS_TABLE + "." + PTERMS_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();

                customer.setId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                customer.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                customer.setPostaladdress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                customer.setContactperson(cursor.getString(cursor.getColumnIndex(CONTACT_PERSON)));
                customer.setMobilenumber(cursor.getString(cursor.getColumnIndex(MOBILE_NO)));
                customer.setTelephonenumber(cursor.getString(cursor.getColumnIndex(TELEPHONE_NO)));
                customer.setPaymenttermsid(cursor.getString(cursor.getColumnIndex(PAYMENT_TERMS_ID)));
                customer.setSalesrepid(cursor.getString(cursor.getColumnIndex(SALES_REP_ID)));
                customer.setPricelevelid(cursor.getString(cursor.getColumnIndex(PRICE_LEVEL_ID)));

                PaymenTerm paymenTerm = new PaymenTerm();
                // paymenTerm.setId(cursor.getString(cursor.getColumnIndex(PTERMS_ID)));
                paymenTerm.setCode(cursor.getString(cursor.getColumnIndex(PTERMS_CODE)));
                paymenTerm.setDescription(cursor.getString(cursor.getColumnIndex(PTERMS_DESCRIPTION)));
                paymenTerm.setNetdue(cursor.getString(cursor.getColumnIndex(PTERMS_NET_DUE)));

                customer.setPaymenTerm(paymenTerm);

                customerlist.add(customer);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return customerlist;
    }

    public ArrayList<Customer> getCustomerFromCoveragePlan() {
        ArrayList<Customer> customerlist = new ArrayList<>();
        String query = "SELECT " +
                CUSTOMER_TABLE + "." + CUSTOMER_ID + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_NAME + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_ADDRESS + ", " +
                CUSTOMER_TABLE + "." + CONTACT_PERSON + ", " +
                CUSTOMER_TABLE + "." + MOBILE_NO + ", " +
                CUSTOMER_TABLE + "." + TELEPHONE_NO + ", " +
                CUSTOMER_TABLE + "." + PAYMENT_TERMS_ID + ", " +
                CUSTOMER_TABLE + "." + SALES_REP_ID + ", " +
                CUSTOMER_TABLE + "." + PRICE_LEVEL_ID + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_CODE + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_DESCRIPTION + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_NET_DUE +
                " FROM " + CUSTOMER_TABLE +
                " INNER JOIN CUSTOMER_COVERAGE_PLAN ON CUSTOMER_COVERAGE_PLAN.CUSTOMER_ID = CUSTOMER_TABLE.CUSTOMER_ID "
                +"AND CUSTOMER_COVERAGE_PLAN.SALESREP_ID=(SELECT SALESREP_ID FROM SALES_REP_TABLE WHERE salesrep_name =(SELECT VALUE FROM SYSTEM_SETTINGS WHERE NAME='DEFAULT_SALES_REP_ID')) "
                +"AND CUSTOMER_COVERAGE_PLAN.COVERAGE_DAY = (SELECT  strftime('%w', date('now')))"
                +" LEFT JOIN " + PAYMENT_TERMS_TABLE +
                "  ON " + CUSTOMER_TABLE + "." + PAYMENT_TERMS_ID + " = " + PAYMENT_TERMS_TABLE + "." + PTERMS_ID
                +" GROUP BY "+CUSTOMER_TABLE + "." + CUSTOMER_ID

                +" UNION ALL "

                +"SELECT " +
                CUSTOMER_TABLE + "." + CUSTOMER_ID + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_NAME + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_ADDRESS + ", " +
                CUSTOMER_TABLE + "." + CONTACT_PERSON + ", " +
                CUSTOMER_TABLE + "." + MOBILE_NO + ", " +
                CUSTOMER_TABLE + "." + TELEPHONE_NO + ", " +
                CUSTOMER_TABLE + "." + PAYMENT_TERMS_ID + ", " +
                CUSTOMER_TABLE + "." + SALES_REP_ID + ", " +
                CUSTOMER_TABLE + "." + PRICE_LEVEL_ID + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_CODE + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_DESCRIPTION + ", " +
                PAYMENT_TERMS_TABLE + "." + PTERMS_NET_DUE +
                " FROM " + CUSTOMER_TABLE
                +" LEFT JOIN " + PAYMENT_TERMS_TABLE +
                " ON " + CUSTOMER_TABLE + "." + PAYMENT_TERMS_ID + " = " + PAYMENT_TERMS_TABLE + "." + PTERMS_ID
                +" WHERE CUSTOMER_TABLE"+"."+"CUSTOMER_ID"+"="+"1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();

                customer.setId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                customer.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                customer.setPostaladdress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                customer.setContactperson(cursor.getString(cursor.getColumnIndex(CONTACT_PERSON)));
                customer.setMobilenumber(cursor.getString(cursor.getColumnIndex(MOBILE_NO)));
                customer.setTelephonenumber(cursor.getString(cursor.getColumnIndex(TELEPHONE_NO)));
                customer.setPaymenttermsid(cursor.getString(cursor.getColumnIndex(PAYMENT_TERMS_ID)));
                customer.setSalesrepid(cursor.getString(cursor.getColumnIndex(SALES_REP_ID)));
                customer.setPricelevelid(cursor.getString(cursor.getColumnIndex(PRICE_LEVEL_ID)));

                PaymenTerm paymenTerm = new PaymenTerm();
                // paymenTerm.setId(cursor.getString(cursor.getColumnIndex(PTERMS_ID)));
                paymenTerm.setCode(cursor.getString(cursor.getColumnIndex(PTERMS_CODE)));
                paymenTerm.setDescription(cursor.getString(cursor.getColumnIndex(PTERMS_DESCRIPTION)));
                paymenTerm.setNetdue(cursor.getString(cursor.getColumnIndex(PTERMS_NET_DUE)));

                customer.setPaymenTerm(paymenTerm);

                customerlist.add(customer);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return customerlist;
    }

    //Select Item that has Price Level//
    @SuppressLint("Range")
    public ArrayList<Item> combinedata() {
        ArrayList<Item> itemList = new ArrayList<>();
        String query = "SELECT " +
                TABLE_NAME + "." + ITEM_CODE + ", " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
                TABLE_NAME + "." + ITEM_RATE + ", " +
                TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
                TABLE_NAME + "." + ITEM_QUANTITY + ", " +
                TABLE_NAME + "." + ITEM_GROUP + ", " +
                UNIT_MEASURE_TABLE + "." + NAME + ", " +
                UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
                UNIT_MEASURE_TABLE + "." + UNIT_ID + ", " +
                "CASE WHEN " + PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID + " IS NOT NULL " +
                "THEN " + PRICE_LEVEL_LINES_TABLE + "." + PRCUSTOM_PRICE + " ELSE " + TABLE_NAME + "." + ITEM_RATE + " END AS PRICE " +
                "FROM " + TABLE_NAME +
                " JOIN " + UNIT_MEASURE_TABLE +
                " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + ITM +
                " LEFT JOIN " + PRICE_LEVEL_LINES_TABLE +
                " ON " + TABLE_NAME + "." + ITEMID + " = " + PRICE_LEVEL_LINES_TABLE + "." + PRITEM_ID +
                " WHERE " + PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID + " IS NULL OR " +
                PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID + " = 1" +
                " AND INACTIVE=0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
                item.setRate(cursor.getString(cursor.getColumnIndex("PRICE")));
                item.setUnitquant(cursor.getString(cursor.getColumnIndex(ITEM_UNIT_MEASURE)));
                item.setQuantity(cursor.getString(cursor.getColumnIndex(ITEM_QUANTITY)));
                item.setGroup(cursor.getString(cursor.getColumnIndex(ITEM_GROUP)));
                Unit unit = new Unit();

                unit.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                unit.setQuantity(cursor.getString(cursor.getColumnIndex(QUANTITY)));
                unit.setUnit_id(cursor.getString(cursor.getColumnIndex(UNIT_ID)));

                item.setUnit(unit);

                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return itemList;
    }

    /// Get Data from OrderTable
    public List<Item2> getItemDataOrder() {
        List<Item2> orderItemList = new ArrayList<>();
        String query =
                "SELECT " +
                        ORDER_ITEM_TABLE + "." + ORDER_ITEM_ID + ", " +
                        ORDER_ITEM_TABLE + "." + ORDER_ITEM_QUANTITY + ", " +
                        ORDER_ITEM_TABLE + "." + ORDER_UNIT_BASE + ", " +
                        ORDER_ITEM_TABLE + "." + ORDER_ITEM_UOM + ", " +
                        ORDER_ITEM_TABLE + "." + ORDER_ITEM_PRICE + ", " +
                        ORDER_ITEM_TABLE + "." + ORDER_ITEM_TOTAL + ", " +
                        ORDER_ITEM_TABLE + "." + ORDER_PRICELEVEL_ID +
                        " FROM " + ORDER_ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            do {
                Item2 item2 = new Item2();
                item2.setId(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_ID)));
                item2.setQuantity(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_QUANTITY)));
                item2.setUnitbase(cursor.getString(cursor.getColumnIndex(ORDER_UNIT_BASE)));
                item2.setUnitmeasure(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_UOM)));
                item2.setPrice(cursor.getString(cursor.getColumnIndex(ORDER_ITEM_PRICE)));
                item2.setTotal(cursor.getDouble(cursor.getColumnIndex(ORDER_ITEM_TOTAL)));
                item2.setPricelvlid(cursor.getString(cursor.getColumnIndex(ORDER_PRICELEVEL_ID)));

                orderItemList.add(item2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderItemList;
    }

    //* DELETE All ITEM DATA SYNC //*
    public void deleteExistingData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
//        db.close();
    }

    //* DELETE All CUSTOMER DATA SYNC //*
    public void deleteCustomerData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CUSTOMER_TABLE, null, null);
//        db.close();
    }

    //* DELETE All SALESREP DATA SYNC //*s
    public void deleteSalerepDate() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SALESREP_TABLE, null, null);
//        db.close();
    }

    // DELETE SYNC LOCATION //
    public void deleteLocation() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LOCATION_TABLE, null, null);
//        db.close();
    }

    public void deleteItem(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Order_Item_Table", "order_item_code = ?", new String[]{code});
//        db.close();

    }

    public void deleteexistUm() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(UNIT_MEASURE_TABLE, null, null);
//        db.close();
    }

    public void deletePT() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PAYMENT_TERMS_TABLE, null, null);
//        db.close();
    }

    public void deleteplines() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PRICE_LEVEL_LINES_TABLE, null, null);
//        db.close();

    }

    public void deletepricelevel() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PRICE_LEVEL_TABLE, null, null);
//        db.close();
    }

    // delete lsit order
    public void deleOrderSample() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ORDER_ITEM_TABLE, null, null);
//        db.close();
    }

    public void deletanan() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SALES_ORDER_TABLE, null, null);
//        db.close();
    }




    public void DeletealltotBOOK() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DASHBOARD_TABLE, null, null);
//        db.close();
    }

//    public void deleteSpecific() {
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(SALES_ORDER_TABLE, WHERE)
//    }


    // total Order item//
    public double getTotalPayable() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + ORDER_ITEM_TOTAL + ") FROM " + ORDER_ITEM_TABLE, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
//        db.close();
        return total;
    }


    public double getBookTotal(String date) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + AMOUNT + " FROM " + SALES_ORDER_TABLE+" WHERE "+ SALES_ORDER_TABLE+"."+ DATE+"=STRFTIME('%d/%m/%Y','"+date+
                "') ", null);

        while (cursor.moveToNext()) {
            String amountString = cursor.getString(cursor.getColumnIndex(AMOUNT));

            String cleanedAmountString = amountString.replace(",", "");

            try {

                double amount = Double.parseDouble(cleanedAmountString);
                total += amount;
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }

        cursor.close();
//        db.close();
        return total;
    }

//    //DashBoard
//    public double DashTot() {
//        double total = 0;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT " + D_TOTAL + " FROM " + DASHBOARD_TABLE, null);
//
//        while (cursor.moveToNext()) {
//            String amountString = cursor.getString(cursor.getColumnIndex(D_TOTAL));
//
//            String cleanedAmountString = amountString.replace(",", "");
//
//            try {
//                double amount = Double.parseDouble(cleanedAmountString);
//                total += amount;
//            } catch (NumberFormatException e) {
//
//                e.printStackTrace();
//            }
//        }
//
//        cursor.close();
//        db.close();
//        return total;
//    }

    public int countData(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(" + SOCUSTOMER_ID + ") FROM " + SALES_ORDER_TABLE+" WHERE "+ SALES_ORDER_TABLE+"."+ DATE+"=STRFTIME('%d/%m/%Y','"+date+
                "') ", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
//        db.close();
        return count;
    }

    public long insertDataIDAndITEMSO() {
        SQLiteDatabase db = this.getWritableDatabase();
        long maxSalesOrderID = 0;
        String maxSalesOrderIDQuery = "SELECT MAX(" + SALES_ORDERID + ") FROM " + SALES_ORDER_TABLE;
        Cursor cursor = db.rawQuery(maxSalesOrderIDQuery, null);
        if (cursor.moveToFirst()) {
            maxSalesOrderID = cursor.getLong(0);
        }
        cursor.close();
        String insertQuery = "INSERT INTO " + SALES_ORDER_ITEMS_TABLE + " (" +
                SALES_ORDERITEMID + ", " +
                SOI_ITEM_ID + ", " +
                SOI_QUANTITY + ", " +
                SOI_UNIT_BASE_QUANTITY + ", " +
                SOI_UOM + ", " +
                SOI_RATE + ", " +
                SOI_AMOUNT + ", " +
                SOI_PRICE_LEVEL_ID + ", " +
                SOI_CUSTOM_FIELD1 + ", " +
                SOI_CUSTOM_FIELD2 + ", " +
                SOI_CUSTOM_FIELD3 + ", " +
                SOI_CUSTOM_FIELD4 + ", " +
                SOI_CUSTOM_FIELD5 + ") " +
                "SELECT " +
                maxSalesOrderID + ", " +
                ORDER_ITEM_ID + ", " +
                ORDER_ITEM_QUANTITY + ", " +
                ORDER_UNIT_BASE + ", " +
                ORDER_ITEM_UOM + ", " +
                ORDER_ITEM_PRICE + ", " +
                ORDER_ITEM_TOTAL + ", " +
                ORDER_PRICELEVEL_ID + ", " +
                "NULL, NULL, NULL, NULL, NULL " +
                "FROM " + ORDER_ITEM_TABLE;

        String setNullQuery = "UPDATE " + SALES_ORDER_ITEMS_TABLE +
                " SET " +
                SOI_CUSTOM_FIELD1 + " = NULL, " +
                SOI_CUSTOM_FIELD2 + " = NULL, " +
                SOI_CUSTOM_FIELD3 + " = NULL, " +
                SOI_CUSTOM_FIELD4 + " = NULL, " +
                SOI_CUSTOM_FIELD5 + " = NULL " +
                "WHERE " +
                SOI_CUSTOM_FIELD1 + " = '' AND " +
                SOI_CUSTOM_FIELD2 + " = '' AND " +
                SOI_CUSTOM_FIELD3 + " = '' AND " +
                SOI_CUSTOM_FIELD4 + " = '' AND " +
                SOI_CUSTOM_FIELD5 + " = ''";

        db.beginTransaction();
        try {

            db.execSQL(insertQuery);
            db.execSQL(setNullQuery);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        // Return the number of rows inserted (optional)
        return DatabaseUtils.queryNumEntries(db, SALES_ORDER_ITEMS_TABLE);
    }

    public int update_so_status(int so_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE SALES_ORDER_TABLE SET STATUS = 1 where SALES_ORDERID = "+so_id;
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
        return 0;
    }

    public int update_customer_skip_status(int customer_skip_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE CUSTOMER_SKIP_TABLE SET STATUS = 1 where ID = "+customer_skip_id;
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
        return 0;
    }

    public int update_customer_coordinates(int id,String longitude,String latitude){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE CUSTOMER_TABLE SET LONGITUDE='"+longitude+"'," +
                "LATITUDE='"+latitude+"', PIN_FLAG=1 where CUSTOMER_ID = "+id;
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
        return 0;
    }
    public int update_customer_pin_flag(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE CUSTOMER_TABLE SET PIN_FLAG=0 where CUSTOMER_ID = "+id;
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
        return 0;
    }
    public int update_so_posted_flag(int so_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE SALES_ORDER_TABLE SET POSTED = 1 where SALES_ORDERID = "+so_id;
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
        return 0;
    }

    public String get_customer_longitude(int id){
        String longitude ="";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT IFNULL(LONGITUDE,'0') FROM CUSTOMER_TABLE WHERE CUSTOMER_ID="+id;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                longitude = cursor.getString(0);
            }
            cursor.close();
//            db.close();
            return longitude;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return longitude;
    }

    public String get_customer_latitude(int id){
        String latitude ="";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT IFNULL(LATITUDE,'0') FROM CUSTOMER_TABLE WHERE CUSTOMER_ID="+id;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                latitude = cursor.getString(0);
            }
            cursor.close();
//            db.close();
            return latitude;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return latitude;
    }

    public String get_verify_pin(int id){
        String verify_pin ="";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT VERIFY FROM CUSTOMER_TABLE WHERE CUSTOMER_ID="+id;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                verify_pin = cursor.getString(0);
            }
            cursor.close();
//            db.close();
            return verify_pin;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return verify_pin;
    }

    public int get_customer_pin_flag(){
        int customer_id =0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT CUSTOMER_ID FROM CUSTOMER_TABLE WHERE PIN_FLAG =1 LIMIT 1";
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                customer_id = cursor.getInt(0);
            }
            cursor.close();
//            db.close();
            return customer_id;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return customer_id;
    }

    public int get_unsynced_skipped_orders(){
        int customer_skip_id =0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT ID FROM CUSTOMER_SKIP_TABLE WHERE STATUS =0 LIMIT 1";
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                customer_skip_id = cursor.getInt(0);
            }
            cursor.close();
//            db.close();
            return customer_skip_id;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return customer_skip_id;
    }

    public int get_so_status(int so_id){
        int status =0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT STATUS FROM SALES_ORDER_TABLE WHERE SALES_ORDERID="+so_id;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                status = cursor.getInt(0);
            }
            cursor.close();
//            db.close();
            return status;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return status;
    }

    public int get_so_posted_flag(int so_id){
        int status =0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT POSTED FROM SALES_ORDER_TABLE WHERE SALES_ORDERID="+so_id;
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                status = cursor.getInt(0);
            }
            cursor.close();
//            db.close();
            return status;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return status;
    }

// INSERT DATA INTO DASHBOARD TABLE

    public long insertDataIntoDashboard() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + DASHBOARD_TABLE + " (" +
                D_CODE + ", " +
                D_CUSTOMER_NAME + ", " +
                D_TOTAL + ", " +
                D_DATE + " ) " +
                "SELECT " +
                SALES_ORDER_TABLE + "." + CODE + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_NAME + ", " +
                SALES_ORDER_TABLE + "." + AMOUNT + ", " +
                SALES_ORDER_TABLE + "." + DATE +
                " FROM " + SALES_ORDER_TABLE +
                " JOIN " + CUSTOMER_TABLE +
                " ON " + SALES_ORDER_TABLE + "." + SOCUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
                " ORDER BY " + SALES_ORDER_TABLE + "." + CODE + " ASC";

        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return DatabaseUtils.queryNumEntries(db, DASHBOARD_TABLE);
    }


//    public long insetDataDash() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = " INSERT INTO " + DASHBOARD_TABLE + " (" +
//                D_CODE + ", " +
//                D_CUSTOMER_NAME + ", " +
//                D_TOTAL + ", " +
//                D_DATE + " ) " +
//                "SELECT " +
//                SALES_ORDER_TABLE + "." + CODE + ", " +
//                SALES_ORDER_TABLE + "." + AMOUNT + ", " +
//                SALES_ORDER_TABLE + "." + DATE + ", " +
//                CUSTOMER_TABLE + "." + CUSTOMER_NAME +
//                " FROM " + SALES_ORDER_TABLE +
//                " JOIN " + CUSTOMER_TABLE +
//                " ON " + SALES_ORDER_TABLE + "." + SOCUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
//                " ORDER BY " + CODE + " ASC";
//
//        db.beginTransaction();
//        try {
//            db.execSQL(query);
//            db.setTransactionSuccessful();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
//        return DatabaseUtils.queryNumEntries(db, DASHBOARD_TABLE);
//
//    }
    //GET ORDER TO HISTORY
    @SuppressLint("Range")
    public ArrayList<SALESORDER> getSlsorder(int sales_order_id) {
        ArrayList<SALESORDER> salesORlist = new ArrayList<>();
        String query ="";
        if(sales_order_id == 0) {
                query = " SELECT " +
                    SALES_ORDER_TABLE + "." + SALES_ORDERID + ", " +
                    SALES_ORDER_TABLE + "." + CODE + ", " +
                    SALES_ORDER_TABLE + "." + AMOUNT + ", " +
                    SALES_ORDER_TABLE + "." + DATE + ", " +
                    SALES_ORDER_TABLE + "." + SALES_REP_ID + ", " +
                    SALES_ORDER_TABLE + "." + LOCATION_ID + ", " +
                    SALES_ORDER_TABLE + "." + BEGIN_ORDER + ", " +
                    SALES_ORDER_TABLE + "." + END_ORDER + ", " +
                    CUSTOMER_TABLE + "." + CUSTOMER_ID + ", " +
                    SALESREP_TABLE + "." + SALESREP_NAME + ", " +
                    CUSTOMER_TABLE + "." + CUSTOMER_NAME +
                    " FROM " + SALES_ORDER_TABLE +
                    " INNER JOIN " + SALESREP_TABLE +
                    " ON " + SALESREP_TABLE + "." + SALESREP_ID + " = " + SALES_ORDER_TABLE + "." + SALES_REP_ID +
                    " INNER JOIN " + CUSTOMER_TABLE +
                    " ON " + SALES_ORDER_TABLE + "." + CUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
                    " ORDER BY " + SALES_ORDERID + " ASC";
        }
        else{
            query = " SELECT " +
                    SALES_ORDER_TABLE + "." + SALES_ORDERID + ", " +
                    SALES_ORDER_TABLE + "." + CODE + ", " +
                    SALES_ORDER_TABLE + "." + AMOUNT + ", " +
                    SALES_ORDER_TABLE + "." + DATE + ", " +
                    SALES_ORDER_TABLE + "." + SALES_REP_ID + ", " +
                    SALES_ORDER_TABLE + "." + LOCATION_ID + ", " +
                    SALES_ORDER_TABLE + "." + BEGIN_ORDER + ", " +
                    SALES_ORDER_TABLE + "." + END_ORDER + ", " +
                    CUSTOMER_TABLE + "." + CUSTOMER_ID + ", " +
                    SALESREP_TABLE + "." + SALESREP_NAME + ", " +
                    CUSTOMER_TABLE + "." + CUSTOMER_NAME +
                    " FROM " + SALES_ORDER_TABLE +
                    " INNER JOIN " + SALESREP_TABLE +
                    " ON " + SALESREP_TABLE + "." + SALESREP_ID + " = " + SALES_ORDER_TABLE + "." + SALES_REP_ID +
                    " INNER JOIN " + CUSTOMER_TABLE +
                    " ON " + SALES_ORDER_TABLE + "." + CUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
                    " WHERE "+ SALES_ORDER_TABLE+"."+ SALES_ORDERID+"="+sales_order_id+
                    " ORDER BY " + SALES_ORDERID + " ASC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SALESORDER salesorder = new SALESORDER();
                salesorder.setSalesorderid(cursor.getInt(cursor.getColumnIndex(SALES_ORDERID)));
                salesorder.setCode(cursor.getString(cursor.getColumnIndex(CODE)));
                salesorder.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                salesorder.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                salesorder.setSalesrepid(cursor.getInt(cursor.getColumnIndex(SALES_REP_ID)));

                Customer customerR = new Customer();
                customerR.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                customerR.setId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));

                Location location = new Location();
                location.setLocid(cursor.getString(cursor.getColumnIndex(LOCATION_ID)));

                salesorder.setLocationid(Integer.parseInt(location.getLocid()));
                salesorder.set_sales_rep_name(cursor.getString(cursor.getColumnIndex(SALESREP_NAME)));
                salesorder.set_customer_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                salesorder.set_begin_order(cursor.getString(cursor.getColumnIndex(BEGIN_ORDER)));
                salesorder.set_end_order(cursor.getString(cursor.getColumnIndex(END_ORDER)));

                salesorder.setCustomer(customerR);
                salesORlist.add(salesorder);
            } while (cursor.moveToNext());
        }
//        cursor.close();
//        db.close();
        return salesORlist;
        }

    public ArrayList<SALESORDER> history_getSlsorder(String date) {
        ArrayList<SALESORDER> salesORlist = new ArrayList<>();
        String query ="";
            query = " SELECT " +
                    SALES_ORDER_TABLE + "." + SALES_ORDERID + ", " +
                    SALES_ORDER_TABLE + "." + CODE + ", " +
                    SALES_ORDER_TABLE + "." + AMOUNT + ", " +
                    SALES_ORDER_TABLE + "." + DATE + ", " +
                    SALES_ORDER_TABLE + "." + SALES_REP_ID + ", " +
                    SALES_ORDER_TABLE + "." + LOCATION_ID + ", " +
                    SALES_ORDER_TABLE + "." + BEGIN_ORDER + ", " +
                    SALES_ORDER_TABLE + "." + END_ORDER + ", " +
                    CUSTOMER_TABLE + "." + CUSTOMER_ID + ", " +
                    SALESREP_TABLE + "." + SALESREP_NAME + ", " +
                    CUSTOMER_TABLE + "." + CUSTOMER_NAME +
                    " FROM " + SALES_ORDER_TABLE +
                    " INNER JOIN " + SALESREP_TABLE +
                    " ON " + SALESREP_TABLE + "." + SALESREP_ID + " = " + SALES_ORDER_TABLE + "." + SALES_REP_ID +
                    " INNER JOIN " + CUSTOMER_TABLE +
                    " ON " + SALES_ORDER_TABLE + "." + CUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
                    " WHERE "+ SALES_ORDER_TABLE+"."+ DATE+"=STRFTIME('%d/%m/%Y','"+date+
                    "') ORDER BY " + SALES_ORDERID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SALESORDER salesorder = new SALESORDER();
                salesorder.setSalesorderid(cursor.getInt(cursor.getColumnIndex(SALES_ORDERID)));
                salesorder.setCode(cursor.getString(cursor.getColumnIndex(CODE)));
                salesorder.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                salesorder.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                salesorder.setSalesrepid(cursor.getInt(cursor.getColumnIndex(SALES_REP_ID)));

                Customer customerR = new Customer();
                customerR.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                customerR.setId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));

                Location location = new Location();
                location.setLocid(cursor.getString(cursor.getColumnIndex(LOCATION_ID)));

                salesorder.setLocationid(Integer.parseInt(location.getLocid()));
                salesorder.set_sales_rep_name(cursor.getString(cursor.getColumnIndex(SALESREP_NAME)));
                salesorder.set_customer_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                salesorder.set_begin_order(cursor.getString(cursor.getColumnIndex(BEGIN_ORDER)));
                salesorder.set_end_order(cursor.getString(cursor.getColumnIndex(END_ORDER)));

                salesorder.setCustomer(customerR);
                salesORlist.add(salesorder);
            } while (cursor.moveToNext());
        }
//        cursor.close();
//        db.close();
        return salesORlist;
    }

        @SuppressLint("Range")
        public ArrayList<SALESORDER> get_customer_skip_order(int id) {
            ArrayList<SALESORDER> salesORlist = new ArrayList<>();
            String query ="";
            query = " SELECT " +
                    CUSTOMER_SKIP_TABLE + "." + CUSTOMER_SKIP_TABLE_ID + ", " +
                    CUSTOMER_SKIP_TABLE + "." + CUSTOMER_SKIP_TABLE_REASON + ", " +
                    CUSTOMER_SKIP_TABLE + "." + CUSTOMER_SKIP_TABLE_DATETIME + ", " +
                    CUSTOMER_SKIP_TABLE + "." + CUSTOMER_SKIP_TABLE_DATETIME + ", " +
                    CUSTOMER_SKIP_TABLE + "." + CUSTOMER_SKIP_TABLE_CUSTOMER_ID +
                    " FROM " + CUSTOMER_SKIP_TABLE +
                    " INNER JOIN " + CUSTOMER_TABLE +
                    " ON " + CUSTOMER_SKIP_TABLE + "." + CUSTOMER_SKIP_TABLE_CUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
                    " WHERE "+ CUSTOMER_SKIP_TABLE+"."+ CUSTOMER_SKIP_TABLE_ID+"="+id+
                    " ORDER BY " + CUSTOMER_SKIP_TABLE_ID + " ASC";


            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SALESORDER salesorder = new SALESORDER();
                    salesorder.setCustomerid(cursor.getInt(cursor.getColumnIndex(CUSTOMER_SKIP_TABLE_CUSTOMER_ID)));
                    salesorder.set_reason(cursor.getString(cursor.getColumnIndex(CUSTOMER_SKIP_TABLE_REASON)));
                    salesorder.set_end_order(cursor.getString(cursor.getColumnIndex(CUSTOMER_SKIP_TABLE_DATETIME)));

                    salesORlist.add(salesorder);
                } while (cursor.moveToNext());
            }
    //        cursor.close();
    //        db.close();
            return salesORlist;
        }

        @SuppressLint("Range")
        public ArrayList<SALESORDERITEMS> getSlsorderitems(Integer so_id) {
            ArrayList<SALESORDERITEMS> salesORlist = new ArrayList<>();
//            Log.v(TAG, "index=" + Code);
            String query = " SELECT " +
                    TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_ITEM_ID + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_QUANTITY + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_RATE + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_AMOUNT + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_UNIT_BASE_QUANTITY + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_PRICE_LEVEL_ID + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_UOM +
                    " FROM " + SALES_ORDER_ITEMS_TABLE +
                    " INNER JOIN " + TABLE_NAME +
                    " ON " + TABLE_NAME + "." + ITEMID + " = " + SALES_ORDER_ITEMS_TABLE + "." + SOI_ITEM_ID +
                    " INNER JOIN " + SALES_ORDER_TABLE +
                    " ON " + SALES_ORDER_TABLE + "." + SALES_ORDERID + " = " + SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID +
                    " WHERE " + SALES_ORDER_ITEMS_TABLE+ "."+ SALES_ORDERITEMID +"=" + so_id+
                    " ORDER BY " + CODE + " ASC";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SALESORDERITEMS salesorderitems = new SALESORDERITEMS();
//                    salesorderitems.setSalesorderid(cursor.getInt(cursor.getColumnIndex(SALES_ORDERID)));
                    salesorderitems.setSoiitemid(cursor.getInt(cursor.getColumnIndex(SOI_ITEM_ID)));
                    salesorderitems.setSoiquantity(cursor.getInt(cursor.getColumnIndex(SOI_QUANTITY)));
                    salesorderitems.setSoirate(cursor.getDouble(cursor.getColumnIndex(SOI_RATE)));
                    salesorderitems.setSoiamount(cursor.getDouble(cursor.getColumnIndex(SOI_AMOUNT)));
                    salesorderitems.setSoiunitbasequantity(cursor.getInt(cursor.getColumnIndex(SOI_UNIT_BASE_QUANTITY)));
                    salesorderitems.setSoipricelevelid(cursor.getDouble(cursor.getColumnIndex(SOI_PRICE_LEVEL_ID)));
                    Unit uom = new Unit();
                    uom.setName(cursor.getString(cursor.getColumnIndex(SOI_UOM)));
                    salesorderitems.setUom(uom.getName());
                    Item item = new Item();
                    item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
                    salesorderitems.setitemdesc(item.getDescription());
                    salesORlist.add(salesorderitems);
                } while (cursor.moveToNext());
            }
//            cursor.close();
//            db.close();
            return salesORlist;
        }

    @SuppressLint("Range")
    public ArrayList<CONNECT> SelectUPDT() {
        ArrayList<CONNECT> connectlist = new ArrayList<>();

        String query = "SELECT " +
                CONNECTION_TABLE + "." + CONNECTION_IP +
                " FROM " + CONNECTION_TABLE +
                " WHERE " + "defaultconn" + " = '1'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String cip = cursor.getString(cursor.getColumnIndex(CONNECTION_IP));

                CONNECT connect = new CONNECT();
                connect.setIp(cip);
                connectlist.add(connect);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return connectlist;
    }

    public String sales_type(){
        String sales_type="";
        String query ="SELECT "+ "SYSTEM_SETTINGS"+"."+SYSTEM_SETTINGS_VALUE +" FROM "+ SYSTEM_SETTINGS +
                " WHERE "+ SYSTEM_SETTINGS_NAME +"= 'SALES_TYPE'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
                sales_type=cursor.getString(cursor.getColumnIndex(SYSTEM_SETTINGS_VALUE));
                Log.d("sales_type",sales_type);
        }
        return sales_type;
    }

    public int get_open_sales_order(){
        int sales_order_id = 0;
        String query ="SELECT "+SALES_ORDERID +" FROM "+ SALES_ORDER_TABLE +
                " WHERE STATUS = 0 LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            sales_order_id=cursor.getInt(cursor.getColumnIndex(SALES_ORDERID));
            Log.d("sales_order_id", Integer.toString(sales_order_id));
        }
        return sales_order_id;
    }

    @SuppressLint("Range")
    public ArrayList<SALESORDER> get_principal_performance(String date){
        ArrayList<SALESORDER> pr = new ArrayList<>();
        String query="SELECT ITEM_GROUP,printf('%,d',ROUND(SUM(SALES_ORDER_ITEMS_TABLE.AMOUNT),2)) AMOUNT,COUNT(DISTINCT(CUSTOMER_ID)) BUYING_ACCTS,printf('%,d',ROUND(SUM(SALES_ORDER_ITEMS_TABLE.AMOUNT)/COUNT(DISTINCT(CUSTOMER_ID)),2)) DROP_SIZE,ROUND(SUM(SALES_ORDER_ITEMS_TABLE.AMOUNT)) AMOUNTNUMBER FROM Sales_Order_Items_Table " +
                "INNER JOIN ITEM_TABLE ON ITEM_TABLE.item_id = SALES_ORDER_ITEMS_TABLE.item_id " +
                "INNER JOIN SALES_ORDER_TABLE ON SALES_ORDER_TABLE.SALES_ORDERID = SALES_ORDER_ITEMS_TABLE.SALES_ORDER_ID " +
                " WHERE SALES_ORDER_TABLE.DATE=STRFTIME('%d/%m/%Y','"+date+
                "') GROUP BY ITEM_GROUP ORDER BY AMOUNTNUMBER DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,  null);
        if(cursor.moveToFirst()){
            do{
                SALESORDER so = new SALESORDER();
                so.set_item_group(cursor.getString(0));
                so.setAmount(cursor.getString(1));
                so.setBuying_accts(cursor.getString(2));
                so.setDrop_size(cursor.getString(3));
                pr.add(so);
                Log.d("principal_result", cursor.getString(0));
                Log.d("principal_result", cursor.getString(1));
                Log.d("principal_result", cursor.getString(2));
                Log.d("principal_result", cursor.getString(3));
            }while(cursor.moveToNext());
        }
        Log.d("principal_performance", pr.toString());
        return pr;
    }

    public void UpdateSyncHistory(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE sync_history SET date = DATETIME('now','localtime') where ID ="+ id.toString();
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
    }

    public void VerifyPin(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE CUSTOMER_TABLE SET verify = 1,PIN_FLAG=1 where CUSTOMER_ID ="+ id.toString();
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
    }

    public String get_sync_history(Integer id){
        String value="";
        String query ="SELECT DATE FROM SYNC_HISTORY WHERE  id ="+id.toString();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(cursor.getColumnIndex(SYNC_HISTORY_TABLE_DATE));
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_active_connection(){
        String value="";
        String query ="SELECT conection_name FROM CONNECTION_TABLE WHERE  defaultconn=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_active_salestype(){
        String value="";
        String query ="SELECT VALUE FROM SYSTEM_SETTINGS WHERE  id=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_default_salesrep(){
        String value="";
        String query ="SELECT ifnull(VALUE,'') VALUE FROM SYSTEM_SETTINGS WHERE  id=2";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_coverage_type(){
        String value="";
        String query ="SELECT ifnull(VALUE,'') VALUE FROM SYSTEM_SETTINGS WHERE  id=4";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_bluetooth_device(){
        String value="";
        String query ="SELECT ifnull(VALUE,'') VALUE FROM SYSTEM_SETTINGS WHERE  id=5";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_verify_type(){
        String value="";
        String query ="SELECT ifnull(VALUE,'') VALUE FROM SYSTEM_SETTINGS WHERE  id=3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_default_salesrep_id(){
        String value="";
        String query ="SELECT ifnull(SALES_REP_TABLE.salesrep_id,'') salesrep_id FROM SYSTEM_SETTINGS INNER JOIN SALES_REP_TABLE ON SALES_REP_TABLE.salesrep_name = SYSTEM_SETTINGS.VALUE WHERE  id=2";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public Integer get_customer_verification(Integer id){
        Log.d("customer_id", id.toString());
        Integer value=0;
        String query ="SELECT verify FROM customer_table where customer_id ="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getInt(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public Integer check_customer_skip(Integer id){
        Log.d("customer_id", id.toString());
        Integer value=0;
        String query ="select customer_id from customer_skip_table where date(datetime) = date('now') and customer_id ="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getInt(0);
//            Log.d("sales_type",sales_type);
        }
        return value;
    }

    public String get_max_sync_history(){

        String value="";
        int difference = 0;
        String query ="SELECT ifnull(julianday(date('now'))- julianday(date(strftime('%Y-%m-%d',min(date) ))),2) mindate,tabe_name FROM sync_history";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getString(1);
            difference=cursor.getInt(0);
        }
        if(difference >1) {
            return value;
        }
        else{
            return "";
        }
    }

    public boolean request_repin(Integer customer_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            //  values.put(SALES_ORDERID, salesorder.getSalesorderid());
            values.put(REQUEST_REPIN_CUSTOMER_ID, customer_id);
            values.put(REQUEST_REPIN_DATE, datenow.toString());
            values.put(REQUEST_REPIN_STATUS, "0");
            db.insert(REQUEST_REPIN_TABLE, null, values);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public Integer count_request_repin(Integer customer_id){
        Integer value=0;

        String query ="SELECT ifnull(count(id),0) FROM request_repin_table where date = date('now') and customer_id ="+customer_id.toString();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            value=cursor.getInt(0);
        }

        return value;
    }

    public int get_unsynced_request_repin(){
        int customer_repin_id =0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT ID FROM REQUEST_REPIN_TABLE WHERE STATUS =0 LIMIT 1";
            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                customer_repin_id = cursor.getInt(0);
            }
            cursor.close();
//            db.close();
            return customer_repin_id;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return customer_repin_id;
    }

    public ArrayList<SALESORDER> get_repin_customer(int id) {
        Log.d("repin_id", Integer.toString(id));
        ArrayList<SALESORDER> salesORlist = new ArrayList<>();
        String query ="";
        query = " SELECT " +
                REQUEST_REPIN_TABLE + "." + REQUEST_REPIN_CUSTOMER_ID + ", " +
                REQUEST_REPIN_TABLE + "." + REQUEST_REPIN_DATE +
                " FROM " + REQUEST_REPIN_TABLE +
                " WHERE "+ REQUEST_REPIN_TABLE+"."+ REQUEST_REPIN_ID+"="+id+
                " ORDER BY " + REQUEST_REPIN_ID + " ASC LIMIT 1";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SALESORDER salesorder = new SALESORDER();
                salesorder.setCustomerid(cursor.getInt(cursor.getColumnIndex(REQUEST_REPIN_CUSTOMER_ID)));
                salesorder.set_end_order(cursor.getString(cursor.getColumnIndex(REQUEST_REPIN_DATE)));

                salesORlist.add(salesorder);
            } while (cursor.moveToNext());
        }
        //        cursor.close();
        //        db.close();
        return salesORlist;
    }

    public int update_customer_repin_status(int customer_skip_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String update_query = "UPDATE REQUEST_REPIN_TABLE SET STATUS = 1 where date = date('now') and ID = "+customer_skip_id+"";
        try{
            db.execSQL(update_query);
            //db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.endTransaction();
        }
        return 0;
    }

    }



