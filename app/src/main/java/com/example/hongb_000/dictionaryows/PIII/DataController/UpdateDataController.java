package com.example.hongb_000.dictionaryows.PIII.DataController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by phamn on 8/28/2015.
 */
public class UpdateDataController {

    private static final String dbName = "HistoryTest";
    private static final String tableName = "UpdateData";

    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public UpdateDataController(Context context) {
        this.context = context;
        sqLiteDatabase = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        try {
            String sqlCreatTable = "CREATE TABLE " + tableName + " ( \n" +
                    "id int,\n" +
                    "name varchar[255],\n" +
                    "link varchar[255] ,\n" +
                    "note varchar[255],\n" +
                    "nQuetions int\n" +
                    " )";
            sqLiteDatabase.execSQL(sqlCreatTable);
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", 1);
            contentValues.put("name", "TEST N1->N5");
            contentValues.put("link", "default");
            contentValues.put("note", "Dữ liệu mặc định của app");
            contentValues.put("nQuetions", 3885);
            sqLiteDatabase.insert(tableName, null, contentValues);
        } catch (Exception e) {

        }
    }

    public ArrayList<UpdateData> getUpdateDataSqlite() {
        ArrayList<UpdateData> list = new ArrayList<UpdateData>();

        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            UpdateData updateData = new UpdateData(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    true
            );
            list.add(updateData);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<UpdateData> getUpdateData(GetDataFirebase getDataFirebase) {
        final ArrayList<UpdateData> list = new ArrayList<UpdateData>();
        LoadDataFromFirebase loadDataFromFirebase = new LoadDataFromFirebase(context, getDataFirebase);
        return list;
    }


}
