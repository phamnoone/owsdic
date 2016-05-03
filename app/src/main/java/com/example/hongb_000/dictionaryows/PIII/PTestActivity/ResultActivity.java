package com.example.hongb_000.dictionaryows.PIII.PTestActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.PIII.DataController.HistoryDataController;
import com.example.hongb_000.dictionaryows.PIII.DataController.TestDataControler;
import com.example.hongb_000.dictionaryows.PIII.DataController.TestKanj;
import com.example.hongb_000.dictionaryows.PIII.MainTestActivity;
import com.example.hongb_000.dictionaryows.R;

import java.util.ArrayList;

/**
 * Created by phamn on 8/23/2015.
 */
public class ResultActivity extends AppCompatActivity {

    int trueAns;
    int falseAns;
    int nullAns;
    int numberQuetions;
    char nValue;
    ArrayList<TestKanj> listQuetions;
    ArrayList<Integer> listAns;
    TestDataControler testDataControler;
    //
    RadioButton A, B, C, D;
    RadioGroup radioGroup;
    Button End;
    GridView listViewQuetion;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_test_thongke);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Đáp án và kết quả phân tích");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testDataControler = new TestDataControler(this);
        listAns = testDataControler.getData();
        listQuetions = getIntent().getParcelableArrayListExtra("ListQuetions");
        nValue = getIntent().getCharExtra("N", '1');
        getNumber();
        testDataControler.deleteData(this);

        TextView quetions = (TextView) findViewById(R.id.quetion);
        TextView quetionT = (TextView) findViewById(R.id.trueans);
        TextView quetionF = (TextView) findViewById(R.id.flauseans);
        TextView quetionN = (TextView) findViewById(R.id.nullans);

        listViewQuetion = (GridView) findViewById(R.id.view);

        quetions.setText(quetions.getText() + " " + numberQuetions);
        quetionT.setText(quetionT.getText() + " " + trueAns);
        quetionF.setText(quetionF.getText() + " " + falseAns);
        quetionN.setText(quetionN.getText() + " " + nullAns);

        setlistview();
    }

    @Override
    public void onBackPressed() {
        HistoryDataController historyDataController = new HistoryDataController(this);
        historyDataController.pushData("JPLT N" + nValue, numberQuetions, trueAns);
        Intent next = new Intent(ResultActivity.this, MainTestActivity.class);
        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    private void setlistview() {
        MyAdapter myAdapter = new MyAdapter(this, R.layout.piii_thongke_row_grid, listQuetions, listAns);
        listViewQuetion.setAdapter(myAdapter);
        listViewQuetion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(ResultActivity.this);
                dialog.setTitle("Chi tiết câu số " + (id + 1));
                dialog.setContentView(R.layout.piii_test_node);

                TextView Q = (TextView) dialog.findViewById(R.id.Q);
                A = (RadioButton) dialog.findViewById(R.id.A);
                B = (RadioButton) dialog.findViewById(R.id.B);
                C = (RadioButton) dialog.findViewById(R.id.C);
                D = (RadioButton) dialog.findViewById(R.id.D);
                End = (Button) dialog.findViewById(R.id.end);
                End.setVisibility(View.GONE);

                A.setEnabled(false);
                B.setEnabled(false);
                C.setEnabled(false);
                D.setEnabled(false);

                TestKanj data = listQuetions.get(position);
                String Quetions = data.getQuetion();
                Q.setText(Quetions);
                Q.setTextSize(20);
                String[] ansList = data.getAnser();

                A.setText(ansList[0]);
                A.setTextSize(18);
                B.setText(ansList[1]);
                B.setTextSize(18);
                C.setText(ansList[2]);
                C.setTextSize(18);
                D.setText(ansList[3]);
                D.setTextSize(18);

                switch (listAns.get(position)) {
                    case 1:
                        A.setTextColor(Color.RED);
                        A.setChecked(true);
                        break;
                    case 2:
                        B.setTextColor(Color.RED);
                        B.setChecked(true);
                        break;
                    case 3:
                        C.setTextColor(Color.RED);
                        C.setChecked(true);
                        break;
                    case 4:
                        D.setTextColor(Color.RED);
                        D.setChecked(true);
                        break;
                }

                switch (data.getAnsCorect()) {
                    case 1:
                        A.setTextColor(Color.GREEN);

                        break;
                    case 2:
                        B.setTextColor(Color.GREEN);

                        break;
                    case 3:
                        C.setTextColor(Color.GREEN);

                        break;
                    case 4:
                        D.setTextColor(Color.GREEN);

                        break;
                }

                dialog.show();
            }
        });


    }

    private void getNumber() {
        numberQuetions = listQuetions.size();
        for (int i = 0; i < listAns.size(); i++) {
            if (listAns.get(i) == 0)
                nullAns += 1;
            if (listAns.get(i) == listQuetions.get(i).getAnsCorect())
                trueAns += 1;
        }
        falseAns = numberQuetions - nullAns - trueAns;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                HistoryDataController historyDataController = new HistoryDataController(this);
                historyDataController.pushData("JPLT N" + nValue, numberQuetions, trueAns);
                Intent intent = new Intent(this, MainTestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
