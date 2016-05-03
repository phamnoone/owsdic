package com.example.hongb_000.dictionaryows.PIII.DataController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by phamn on 8/25/2015.
 */
public class TestDataControler {
    private SQLiteDatabase sqLiteDatabase;


    public TestDataControler(Context context, int numberQuetions) {
        sqLiteDatabase = context.openOrCreateDatabase("testvalue", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS testvalue");
        String sqlCreatTable = "CREATE TABLE testvalue ( id INTEGER , value INTEGER )";
        sqLiteDatabase.execSQL(sqlCreatTable);
        ContentValues values = new ContentValues();
        for (int i = 1; i <= numberQuetions; i++) {
            values.put("id", i);
            values.put("value", 0);
            sqLiteDatabase.insert("testvalue", null, values);
        }
    }

    public TestDataControler(Context context) {
        sqLiteDatabase = context.openOrCreateDatabase("testvalue", Context.MODE_PRIVATE, null);
    }

    public void putAnsxValue(int id, int value) {
        String sqlUpdate = "UPDATE testvalue set value =" + value + " where id =" + id;
        sqLiteDatabase.execSQL(sqlUpdate);
    }

    public void deleteData(Context context) {
        context.deleteDatabase("testvalue");
    }

    public ArrayList<Integer> getData() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Cursor cursor = sqLiteDatabase.query("testvalue", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int value = Integer.valueOf(cursor.getString(1));
            list.add(value);
            cursor.moveToNext();
        }
        return list;
    }

    public void close() {
        sqLiteDatabase.close();
    }
}
