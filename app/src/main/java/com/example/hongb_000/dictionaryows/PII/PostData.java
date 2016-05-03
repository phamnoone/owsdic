package com.example.hongb_000.dictionaryows.PII;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongb_000.dictionaryows.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Locale;
import java.util.Random;

/**
 * Created by USER on 6/29/2015.
 */
public class PostData extends ActionBarActivity {
    SQLiteDatabase database=null;
    private String FIREBASE_URL;
    private Firebase mFirebaseRef;
    private String from,ques,mName;
    private TextView txtQuestion;
    Button bntPost;
    private ValueEventListener mConnectedListener;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pii_post_data);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Đăng câu hỏi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        android.support.v7.app.ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFB567")));
        getData();
        getDatabase();
        database=openOrCreateDatabase("myData.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        // Setup our Firebase mFirebaseRef
        Firebase.setAndroidContext(PostData.this);
        mFirebaseRef = new Firebase(FIREBASE_URL).child("Question");
        txtQuestion=(TextView)findViewById(R.id.txtQuestionData);
        bntPost=(Button)findViewById(R.id.bntPost);
        bntPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
                PostData.this.finish();
            }
        });
        txtQuestion.setText(ques);

    }

    private void post() {
        Random r=new Random();
        String id_ques=""+r.nextInt(1000000000);
        Question question=new Question(id_ques,from,ques,mName);
        mFirebaseRef.push().setValue(question);
        ContentValues content = new ContentValues();
        content.put("id", id_ques);
        content.put("id_user", from);
        content.put("ques", ques);
        content.put("mName", mName);

        if (database != null) {
            long authorid = database.insert("tblQues", null, content);
            if (authorid == -1) {
                Toast.makeText(PostData.this, "Xảy ra lỗi, chưa thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(PostData.this, "Đã thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
        }
    }

    private void getData(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("DATA");
        FIREBASE_URL=bundle.getString("URL");
        from=bundle.getString("from");
        ques=bundle.getString("ques");
        mName=bundle.getString("mName");
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
                    //Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
//                    new AlertDialog.Builder(PostData.this).setTitle("Lỗi kết nối").setMessage("Kiểm tra lại kết nối mạng!")
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

    /*Hàm kiểm tra xem đã tồn tại bảng trong CSDL chưa
    có thì trả về true
    chưa có thì trả về false
     */
    public boolean isTableExits(SQLiteDatabase database,String tableName){
        Cursor cursor=database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name='" + tableName + "'", null);
        if (cursor!=null){
            if (cursor.getCount()>0){
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    /*Hàm tạo CSDL và các bảng liên quan

     */
    public SQLiteDatabase getDatabase(){
        try{
            database=openOrCreateDatabase("myData.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
            if (database!=null){
                if (isTableExits(database,"tblQues"))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sqlQues="CREATE TABLE tblQues ("
                        +"id text PRIMARY KEY,"+"id_user text,"+"ques text,"+"mName text)";
                database.execSQL(sqlQues);
                ContentValues content=new ContentValues();
                content.put("id","");
                content.put("ques","");
                content.put("id_user","");
                content.put("mName","");
                long authorid=database.insert("tblQues", null, content);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
        return database;
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
