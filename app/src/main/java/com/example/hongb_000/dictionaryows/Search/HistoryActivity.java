package com.example.hongb_000.dictionaryows.Search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.PIV.DataSetting;
import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.DataBase.HistoryDBHelper;

/**
 * Created by hongb_000 on 7/28/2015.
 */
public class HistoryActivity extends AppCompatActivity{
    private Cursor mCursorHistory;
    private ListView mLVHistoty;
    public HistoryDBHelper mHistoryDBHelper;
    private Toolbar mToolbar;
    private final int MAX_ELEMENT_HISTORY_DB = 1000;
    private int GET_ELMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHistoryDBHelper = new HistoryDBHelper(this);
        mLVHistoty = (ListView) findViewById(R.id.list_history);

        mCursorHistory = mHistoryDBHelper.getHistory();

        int size = mCursorHistory.getCount();
        GET_ELMENT = (new DataSetting(this)).getMaxHistory();

        if (size > MAX_ELEMENT_HISTORY_DB) {
            while (size > GET_ELMENT) {
                mHistoryDBHelper.deleteHistory();
                size--;
            }
        }

        mCursorHistory = mHistoryDBHelper.getHistory();

        mLVHistoty.setAdapter(new WordCursorAdapter(HistoryActivity.this, mCursorHistory));

        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class WordCursorAdapter extends CursorAdapter {

        public WordCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup viewGroup) {

            return getLayoutInflater().inflate(R.layout.one_word_layout, null);
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            TextView tv = (TextView) view.findViewById(R.id.text_view_word_item);
            final String word;
            final String content;
            word = cursor.getString(cursor.getColumnIndex("word"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            tv.setText(word);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoryActivity.this, DisplayMeanWordActivity.class);
                    intent.putExtra("word", word);
                    intent.putExtra("content", content);
                    startActivity(intent);
                }
            });
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
}
