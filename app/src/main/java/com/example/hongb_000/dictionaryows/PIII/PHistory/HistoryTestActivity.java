package com.example.hongb_000.dictionaryows.PIII.PHistory;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.PIII.DataController.HistoryData;
import com.example.hongb_000.dictionaryows.PIII.DataController.HistoryDataController;
import com.example.hongb_000.dictionaryows.R;

import java.util.ArrayList;

/**
 * Created by phamn on 8/21/2015.
 */
public class HistoryTestActivity extends AppCompatActivity {

    ListView listView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_history);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thống kê");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView = (ListView) findViewById(R.id.listView);
        HistoryDataController historyDataController = new HistoryDataController(this);
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(this, R.layout.piii_history_customlisview,
                historyDataController.getData());
        listView.setAdapter(arrayAdapter);

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

    private class MyArrayAdapter extends ArrayAdapter<HistoryData> {
        Activity context;
        int idLayout;
        ArrayList<HistoryData> listData;

        public MyArrayAdapter(Activity context, int resource, ArrayList<HistoryData> listData) {
            super(context, resource, listData);
            this.context = context;
            this.idLayout = resource;
            this.listData = listData;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(idLayout, null);

            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView n = (TextView) convertView.findViewById(R.id.N);
            TextView numberQuetions = (TextView) convertView.findViewById(R.id.numberquetions);
            TextView point = (TextView) convertView.findViewById(R.id.point);

            HistoryData data = listData.get(position);

            date.setText(data.getDate());
            n.setText(data.getnValue());
            numberQuetions.setText(data.getNumberQ());
            point.setText(data.getPoint());
            if ((position + 1) % 2 == 0)
                convertView.setBackground(new ColorDrawable(Color.argb(255,195,216,244)));
            return convertView;
        }
    }


}
