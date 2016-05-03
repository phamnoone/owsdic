package com.example.hongb_000.dictionaryows.PIII.PTestActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.PIII.DataController.TestDataControler;
import com.example.hongb_000.dictionaryows.PIII.DataController.TestKanj;
import com.example.hongb_000.dictionaryows.PIII.MainTestActivity;
import com.example.hongb_000.dictionaryows.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 16/07/2015.
 */
public class TabNodeActivity extends AppCompatActivity {

    int AnsCorect;
    RadioButton A, B, C, D;
    RadioGroup radioGroup;
    Button End;
    Boolean isLast;
    int isTest;
    ArrayList<String> data;
    SharedPreferences pre;
    ArrayList<TestKanj> ListQuetions;
    int id;
    TestDataControler testDataControler;
    char nValue;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_test_node);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Câu " + id + "/" +
//                getIntent().getBundleExtra("data").getInt("Total"));


        data = getIntent().getBundleExtra("data").getStringArrayList("data");
        nValue = getIntent().getBundleExtra("data").getChar("N");
        ListQuetions = getIntent().getBundleExtra("data").getParcelableArrayList("ListQuetions");
        isLast = getIntent().getBooleanExtra("last", false);
        isTest = getIntent().getIntExtra("test", -1);
        id = getIntent().getBundleExtra("data").getInt("Number");
        testDataControler = new TestDataControler(this);

        TextView Q = (TextView) findViewById(R.id.Q);
        A = (RadioButton) findViewById(R.id.A);
        B = (RadioButton) findViewById(R.id.B);
        C = (RadioButton) findViewById(R.id.C);
        D = (RadioButton) findViewById(R.id.D);
        End = (Button) findViewById(R.id.end);
        radioGroup = (RadioGroup) findViewById(R.id.radioG);
        End.setVisibility(View.GONE);

        //set text;
        String Quetions = "Câu " + id + " : " + data.get(0);
        Q.setText(Quetions);
        A.setText(data.get(1));
        B.setText(data.get(2));
        C.setText(data.get(3));
        D.setText(data.get(4));
        AnsCorect = Integer.valueOf(data.get(5));

        if (isTest == 0) {
            setTraining();
        } else if (isTest == 1) {
            setTesting();
        }
    }

    public void setTesting() {
        if (isLast == true) {
            End.setVisibility(View.VISIBLE);
            End.setAnimation(AnimationUtils.loadAnimation(TabNodeActivity.this, R.anim.left_in));
            End.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //next den dau do di
                    Intent next = new Intent(TabNodeActivity.this, ResultActivity.class);
                    next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    next.putParcelableArrayListExtra("ListQuetions", ListQuetions);
                    next.putExtra("N", nValue);
                    startActivity(next);
                }
            });
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ansAction(A, 1);
                ansAction(B, 2);
                ansAction(C, 3);
                ansAction(D, 4);
            }
        });

    }


    private void ansAction(RadioButton button, final int value) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDataControler.putAnsxValue(id, value);
            }
        });
    }

    public void setTraining() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                A.setEnabled(false);
                B.setEnabled(false);
                C.setEnabled(false);
                D.setEnabled(false);
                if (isLast == true) {
                    End.setVisibility(View.VISIBLE);
                    End.setAnimation(AnimationUtils.loadAnimation(TabNodeActivity.this, R.anim.left_in));
                    End.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDialog();
                        }
                    });
                }
                switch (AnsCorect) {
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
            }
        });

        setClick(A);
        setClick(B);
        setClick(C);
        setClick(D);

    }

    private void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");// Set tiêu đề
        builder.setMessage("Bạn đã hoàn thành bài kiểm tra.Bạn có muốn thoát?");//Set nội dung cho Dialog
        builder.setCancelable(false);//Set có cho người dùng Cancer bằng nút quay lại (back) ko? false: ko
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Làm cái gì đó khi ấn Yes tại đây
                Intent next = new Intent(TabNodeActivity.this, MainTestActivity.class);
                next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(next);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");// Set tiêu đề
        builder.setMessage("Trở về màn hình kiểm tra chính");//Set nội dung cho Dialog
        builder.setCancelable(false);//Set có cho người dùng Cancer bằng nút quay lại (back) ko? false: ko
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent back = new Intent(TabNodeActivity.this, MainTestActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                testDataControler.deleteData(TabNodeActivity.this);
                startActivity(back);
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void setClick(RadioButton b) {
        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setTextColor(Color.RED);
            }
        });
    }
}
