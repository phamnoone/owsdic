package com.example.hongb_000.dictionaryows.Search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.MainActivity;
import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.DataBase.DataBase;
import com.example.hongb_000.dictionaryows.Search.DataBase.SearchDBHelper;

import java.io.IOException;

/**
 * Created by hongb on 8/20/2015.
 */
public class DisplayKanjiFromCameraActivity extends AppCompatActivity {
    private Cursor mCursorSearchByKanji;
    private SearchDBHelper myDB;
    private ListView mLVListKanji;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_kanji_from_camera_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myDB = new SearchDBHelper(this, DataBase.DB_NAME_KANJI, DataBase.DB_TABLE_KANJI);
        mLVListKanji = (ListView) findViewById(R.id.list_view);

        try {
            myDB.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            myDB.openDataBase();
        } catch (SQLException e) {
            throw e;
        }

        Intent intent = getIntent();
        String queryFromCamera = intent.getStringExtra("text");

        new DisplayListKanjiTask(queryFromCamera).execute();
        mLVListKanji.setOnItemClickListener(new OnItemClickListener());

        getSupportActionBar().setTitle(queryFromCamera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            final String kanji = cursor.getString(cursor.getColumnIndex("kanji"));
            final String hanviet = cursor.getString(cursor.getColumnIndex("hanviet"));
            final String radical = cursor.getString(cursor.getColumnIndex("radical"));
            final String onread = cursor.getString(cursor.getColumnIndex("onread"));
            final String kunread = cursor.getString(cursor.getColumnIndex("kunread_meaning"));
            final int jplt = cursor.getInt(cursor.getColumnIndex("jlpt"));

            Intent intent = new Intent(DisplayKanjiFromCameraActivity.this, DisplayMeanKanjiActivity.class);
            intent.putExtra("kanji", kanji);
            intent.putExtra("hanviet", hanviet);
            intent.putExtra("radical", radical);
            intent.putExtra("onread", onread);
            intent.putExtra("kunread", kunread);
            intent.putExtra("jplt", jplt);
            startActivity(intent);
        }
    }

    private class DisplayListKanjiTask extends AsyncTask<Void, Void, Void> {

        private String query;

        public DisplayListKanjiTask(String query) {
            this.query = query;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mCursorSearchByKanji = myDB.getInfoBySearchKanji(query, DataBase.DB_COLUMN_KANJI);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mLVListKanji.setAdapter(new WordCursorAdapter(DisplayKanjiFromCameraActivity.this, mCursorSearchByKanji));
        }
    }

    private class WordCursorAdapter extends CursorAdapter {

        class ViewHolder {
            public TextView mTVKanji;
            public TextView mTVHanViet;
            public TextView mTVKunread;
        }

        public WordCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup viewGroup) {
            View rowView = getLayoutInflater().inflate(R.layout.one_kanji_layout, null);
            ViewHolder holder = new ViewHolder();
            holder.mTVKanji = (TextView) rowView.findViewById(R.id.text_view_kanji_item);
            holder.mTVHanViet = (TextView) rowView.findViewById(R.id.hanviet);
            holder.mTVKunread = (TextView) rowView.findViewById(R.id.kunread);
            rowView.setTag(holder);
            return rowView;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            final String kanji = cursor.getString(cursor.getColumnIndex("kanji"));
            final String hanviet = cursor.getString(cursor.getColumnIndex("hanviet"));
            final String radical = cursor.getString(cursor.getColumnIndex("radical"));
            final String onread = cursor.getString(cursor.getColumnIndex("onread"));
            final String kunread = cursor.getString(cursor.getColumnIndex("kunread_meaning"));
            final int jplt = cursor.getInt(cursor.getColumnIndex("jlpt"));

            ViewHolder holder = (ViewHolder) view.getTag();
            holder.mTVKanji.setText(kanji);
            holder.mTVHanViet.setText(hanviet);
            holder.mTVKunread.setText(kunread);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextAv = new Intent(DisplayKanjiFromCameraActivity.this, MainActivity.class);
        nextAv.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nextAv);
    }
}
