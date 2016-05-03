package com.example.hongb_000.dictionaryows.PIV;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongb_000.dictionaryows.MainActivity;
import com.example.hongb_000.dictionaryows.R;

import java.io.IOException;

/**
 * Created by Administrator on 15/07/2015.
 */
public class SettingActivity extends AppCompatActivity {
    EditText maxHis;
    Button seachBt;
    TextView link;
    DataSetting dataSetting;
    ImportDataController importDataController;
    int max;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piv_setting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cài đặt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        maxHis = (EditText) findViewById(R.id.max);
        seachBt = (Button) findViewById(R.id.button2);
        seachBt.setVisibility(View.GONE);
        dataSetting = new DataSetting(this);
        max = dataSetting.getMaxHistory();
        maxHis.setText(String.valueOf(max));

        importDataController = new ImportDataController(this);
        importDataController.setButon(seachBt);

    }

    @Override
    public void onBackPressed() {
        //check data
        max = Integer.valueOf(maxHis.getText().toString());
        if (max >= 2) {
            Intent a = new Intent(this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            dataSetting.putMaxhistory(max);
        } else
            Toast.makeText(this, "Số từ tối thiểu là 2", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                max = Integer.valueOf(maxHis.getText().toString());
                if (max >= 2) {
                    dataSetting.putMaxhistory(max);
                    this.onBackPressed();
                } else
                    Toast.makeText(this, "Số từ tối thiểu là 2", Toast.LENGTH_LONG).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            importDataController.setUrl(data.getDataString().substring(7));
        } catch (Exception e) {
            Toast.makeText(this,"file không hợp lệ",Toast.LENGTH_LONG).show();
        }
        if (importDataController.CheckImprotData() == true) {
            try {
                importDataController.MoveFileToData();
                Toast.makeText(this, "Import thành công", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"file không hợp lệ",Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this,"File không hợp lệ",Toast.LENGTH_LONG).show();

    }
}
