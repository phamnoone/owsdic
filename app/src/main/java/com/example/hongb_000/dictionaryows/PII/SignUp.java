package com.example.hongb_000.dictionaryows.PII;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hongb_000.dictionaryows.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by USER on 6/29/2015.
 */
public class SignUp extends ActionBarActivity {
    private String FIREBASE_URL;
    private Firebase mFirebaseRef,mFire;
    private String name,pass,pas,email,phone;
    private int temp=0,checkName,checkMail1;
    private ValueEventListener mConnectedListener;
    Button bntSign;
    EditText signName,signPass,signPas,signEmail,signPhone;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pii_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Đăng kí");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bntSign=(Button)findViewById(R.id.bntSignUp);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("DATA");
        FIREBASE_URL=bundle.getString("URL");
        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("Users");
        bntSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                checkName=CheckEmail(email);
                checkMail1=checkPerfectEmail(email);
                new AlertDialog.Builder(SignUp.this).setTitle("Xác nhận").setMessage("Bạn đồng ý với các điều khoản của chúng tôi?")
                        .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkName=CheckEmail(email);
                                CheckSignedUp();
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }

    private void getData() {
        signName=(EditText)findViewById(R.id.signName);
        signPass=(EditText)findViewById(R.id.signPass);
        signPas=(EditText)findViewById(R.id.signPas);
        signEmail=(EditText)findViewById(R.id.signEmail);
        signPhone=(EditText)findViewById(R.id.signPhone);
        name=signName.getText().toString();
        pass=signPass.getText().toString();
        pas=signPas.getText().toString();
        email=signEmail.getText().toString();
        phone=signPhone.getText().toString();
    }

    private void CheckSignedUp(){

        if (name.equals("")||pass.equals("")||pas.equals("")||email.equals("")||phone.equals(""))
            new AlertDialog.Builder(SignUp.this).setTitle("Lưu ý").setMessage("Hãy nhập đầy đủ thông tin.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
//            Toast.makeText(SignUp.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
        else if (checkMail1==0) new AlertDialog.Builder(SignUp.this).setTitle("Lưu ý").setMessage("Định dạng Email không đúng.")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
        else if (!pass.equals(pas)) new AlertDialog.Builder(SignUp.this).setTitle("Lưu ý").setMessage("Mật khẩu phải giống nhau.")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
//            Toast.makeText(SignUp.this,"Mật khẩu không trùng nhau!",Toast.LENGTH_LONG).show();
        else if (pass.length()<6) new AlertDialog.Builder(SignUp.this).setTitle("Lưu ý").setMessage("Mật khẩu không được nhỏ hơn 6 kí tự.")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
//            Toast.makeText(SignUp.this,"Mật khẩu không được nhỏ hơn 6 kí tự!",Toast.LENGTH_LONG).show();
        else if (checkName==1) { new AlertDialog.Builder(SignUp.this).setTitle("Lưu ý").setMessage("Email này đã được đăng kí, vui lòng nhập email khác.")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
//            Toast.makeText(SignUp.this,"Email này đã được đăng kí, vui lòng nhập email khác!",Toast.LENGTH_LONG).show();
            temp=0;
        }
        else {
            User user = new User(email, name, pass,phone);
            mFirebaseRef.push().setValue(user);
            new AlertDialog.Builder(SignUp.this).setTitle("Thành công")
                    .setMessage("Tài khoản của bạn đã được tạo!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent=new Intent(SignUp.this,Home.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("URL",FIREBASE_URL);
                            bundle.putString("from",email);
                            bundle.putString("mName",name);
                            intent.putExtra("DATA", bundle);
                            startActivity(intent);
                            SignUp.this.finish();
                        }
                    }).show();
        }

    }
    private int checkPerfectEmail(String email){
        String check1="@";
        String check2=".";
        int check=0;
        if (!email.contains(check1)) check=0;
        else if (!email.contains(check2)) check=0;
        else check=1;
        return check;
    }
    private int CheckEmail(String email){
        Query query = mFirebaseRef.orderByChild("email").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                temp = 1;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return temp;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Finally, HistoryActivity little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    //Toast.makeText(SignedUp.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(SignedUp.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
//                    new AlertDialog.Builder(SignUp.this).setTitle("Lỗi kết nối").setMessage("Kiểm tra lại kết nối mạng.")
//                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            }).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void close(){
        SignUp.this.finish();
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