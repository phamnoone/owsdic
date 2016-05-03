package com.example.hongb_000.dictionaryows.Search;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.R;

/**
 * Created by hongb_000 on 7/28/2015.
 */
public class DisplayMeanKanjiActivity extends AppCompatActivity{
    private String mKanji;
    private String mHanviet;
    private String mRadical;
    private String mOnread;
    private String mKunread;
    private int mJPLT;

    private TextView mTVKanji;
    private TextView mTVHanviet;
    private TextView mTVRadical;
    private TextView mTVOnread;
    private TextView mTVKunread;
    private TextView mTVJPLT;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_mean_kanji_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mKanji = intent.getStringExtra("kanji");
        mHanviet = intent.getStringExtra("hanviet");
        mRadical = intent.getStringExtra("radical");
        mOnread = intent.getStringExtra("onread");
        mKunread = intent.getStringExtra("kunread");
        mJPLT = intent.getIntExtra("jplt", -1);

        mTVKanji = (TextView) findViewById(R.id.kanji);
        mTVHanviet = (TextView)findViewById(R.id.hanviet);
        mTVRadical = (TextView)findViewById(R.id.radical);
        mTVOnread = (TextView)findViewById(R.id.onread);
        mTVKunread = (TextView)findViewById(R.id.kunread);
        mTVJPLT = (TextView)findViewById(R.id.jplt);

        mTVKanji.setText(mKanji);
        mTVHanviet.setText(mHanviet);
        mTVRadical.setText(mRadical);
        mTVOnread.setText(mOnread);
        mTVKunread.setText(mKunread);

        if (mJPLT == 0) {
            mTVJPLT.setText("Khac");
        } else {
            mTVJPLT.setText(mJPLT+"");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mHanviet);
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
