package com.example.hongb_000.dictionaryows.PIII;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.hongb_000.dictionaryows.PIII.PHistory.HistoryTestActivity;
import com.example.hongb_000.dictionaryows.PIII.PTestActivity.SettingQuetions;
import com.example.hongb_000.dictionaryows.PIII.PUpdate.UpdateActivity;
import com.example.hongb_000.dictionaryows.R;

/**
 * Created by phamn on 8/21/2015.
 */
public class MainTestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    ImageButton kiemtra;
    ImageButton luyentap;
    ImageButton thongke;
    ImageButton themcauhoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_maintest);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kiểm Tra");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        kiemtra = (ImageButton) findViewById(R.id.kiemtra);
        luyentap = (ImageButton) findViewById(R.id.luyentap);
        thongke = (ImageButton) findViewById(R.id.thongke);
        themcauhoi = (ImageButton) findViewById(R.id.themcauhoi);

        kiemtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextAtv = new Intent(MainTestActivity.this, SettingQuetions.class);
                nextAtv.putExtra("test", 1);
                startActivity(nextAtv);
            }
        });
        luyentap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextAtv = new Intent(MainTestActivity.this, SettingQuetions.class);
                nextAtv.putExtra("test", 0);
                startActivity(nextAtv);
            }
        });
        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextAtv = new Intent(MainTestActivity.this, HistoryTestActivity.class);
                startActivity(nextAtv);
            }
        });
        themcauhoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextAtv = new Intent(MainTestActivity.this, UpdateActivity.class);
               startActivity(nextAtv);
            }
        });
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
