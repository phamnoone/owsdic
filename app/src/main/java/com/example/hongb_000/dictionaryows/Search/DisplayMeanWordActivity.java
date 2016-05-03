package com.example.hongb_000.dictionaryows.Search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.DataBase.DataBase;
import com.example.hongb_000.dictionaryows.Search.DataBase.SearchDBHelper;
import com.example.hongb_000.dictionaryows.Search.HandingStrings.HandingStrings;
import com.example.hongb_000.dictionaryows.Search.LockableScrollView.LockableScrollView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.io.IOException;

/**
 * Created by hongb_000 on 7/28/2015.
 */
public class DisplayMeanWordActivity extends AppCompatActivity {
    private String mWord;
    private String mContent;
    private TextView mTVword;
    private TextView mTVcontent;
    private ListView mLVKanji;
    private LinearLayout mLNContentWord;

    public Cursor mCurosrKanji;
    public SearchDBHelper myDB;

    private String mListKanji;

    private Toolbar mToolbar;

    private FloatingActionButton actionButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_mean_word_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mTVword = (TextView)findViewById(R.id.word);
        mTVcontent = (TextView)findViewById(R.id.content);
        mLVKanji = (ListView) findViewById(R.id.list_view_kanji);
        mLNContentWord = (LinearLayout) findViewById(R.id.content_word);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mWord = intent.getStringExtra("word");
        mContent = intent.getStringExtra("content");

        mTVword.setText(mWord);
        System.out.println("word " + mContent);
        CharSequence html = HandingStrings.trim(Html.fromHtml(mContent));
        //System.out.println("spaned " + html);
        mTVcontent.setText(html);


        myDB = new SearchDBHelper(DisplayMeanWordActivity.this, DataBase.DB_NAME_KANJI, DataBase.DB_TABLE_KANJI);
        try {
            myDB.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            myDB.openDataBase();
        }catch (SQLException e) {
            throw e;
        }

        mListKanji = HandingStrings.convertStringtoKanji(mContent);

        ImageView icon = new ImageView(this);
        icon.setBackgroundResource(R.mipmap.kanji);
        icon.setPadding(0, 0, 0, 0);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        if (mListKanji == ""){
            actionButton.setVisibility(View.INVISIBLE);
        } else {
            mCurosrKanji = myDB.getInfoBySearchKanji(mListKanji, DataBase.DB_COLUMN_KANJI);
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLVKanji.setAdapter(new WordCursorAdapter(DisplayMeanWordActivity.this, mCurosrKanji));

                mLVKanji.setCacheColorHint(Color.TRANSPARENT);

                ((LockableScrollView)findViewById(R.id.scrollView)).setScrollingEnabled(false);

                mLNContentWord.setBackgroundColor(getResources().getColor(R.color.list_view_display));

            }
        });

        mLNContentWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLVKanji.setAdapter(null);

                mLNContentWord.setVisibility(View.VISIBLE);

                mLNContentWord.setBackgroundColor(Color.parseColor("#fff2fff8"));

                ((LockableScrollView)findViewById(R.id.scrollView)).setScrollingEnabled(true);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mWord);

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

    private class WordCursorAdapter extends CursorAdapter {

        public WordCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup viewGroup) {
            return getLayoutInflater().inflate(R.layout.one_kanji_layout, null);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            final String kanji = cursor.getString(cursor.getColumnIndex("kanji"));
            final String hanviet = cursor.getString(cursor.getColumnIndex("hanviet"));
            final String radical = cursor.getString(cursor.getColumnIndex("radical"));
            final String onread = cursor.getString(cursor.getColumnIndex("onread"));
            final String kunread = cursor.getString(cursor.getColumnIndex("kunread_meaning"));
            final int jplt = cursor.getInt(cursor.getColumnIndex("jlpt"));
            TextView tvKanji = (TextView) view.findViewById(R.id.text_view_kanji_item);
            TextView tvHanviet = (TextView) view.findViewById(R.id.hanviet);
            TextView tvKunread = (TextView) view.findViewById(R.id.kunread);
            tvKanji.setText(kanji);
            tvHanviet.setText(hanviet);
            tvKunread.setText(kunread);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DisplayMeanWordActivity.this, DisplayMeanKanjiActivity.class);
                    intent.putExtra("kanji", kanji);
                    intent.putExtra("hanviet", hanviet);
                    intent.putExtra("radical", radical);
                    intent.putExtra("onread", onread);
                    intent.putExtra("kunread", kunread);
                    intent.putExtra("jplt", jplt);
                    startActivity(intent);
                }
            });
        }
    }
}
