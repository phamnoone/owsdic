package com.example.hongb_000.dictionaryows.Search.Tabs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.ConvertToHiragana.KanaConverter;
import com.example.hongb_000.dictionaryows.Search.DataBase.DataBase;
import com.example.hongb_000.dictionaryows.Search.DataBase.HistoryDBHelper;
import com.example.hongb_000.dictionaryows.Search.DataBase.SearchDBHelper;
import com.example.hongb_000.dictionaryows.Search.DisplayMeanWordActivity;
import com.example.hongb_000.dictionaryows.Search.HandingStrings.HandingStrings;
import com.example.hongb_000.dictionaryows.Search.Information.WordInfor;
import com.example.hongb_000.dictionaryows.Search.SearchFragment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hongb_000 on 7/21/2015.
 */
public class TabSearchWord extends Fragment{

    private ListView mLVWordJa_Vi;

    private SearchDBHelper mSearchDBHelperJa_ViHira;
    private SearchDBHelper mSearchDBHelperJa_ViKata;
    private SearchDBHelper mSearchDBHelperVi_Ja;

    private HistoryDBHelper mHistoryDBHelper;

    private Cursor mCursorJa_ViHira;
    private Cursor mCursorJa_ViKata;
    private Cursor mCursorVi_Ja;

    private MergeAdapter mAdapter = null;

    public WordCursorAdapter adapterJa_ViHira, adapterJa_ViKata, adapterVi_Ja;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.tab_search_word, container, false);

            mSearchDBHelperJa_ViHira = new SearchDBHelper(getActivity(), DataBase.DB_NAME_JaVi, DataBase.DB_TABLE_JaVi);
            mSearchDBHelperJa_ViKata = new SearchDBHelper(getActivity(), DataBase.DB_NAME_JaVi, DataBase.DB_TABLE_JaVi);
            mSearchDBHelperVi_Ja = new SearchDBHelper(getActivity(), DataBase.DB_NAME_ViJa, DataBase.DB_TABLE_ViJa);
            mHistoryDBHelper = new HistoryDBHelper(getActivity());


            mLVWordJa_Vi = (ListView) mView.findViewById(R.id.listWordJa_Vi);

            try {
                mSearchDBHelperVi_Ja.createDB();
                mSearchDBHelperJa_ViHira.createDB();
                mSearchDBHelperJa_ViKata.createDB();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                mSearchDBHelperJa_ViHira.openDataBase();
                mSearchDBHelperJa_ViKata.openDataBase();
                mSearchDBHelperVi_Ja.openDataBase();
            }catch (SQLException e){
                throw  e;
            }

            mAdapter = new MergeAdapter();



            adapterJa_ViHira = new WordCursorAdapter(getActivity(), mCursorJa_ViHira);
            adapterJa_ViKata = new WordCursorAdapter(getActivity(), mCursorJa_ViKata);
            adapterVi_Ja = new WordCursorAdapter(getActivity(), mCursorVi_Ja);

            mAdapter.addAdapter(adapterJa_ViHira);
            mAdapter.addAdapter(adapterJa_ViKata);
            mAdapter.addAdapter(adapterVi_Ja);

            mLVWordJa_Vi.setAdapter(mAdapter);

            mLVWordJa_Vi.setOnItemClickListener(new OnItemClickListener());

        }

        return mView;
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            String word = cursor.getString(cursor.getColumnIndex(DataBase.DB_COLUMN_WORD));
            String content = cursor.getString(cursor.getColumnIndex(DataBase.DB_COLUMN_CONTENT));

            content = HandingStrings.setHTMLforText(content);

            int idWord;

            if (mHistoryDBHelper.getAll() == 0) {
                mHistoryDBHelper.putHistory(mHistoryDBHelper, word, content);
            } else {
                if ( (idWord = mHistoryDBHelper.getIDFromHistoryDB(word)) != 0) {

                    mHistoryDBHelper.deleteByID(idWord);
                }

                mHistoryDBHelper.putHistory(mHistoryDBHelper, word, content);
            }

            Intent intent = new Intent(getActivity(), DisplayMeanWordActivity.class);
            intent.putExtra("word", word);
            intent.putExtra("content", content);
            startActivity(intent);        }
    }

    public void displayListWords (String wordSearch) {

        new DisplayListWordTask(wordSearch).execute();

    }

    public class DisplayListWordTask extends AsyncTask<Void, Void, Void> {

        private String wordSearch;

        public DisplayListWordTask (String query) {
            this.wordSearch = query;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mCursorJa_ViHira = mSearchDBHelperJa_ViHira.getInfoFast(KanaConverter.toHiragana(wordSearch), DataBase.DB_TABLE_JaVi);

            mCursorJa_ViKata = mSearchDBHelperJa_ViKata.getInfoFast(KanaConverter.toKatakana(wordSearch), DataBase.DB_TABLE_JaVi);

            mCursorVi_Ja = mSearchDBHelperVi_Ja.getInfoFast(wordSearch, DataBase.DB_TABLE_ViJa);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapterJa_ViHira.swapCursor(mCursorJa_ViHira);
            adapterJa_ViKata.swapCursor(mCursorJa_ViKata);
            adapterVi_Ja.swapCursor(mCursorVi_Ja);


            mAdapter.notifyDataSetChanged();
        }
    }

    private class WordCursorAdapter extends CursorAdapter {

        class ViewHolder {
            public TextView mTextView;
        }

        public WordCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
           View rowView = getActivity().getLayoutInflater().inflate(R.layout.one_word_layout, null);
            ViewHolder holder = new ViewHolder();
            holder.mTextView = (TextView) rowView.findViewById(R.id.text_view_word_item);
            rowView.setTag(holder);
            return rowView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();

            String word = cursor.getString(cursor.getColumnIndex(DataBase.DB_COLUMN_WORD));

            holder.mTextView.setText(word);

        }
    }


}
