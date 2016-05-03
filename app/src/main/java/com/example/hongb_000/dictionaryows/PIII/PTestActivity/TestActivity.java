package com.example.hongb_000.dictionaryows.PIII.PTestActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.PIII.DataController.DataController;
import com.example.hongb_000.dictionaryows.PIII.DataController.TestDataControler;
import com.example.hongb_000.dictionaryows.PIII.DataController.TestKanj;
import com.example.hongb_000.dictionaryows.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 15/07/2015.
 */
public class TestActivity extends TabActivity {

    TabHost tabHost;
    int isTest;
    TextView tile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        char N = getIntent().getCharExtra("N", '5');
        String NQ = getIntent().getStringExtra("NQ");
        isTest = getIntent().getIntExtra("test", -1);
        tile = (TextView) findViewById(R.id.textView20);

        tile.setText("Số câu hỏi : " + NQ + " - JPLT N" + N);
        DataController dataController = new DataController(this);
        ArrayList<TestKanj> data = dataController.getQuetions(String.valueOf(N), NQ);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TestDataControler testDataControler = new TestDataControler(this, data.size());
        testDataControler.close();

        for (int i = 0; i < data.size(); i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec(String.valueOf(i + 1));
            tab.setIndicator((String.valueOf(i + 1)));
            Intent tabIt = new Intent(this, TabNodeActivity.class);
            tabIt.putExtra("test", isTest);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", data.get(i).toArray());
            bundle.putInt("Number", i + 1);
            bundle.putInt("Total", data.size());
            bundle.putChar("N", N);
            if (i == data.size() - 1)
                tabIt.putExtra("last", true);
            else tabIt.putExtra("last", false);
            bundle.putParcelableArrayList("ListQuetions", data);
            tabIt.putExtra("data", bundle);
            tab.setContent(tabIt);
            tabHost.addTab(tab);
        }

        tabHost.setOnTabChangedListener(new AnimatedTabHostListener(tabHost));
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffffff"));
        }
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffffff")); // unselected
        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextColor(Color.parseColor("#000000"));

        for (int i = 0; i < data.size(); i++) {
            getTabWidget().getChildAt(i).setFocusableInTouchMode(true);
        }
    }

    float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.BUTTON_BACK: {

            }
            break;
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN:

            {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float currentX = touchevent.getX();

                // if left to right swipe on screen
                if (lastX < currentX) {

                    switchTabs(true);
                }

                // if right to left swipe on screen
                if (lastX > currentX) {
                    switchTabs(false);
                }

                break;
            }
        }
        return false;
    }

    public void switchTabs(boolean direction) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#3f51b5")); // unselected
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        int maxtab = tabHost.getTabWidget().getTabCount();
        if (direction) // true = move left
        {
            if (tabHost.getCurrentTab() == 0) {

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffffff")); // selected
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#000000"));

            } else {
                tabHost.setCurrentTab(tabHost.getCurrentTab() - 1);
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffffff")); // selected
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#000000"));
            }
        } else
        // move right
        {
            tabHost.setCurrentTab(tabHost.getCurrentTab() + 1);
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffffff")); // selected
            TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#000000"));
            if (tabHost.getCurrentTab() + 1 != maxtab) {
                if (tabHost.getCurrentTab() != (tabHost.getTabWidget()
                        .getTabCount() - 1)) {

                } else
                    tabHost.setCurrentTab(0);

            }
        }
    }


}
