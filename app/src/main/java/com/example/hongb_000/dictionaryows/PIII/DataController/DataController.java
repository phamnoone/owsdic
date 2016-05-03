package com.example.hongb_000.dictionaryows.PIII.DataController;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 15/07/2015.
 */
public class DataController extends SQLiteOpenHelper {

    SQLiteDatabase database;
    private static String dbname = "TestData.db";
    private static String dbLink = "data/data/com.example.hongb_000.dictionaryows/databases/";
    Context mContext;
    DecryptData decryptData = new DecryptData();
    private void moveFile() throws IOException {
        InputStream myInput = mContext.getAssets().open(dbname);
        String outFileName = dbLink + dbname;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private void openDatabase() {
        database = SQLiteDatabase.openDatabase(dbLink + dbname, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public DataController(Context context) {
        super(context, dbname, null, 1);
        this.getReadableDatabase();
        mContext = context;
        try {
            moveFile();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        openDatabase();
    }

    public ArrayList<TestKanj> getQuetions(String N,String Q) {
        ArrayList<TestKanj> listData = new ArrayList<TestKanj>();
     //   int BQ
        String query = "SELECT * FROM question WHERE N = "+N+" ORDER BY RANDOM() LIMIT "+Q+";";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast()==false) {

                listData.add(new TestKanj(
                        decryptData.decrypt(cursor.getString(2)),
                        decryptData.decrypt(cursor.getString(3)).split(","),
                                 Integer.valueOf(cursor.getString(4))
                        ));
            cursor.moveToNext();
        }
        return listData;
    }


    // hai cai ham nay ke me no nhe
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
