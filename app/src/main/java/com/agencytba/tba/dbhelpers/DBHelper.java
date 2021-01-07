package com.agencytba.tba.dbhelpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    public static final String TABLE_NAME = "data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BRAND_NAME = "brand_name";
    public static final String COLUMN_PRODUCT_TYPE = "product_type";
    public static final String COLUMN_SKU_NAME = "sku_name";
    public static final String COLUMN_MARKET_NAME = "market_name";
    public static final String COLUMN_COMPANY_NAME = "company_name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TOTAL_SIZE = "total_size";
    public static final String COLUMN_COMPANY_SIZE = "company_size";
    public static final String COLUMN_PERCENTAGE = "percentage";
    public static final String COLUMN_FINISHED = "finished";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MANAGER = "manager";
    public static final String COLUMN_MANAGER2 = "manager2";
    public static final String COLUMN_SUPERVISOR = "supervisor";
    public static final String COLUMN_SUPERVISOR2 = "supervisor2";
    public static final String COLUMN_SUPERVISOR3 = "supervisor3";
    public static final String COLUMN_EXPEDITOR = "expeditor";
    public static final String COLUMN_EXPEDITOR2 = "expeditor2";
    public static final String COLUMN_EXPEDITOR3 = "expeditor3";
    public static final String COLUMN_FIELD_SUPERVISOR = "field_supervisor";
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    String timeStampTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME + " " +
                        "(id integer primary key, brand_name text,product_type text,sku_name text, market_name text, company_name text,status text,total_size text, company_size text, percentage text, manager text, manager2 text, supervisor text, supervisor2 text, supervisor3 text, expeditor text, expeditor2 text, expeditor3 text, field_supervisor text, created_at datetime, finished boolean, type text)"
        );
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
        onCreate(db);
    }

    public boolean insertOSA (String brand_name, String product_type, String sku_name, String market_name, String company_name, String status, String manager, String manager2, String supervisor, String supervisor2, String supervisor3, String expeditor, String expeditor2, String expeditor3, String field_supervisor, boolean finished) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRAND_NAME, brand_name);
        contentValues.put(COLUMN_PRODUCT_TYPE, product_type);
        contentValues.put(COLUMN_SKU_NAME, sku_name);
        contentValues.put(COLUMN_MARKET_NAME, market_name);
        contentValues.put(COLUMN_COMPANY_NAME, company_name);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_MANAGER, manager);
        contentValues.put(COLUMN_MANAGER2, manager2);
        contentValues.put(COLUMN_SUPERVISOR, supervisor);
        contentValues.put(COLUMN_SUPERVISOR2, supervisor2);
        contentValues.put(COLUMN_SUPERVISOR3, supervisor3);
        contentValues.put(COLUMN_EXPEDITOR, expeditor);
        contentValues.put(COLUMN_EXPEDITOR2, expeditor2);
        contentValues.put(COLUMN_EXPEDITOR3, expeditor3);
        contentValues.put(COLUMN_FIELD_SUPERVISOR, field_supervisor);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        contentValues.put(COLUMN_FINISHED, finished);
        contentValues.put(COLUMN_TYPE, "OSA");
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertNPD (String brand_name, String product_type, String sku_name, String market_name, String company_name, String status, String manager, String manager2, String supervisor, String supervisor2, String supervisor3, String expeditor, String expeditor2, String expeditor3, String field_supervisor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRAND_NAME, brand_name);
        contentValues.put(COLUMN_PRODUCT_TYPE, product_type);
        contentValues.put(COLUMN_SKU_NAME, sku_name);
        contentValues.put(COLUMN_MARKET_NAME, market_name);
        contentValues.put(COLUMN_COMPANY_NAME, company_name);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_MANAGER, manager);
        contentValues.put(COLUMN_MANAGER2, manager2);
        contentValues.put(COLUMN_SUPERVISOR, supervisor);
        contentValues.put(COLUMN_SUPERVISOR2, supervisor2);
        contentValues.put(COLUMN_SUPERVISOR3, supervisor3);
        contentValues.put(COLUMN_EXPEDITOR, expeditor);
        contentValues.put(COLUMN_EXPEDITOR2, expeditor2);
        contentValues.put(COLUMN_EXPEDITOR3, expeditor3);
        contentValues.put(COLUMN_FIELD_SUPERVISOR, field_supervisor);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        contentValues.put(COLUMN_TYPE, "NPD");
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertPlanogram (String product_type, String sku_name, String market_name, String company_name, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_TYPE, product_type);
        contentValues.put(COLUMN_SKU_NAME, sku_name);
        contentValues.put(COLUMN_MARKET_NAME, market_name);
        contentValues.put(COLUMN_COMPANY_NAME, company_name);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        contentValues.put(COLUMN_TYPE, "Planogram");
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertSOS (String brand_name, String product_type, String market_name, String company_name, String total_size, String company_size, String percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRAND_NAME, brand_name);
        contentValues.put(COLUMN_PRODUCT_TYPE, product_type);
        contentValues.put(COLUMN_MARKET_NAME, market_name);
        contentValues.put(COLUMN_COMPANY_NAME, company_name);
        contentValues.put(COLUMN_TOTAL_SIZE, total_size);
        contentValues.put(COLUMN_COMPANY_SIZE, company_size);
        contentValues.put(COLUMN_PERCENTAGE, percentage);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        contentValues.put(COLUMN_TYPE, "SOS");
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertSOD (String brand_name, String product_type, String market_name, String company_name, String total_size, String company_size, String percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BRAND_NAME, brand_name);
        contentValues.put(COLUMN_PRODUCT_TYPE, product_type);
        contentValues.put(COLUMN_MARKET_NAME, market_name);
        contentValues.put(COLUMN_COMPANY_NAME, company_name);
        contentValues.put(COLUMN_TOTAL_SIZE, total_size);
        contentValues.put(COLUMN_COMPANY_SIZE, company_size);
        contentValues.put(COLUMN_PERCENTAGE, percentage);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        contentValues.put(COLUMN_TYPE, "SOD");
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id, String market_name, String company_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from " + TABLE_NAME + " where market_name = '" + market_name + "' and company_name = '" + company_name + "' LIMIT " + id + "-1,1", null );
    }

    public int numberOfRows(String market_name, String company_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatus = "SELECT * FROM " + TABLE_NAME + " WHERE (type = 'OSA' OR type = 'NPD' OR type = 'Planogram') AND market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "' GROUP BY sku_name";
        String sqlShares = "SELECT * FROM " + TABLE_NAME + " WHERE (type = 'SOS' OR type = 'SOD') AND market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "' GROUP BY product_type, brand_name";

        /*int statusNCount = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, "type = 'NPD' AND market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "' GROUP BY sku_name");
        int statusPCount = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, "type = 'Planogram' AND market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "' GROUP BY sku_name");
        int shareCount = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, "(type = 'SOS' OR type = 'SOD') AND market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "' GROUP BY product_type, brand_name");
        */
        int allCount = db.rawQuery(sqlStatus, null).getCount() + db.rawQuery(sqlShares, null).getCount();
        db.close();
        return allCount;
    }

    public boolean updateOSA (String brand_name, String product_type, String sku_name, String market_name, String company_name, String status, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        db.update(TABLE_NAME, contentValues, "brand_name = ? AND product_type = ? AND sku_name = ? AND market_name = ? AND company_name = ? AND DATE(created_at) = ? AND type = ? ", new String[] { brand_name, product_type, sku_name, market_name, company_name, timeStamp, type } );
        return true;
    }

    public boolean updateNPD (String brand_name, String product_type, String sku_name, String market_name, String company_name, String status, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        db.update(TABLE_NAME, contentValues, "brand_name = ? AND product_type = ? AND sku_name = ? AND market_name = ? AND company_name = ? AND DATE(created_at) = ? AND type = ? ", new String[] { brand_name, product_type, sku_name, market_name, company_name, timeStamp, type } );
        return true;
    }

    public boolean updatePlanogram (String product_type, String sku_name, String market_name, String company_name, String status, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        db.update(TABLE_NAME, contentValues, "product_type = ? AND sku_name = ? AND market_name = ? AND company_name = ? AND DATE(created_at) = ? AND type = ? ", new String[] { product_type, sku_name, market_name, company_name, timeStamp, type } );
        return true;
    }

    public boolean updateSOS (String brand_name, String product_type, String market_name, String company_name, String company_size, String percentage, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COMPANY_SIZE, company_size);
        contentValues.put(COLUMN_PERCENTAGE, percentage);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        db.update(TABLE_NAME, contentValues, "brand_name = ? AND product_type = ? AND market_name = ? AND company_name = ? AND DATE(created_at) = ? AND type = ? ", new String[] { brand_name, product_type, market_name, company_name, timeStamp, type } );
        return true;
    }

    public boolean updateSOD (String brand_name, String product_type, String market_name, String company_name, String company_size, String percentage, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COMPANY_SIZE, company_size);
        contentValues.put(COLUMN_PERCENTAGE, percentage);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        db.update(TABLE_NAME, contentValues, "brand_name = ? AND product_type = ? AND market_name = ? AND company_name = ? AND DATE(created_at) = ? AND type = ?", new String[] { brand_name, product_type, market_name, company_name, timeStamp, type } );
        return true;
    }

    public boolean updateTotalSize (String product_type, String market_name, String company_name, String total_size, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TOTAL_SIZE, total_size);
        contentValues.put(COLUMN_CREATED_AT, timeStampTime);
        db.update(TABLE_NAME, contentValues, "product_type = ? AND market_name = ? AND company_name = ? AND DATE(created_at) = ? AND type = ? ", new String[] { product_type, market_name, company_name, timeStamp, type } );
        return true;
    }

    public void updateTotalSizeRaw (String product_type, String market_name, String company_name, String total_size, String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET total_size = '" + total_size + "', percentage = ROUND((company_size/total_size)*100) WHERE type = '" + type + "' AND product_type = '" + product_type + "' AND market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "'";

        Cursor c = db.rawQuery(query, null );

        c.moveToFirst();
        c.close();
    }

    public Integer deleteOSA (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllData(String where) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + " where " + where, null );
        res.moveToFirst();

        while(!res.isAfterLast()) {
            if (res.getString(res.getColumnIndex(COLUMN_TYPE)).equals("OSA") || res.getString(res.getColumnIndex(COLUMN_TYPE)).equals("NPD"))
                array_list.add(res.getString(res.getColumnIndex(COLUMN_PRODUCT_TYPE)) + " - " + res.getString(res.getColumnIndex(COLUMN_BRAND_NAME)) + "\n" + res.getString(res.getColumnIndex(COLUMN_SKU_NAME)) + ": " + res.getString(res.getColumnIndex(COLUMN_STATUS)));
            else if (res.getString(res.getColumnIndex(COLUMN_TYPE)).equals("Planogram"))
                array_list.add(res.getString(res.getColumnIndex(COLUMN_PRODUCT_TYPE)) + "\n" + res.getString(res.getColumnIndex(COLUMN_SKU_NAME)) + ": " + res.getString(res.getColumnIndex(COLUMN_STATUS)));
            else
                array_list.add(res.getString(res.getColumnIndex(COLUMN_PRODUCT_TYPE)) + "\n" + res.getString(res.getColumnIndex(COLUMN_BRAND_NAME)) + ": " + "TS: " + res.getString(res.getColumnIndex(COLUMN_TOTAL_SIZE)) + " | CS: " + res.getString(res.getColumnIndex(COLUMN_COMPANY_SIZE)) + " | PER: " + res.getString(res.getColumnIndex(COLUMN_PERCENTAGE)));
            res.moveToNext();
        }
        return array_list;
    }

    public Cursor getStoredData(String market_name, String company_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE market_name = '" + market_name + "' AND company_name = '" + company_name + "' AND DATE(created_at) = '" + timeStamp + "' ORDER BY " + COLUMN_ID + " ASC;";
        return db.rawQuery(sql, null);
    }
}