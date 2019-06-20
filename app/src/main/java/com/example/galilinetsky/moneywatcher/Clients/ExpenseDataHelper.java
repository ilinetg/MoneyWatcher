package com.example.galilinetsky.moneywatcher.Clients;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpenseDataHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Expenses.db";
    public static final String TABLE_NAME = "Expenses_talbe";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "PRODUCT";
    public static final String COL_5 = "VALUE";

    public ExpenseDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertData(String date, String category, String product,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,date);
        values.put(COL_3,category);
        values.put(COL_4,product);
        values.put(COL_5,value);
        long result = db.insert(TABLE_NAME,null,values);
        if(result == -1){
            return false;
        }
        else
            return true;
    }

    public boolean updateData(String id, String date, String category, String product, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,date);
        values.put(COL_3,category);
        values.put(COL_4,product);
        values.put(COL_5,value);
        int result = db.update(TABLE_NAME,values,"ID = ?",new String []{id});
        return true;
    }

    public Integer delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String []{id});
    }

    public StringBuffer getCurrentData(){
        Cursor res = this.getAllData();
        res.moveToLast();
        StringBuffer buffer = new StringBuffer();
        //while(res.moveToNext()){
        //buffer.append("id "+ res.getString(0) + "\n");
        buffer.append("date: "+ res.getString(1) + "\n");
        buffer.append("category: "+ res.getString(2) + "\n");
        buffer.append("product: "+ res.getString(3) + "\n");
        buffer.append("value: "+ res.getString(4) + "\n\n");
        //    }
        return buffer;
    }

    public Cursor getByCategory(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] fields = new String[] { "ID", "DATE", "CATEGORY","PRODUCT","VALUE"};
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where CATEGORY = '" +category +"'", null);
        return c;
    }

    public Cursor getByDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] fields = new String[] { "ID", "DATE", "CATEGORY","PRODUCT","VALUE"};
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where DATE = '" + date + "'", null);
        return c;
    }

    public Cursor getById(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] fields = new String[] { "ID", "DATE", "CATEGORY","PRODUCT","VALUE"};
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where ID = " +id, null);
        return c;
    }

    private Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME,null);
        return res;
    }
    public float getValues(String categoryType){
        float sum = 0;
        Cursor cursor = this.getByCategory(categoryType);
        while(cursor.moveToNext()){
            String value = cursor.getString(4);
            sum += Float.parseFloat(value);
        }
        return sum;
    }

    public float getValuesByMonth(String categoryType,String month){
        float sum = 0;
        if(month == null){
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String todayString = formatter.format(todayDate);
            String[] tmp = todayString.split("/");
            month = tmp[1];
        }
        Cursor cursor = this.getByCategory(categoryType);
        while (cursor.moveToNext()){
            String date = cursor.getString(1);
            String[] tmpDate = date.split("/");
            if(Integer.parseInt(tmpDate[1]) == Integer.parseInt(month)){
                String value = cursor.getString(4);
                sum += Float.parseFloat(value);
            }
        }
        return sum;
    }

    public float getDaily(String date){
        float sum = 0;
        Cursor cursor = this.getByDate(date);
        while(cursor.moveToNext()){
            String value = cursor.getString(4);
            sum += Float.parseFloat(value);
        }
        return sum;
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_1 + " INTEGER PRIMARY KEY," +
                    COL_2 + " TEXT," +
                    COL_3 + " TEXT," +
                    COL_4 + " TEXT," +
                    COL_5 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

}