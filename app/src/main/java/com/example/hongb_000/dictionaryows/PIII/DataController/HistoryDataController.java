package com.example.hongb_000.dictionaryows.PIII.DataController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by phamn on 8/26/2015.
 */
public class HistoryDataController {
    private final int MaxHis = 100;
    private final String tableName = "history";

    private SQLiteDatabase sqLiteDatabase;

    public HistoryDataController(Context context) {
        sqLiteDatabase = context.openOrCreateDatabase("HistoryTest", Context.MODE_PRIVATE, null);
        try {
            String sqlCreatTable = "CREATE TABLE " + tableName + " ( \n" +
                    "id int,\n" +
                    "ngaykiemtra date,\n" +
                    "trinhdo varchar[10],\n" +
                    "cauhoi int,\n" +
                    "diem int\n" +
                    " )";
            sqLiteDatabase.execSQL(sqlCreatTable);
        } catch (Exception e) {

        }
    }

    public void pushData(String N, int numberQuetion, int diem) {

        int id;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());

        if (getsize() > MaxHis) {
            deleteFist();
        }

        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst() == false)
            id = 1;
        else {
            cursor.moveToLast();
            int idMax = Integer.valueOf(cursor.getString(0));
            id = idMax + 1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("ngaykiemtra", date);
        contentValues.put("trinhdo", N);
        contentValues.put("cauhoi", numberQuetion);
        contentValues.put("diem", diem);
        sqLiteDatabase.insert(tableName, null, contentValues);
    }

    private int getsize() {
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        return cursor.getColumnCount();
    }

    private void deleteFist() {
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        String idMin = cursor.getString(0);
        sqLiteDatabase.delete(tableName, "id=?", new String[]{idMin});

    }

    public ArrayList<HistoryData> getData() {
        ArrayList<HistoryData> list = new ArrayList<HistoryData>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            HistoryData data = new HistoryData(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            list.add(data);
            cursor.moveToNext();
        }
        return list;
    }
}
