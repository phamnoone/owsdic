package com.example.hongb_000.dictionaryows.PIV;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by phamn on 7/22/2015.
 */
public class ImportDataController {
    String Url;
    private final String[] ColumName = {"_id", "word", "content"};
    private final String Tablename = "japanese_vietnamese";
    private static String dbLink = "data/data/com.example.hongb_000.dictionaryows/databases/";
    Activity activity;

    public ImportDataController(Activity activity) {
        this.activity = activity;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setButon(Button buton) {
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(intent, "Select program"), 1);
            }
        });
    }

    private boolean CheckTable(SQLiteDatabase database) {
        StringBuffer sql = new StringBuffer();
        Cursor cursor;
        sql.append("SELECT ");
        for (String temp : ColumName) {
            sql.append(temp);
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ");
        sql.append("FROM ");
        sql.append(Tablename);
        try {
            cursor = database.rawQuery(sql.toString(), null);
        } catch (Exception e) {
            return false;
        }
        if (cursor.getCount() == 0)
            return false;
        return true;
    }


    public boolean CheckImprotData() {
        SQLiteDatabase database;
        if (Url == null)
            return false;
        try {
            database = SQLiteDatabase.openDatabase(Url, null, 0);
        } catch (SQLiteCantOpenDatabaseException e) {
            System.out.println("Mo database " + e.toString());
            return false;
        }
        return CheckTable(database);
    }

    public void MoveFileToData() throws IOException {
        String FileName = Url.split("/")[Url.split("/").length - 1];
        System.out.println(FileName);

        InputStream myInput = new FileInputStream(Url);

        File file = new File(dbLink + FileName);
        System.out.println(dbLink + FileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream myOutput = new FileOutputStream(dbLink + FileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        System.out.println("copy     ok");
    }

}
