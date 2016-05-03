package com.example.hongb_000.dictionaryows.Search.Tabs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.DataBase.DataBase;
import com.example.hongb_000.dictionaryows.Search.DataBase.SearchDBHelper;
import com.example.hongb_000.dictionaryows.Search.DisplayMeanKanjiActivity;

import java.io.IOException;

/**
 * Created by hongb_000 on 7/21/2015.
 */
public class TabSearchKanji extends Fragment{

    private ListView mLVListKanjiHanViet;
    private ListView mLVListKanji;

    public Cursor mCursorSearchByHan_Viet;
    public Cursor mCursorSearchByKanji;
    public SearchDBHelper myDB;
    private View mView;

    public TabSearchKanji(){}



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.tab_search_kanji, container, false);

            myDB = new SearchDBHelper(getActivity(), DataBase.DB_NAME_KANJI, DataBase.DB_TABLE_KANJI);
            mLVListKanji = (ListView) mView.findViewById(R.id.list_kanji);
            mLVListKanjiHanViet = (ListView) mView.findViewById(R.id.list_kanji_HanViet);

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

            mLVListKanji.setOnItemClickListener(new OnItemClickListener());
            mLVListKanjiHanViet.setOnItemClickListener(new OnItemClickListener());

        }

        return mView;
    }

    public void displayListKanji(String kanjiSearch) {
        new DisplayListKanjiTask(kanjiSearch).execute();
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

            Intent intent = new Intent(getActivity(), DisplayMeanKanjiActivity.class);
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

        public DisplayListKanjiTask (String query) {
            this.query = query;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mCursorSearchByHan_Viet = myDB.getInfoBySearchHan_Viet(query, DataBase.DB_COLUMN_HAN_VIET);
            mCursorSearchByKanji = myDB.getInfoBySearchKanji(query, DataBase.DB_COLUMN_KANJI);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mLVListKanjiHanViet.setAdapter(new WordCursorAdapter(getActivity(), mCursorSearchByHan_Viet));
            mLVListKanji.setAdapter(new WordCursorAdapter(getActivity(), mCursorSearchByKanji));
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
            View rowView = getActivity().getLayoutInflater().inflate(R.layout.one_kanji_layout, null);
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
}
