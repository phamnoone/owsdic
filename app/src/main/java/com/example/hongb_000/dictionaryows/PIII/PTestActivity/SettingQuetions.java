package com.example.hongb_000.dictionaryows.PIII.PTestActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.hongb_000.dictionaryows.R;

/**
 * Created by Administrator on 15/07/2015.
 */
public class SettingQuetions extends AppCompatActivity {

    String Nvalue;
    Animation animAlpha;
    int isTest;
    String[] data = {"5", "10", "15", "20", "25"};
    String[] dataN = {
            "JPLT N1",
            "JPLT N2",
            "JPLT N3",
            "JPLT N4",
            "JPLT N5"
    };
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_test_quiz_setting);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cài đặt câu hỏi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isTest = getIntent().getIntExtra("test", -1);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.left_in);
        Button button = (Button) findViewById(R.id.button);
        button.setAnimation(animAlpha);

        Nvalue = getIntent().getStringExtra("N");
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner spinnerN = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapterN = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, dataN);
        adapterN.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerN.setAdapter(adapterN);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(SettingQuetions.this, TestActivity.class);
                next.putExtra("N", spinnerN.getSelectedItem().toString().charAt(6));
                next.putExtra("NQ", spinner.getSelectedItem().toString());
                next.putExtra("test", isTest);
                if (isTest == 1) {
                    SharedPreferences pre = getSharedPreferences("point_testing", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pre.edit();
                    edit.putInt("NQTrue", 0);
                    edit.commit();
                }
                startActivity(next);
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
