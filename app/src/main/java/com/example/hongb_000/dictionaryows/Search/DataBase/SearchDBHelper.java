package com.example.hongb_000.dictionaryows.Search.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hongb_000.dictionaryows.Search.HandingStrings.HandingStrings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hongb_000 on 7/27/2015.
 */
public class SearchDBHelper extends SQLiteOpenHelper {
    private static final String DB_PATH = "/data/data/com.example.hongb_000.dictionaryows/databases/";

    private String DB_NAME;
    private String TABLE_NAME;

    private final Context myContext;

    public SearchDBHelper(Context context, String nameDB, String nameTABLE) {
        super(context, nameDB, null, 1);
        this.myContext = context;
        this.DB_NAME = nameDB;
        this.TABLE_NAME = nameTABLE;
    }

    public void createDB() throws IOException {
        boolean isDB = checkDB();

        if(!isDB){
            this.getReadableDatabase();
            copyDataBase();
        }
    }

    private boolean checkDB(){
        SQLiteDatabase mDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            e.printStackTrace();
        }

        if(mDB != null){
            mDB.close();
        }

        return mDB != null ? true : false;
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while (( length = (myInput.read(buffer))) > 0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public Cursor getInfoFast(String input, String nameTableDB){
        SQLiteDatabase SQL = this.getReadableDatabase();
        Cursor result = null;
        String sql = "SELECT " + nameTableDB + ".rowid AS _id, word, content from " + nameTableDB
                + ", main_content WHERE word MATCH '" + input + "*' AND " + nameTableDB + ".rowid = main_content._id"
                + "  LIMIT 50;";

        try{
            result = SQL.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //result.moveToFirst();
        return result;
    }


    public Cursor getInfoBySearchKanji(String ws, String conlumName){
        SQLiteDatabase myDB = this.getReadableDatabase();
        ws = HandingStrings.newStringToSearchKanji(ws);
        Cursor result;
        try{
            result = myDB.rawQuery("Select * from " + TABLE_NAME + " where " + conlumName + " in " + ws , null);
        }catch (Exception e) {
            result = null;
        }

        result.moveToFirst();

        return result;
    }

    public Cursor getInfoBySearchHan_Viet (String ws, String columname){
        SQLiteDatabase SQL = this.getReadableDatabase();
        Cursor result = null;

        try{
            result = SQL.rawQuery("Select * from " + TABLE_NAME + " where " + columname + " like \"" +ws.toUpperCase() + "%\"", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        result.moveToFirst();
        return result;
    }

    public Cursor getWord (String ws, String columname) {
        SQLiteDatabase SQL = this.getReadableDatabase();
        Cursor result = null;

        try{
            result = SQL.rawQuery("Select * from " + TABLE_NAME + " where " + columname + " like \"" +ws.toUpperCase() + "%\"", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        result.moveToFirst();
        return result;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
