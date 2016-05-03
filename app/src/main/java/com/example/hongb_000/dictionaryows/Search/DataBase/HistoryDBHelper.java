package com.example.hongb_000.dictionaryows.Search.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hongb_000.dictionaryows.PIV.DataSetting;

/**
 * Created by hongb_000 on 7/27/2015.
 */
public class HistoryDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    private static  int GET_ELEMENT = 0;

    public String CREATE_QUERY = "CREATE TABLE " + DataBase.DB_TABLE_HISTORY + " ( " + DataBase.DB_HISTORY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DataBase.DB_HISTORY_WORD + " TEXT, " + DataBase.DB_HISTORY_CONTENT + " TEXT);";
    public HistoryDBHelper(Context context) {
        super(context, DataBase.DB_TABLE_HISTORY, null, DB_VERSION);
        GET_ELEMENT = (new DataSetting(context)).getMaxHistory();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    public void putHistory(HistoryDBHelper dbHelper, String word, String content){
        SQLiteDatabase mSQL = dbHelper.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(DataBase.DB_HISTORY_WORD, word);
        CV.put(DataBase.DB_HISTORY_CONTENT, content);
        mSQL.insert(DataBase.DB_TABLE_HISTORY, null, CV);
    }

    public Cursor getHistory(){
        SQLiteDatabase SQL = this.getReadableDatabase();
        String query = "Select * from "+ DataBase.DB_TABLE_HISTORY + " ORDER BY "+  DataBase.DB_HISTORY_ID  +" DESC " + " LIMIT " + GET_ELEMENT;
        Cursor result = null;
        try{
            result = SQL.rawQuery(query, null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void deleteHistory(){
        String queryMaxID = DataBase.DB_HISTORY_ID + " = " + "(SELECT MIN("+ DataBase.DB_HISTORY_ID +") FROM " +
                DataBase.DB_TABLE_HISTORY + ")";

        SQLiteDatabase mSQL = this.getWritableDatabase();

        mSQL.delete(DataBase.DB_TABLE_HISTORY, queryMaxID, null);
    }

    public void deleteByID(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.delete(DataBase.DB_TABLE_HISTORY, DataBase.DB_HISTORY_ID + "=" + id, null);
    }

    public int getIDFromHistoryDB (String ws){
        SQLiteDatabase SQL = this.getReadableDatabase();
        String query = "Select * from " + DataBase.DB_TABLE_HISTORY + " where " + DataBase.DB_TABLE_HISTORY + "."
                +DataBase.DB_HISTORY_WORD + " =  \"" + ws + "\"";

        Cursor cursor = null;

        try{
            cursor = SQL.rawQuery(query, null);
        }catch (Exception e){
            e.printStackTrace();
        }

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return 0;
        } else {
            return cursor.getInt(cursor.getColumnIndex(DataBase.DB_HISTORY_ID));
        }
    }

    public int getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + DataBase.DB_TABLE_HISTORY ,null);

        return cursor.getCount();
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
