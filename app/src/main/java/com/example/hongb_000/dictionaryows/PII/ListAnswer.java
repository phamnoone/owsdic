package com.example.hongb_000.dictionaryows.PII;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongb_000.dictionaryows.R;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Created by USER on 6/29/2015.
 */
public class ListAnswer extends ActionBarActivity{
    SQLiteDatabase database=null;
    private String FIREBASE_URL;
    private Firebase mFirebaseRef,mFire;
    private String from,id_ques,id_ans,mName,ques,name_user_ques,id_user_ques;
    private ListView listView;
    ArrayList<Answer> listData= new ArrayList<Answer>();
    AnswerAdapter itemAdapter=null;
    TextView txtNameQues,txtQues;
    EditText txtSend;
    ImageButton bntSend;
    Button bntFLQues;
    private ValueEventListener mConnectedListener;
    private ProfilePictureView profilePictureView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pii_list_answer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Danh sách câu trả lời");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getButton();
        getData();
        getDatabase();
        database=openOrCreateDatabase("myData.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
//        getQuestion();
        // Setup our Firebase mFirebaseRef
        Firebase.setAndroidContext(ListAnswer.this);
        mFirebaseRef = new Firebase(FIREBASE_URL).child("Answer");
        mFire=new Firebase(FIREBASE_URL).child("Vote");
        clickButton();
        getButtonFollow();
        itemAdapter=new AnswerAdapter(ListAnswer.this,R.layout.pii_answer,listData,from);
        listView.setAdapter(itemAdapter);
        getListAswer();
//        itemAdapter.notifyDataSetChanged();
    }

    private void clickButton() {
        txtSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });
        bntSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        bntFLQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followQues();
            }
        });

    }

    private void followQues() {
        Cursor cursor=database.query("tblQues",null,"id=?",new String[]{id_ques},null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount()!=0) {//Toast.makeText(ListAnswer.this,"Bạn đã theo dõi câu hỏi này rồi!",Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(ListAnswer.this).setTitle("Bỏ theo dõi").setMessage("Bạn chắc chắn không muốn tiếp tục nhận thông tin từ câu hỏi này?")
                    .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database.delete("tblQues","id=?",new String[]{id_ques});
                            dialog.cancel();
                            bntFLQues.setTextColor(getResources().getColor(R.color.colorButtonFollow));
                            bntFLQues.setText("Theo dõi");
                        }
                    })
                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

        }
        else {new AlertDialog.Builder(ListAnswer.this).setTitle("Theo dõi").setMessage("Bạn muốn theo dõi câu hỏi này?")
                .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues content = new ContentValues();
                        content.put("id", id_ques);
                        content.put("id_user", id_user_ques);
                        content.put("ques", ques);
                        content.put("mName", name_user_ques);

                        if (database != null) {
                            long authorid = database.insert("tblQues", null, content);
                            if (authorid == -1) {
                                Toast.makeText(ListAnswer.this, "Xảy ra lỗi, chưa thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ListAnswer.this, "Đã thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
                                bntFLQues.setTextColor(getResources().getColor(R.color.colorPrimary));
                                bntFLQues.setText("Bỏ theo dõi");
                            }
                        }
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        }
    }

    private void sendMessage() {
        String input=txtSend.getText().toString();
        if (!input.equals("")){
            Random r=new Random();
            id_ans=""+r.nextInt(1000000000);
            Answer answer1=new Answer(input,id_ans,id_ques,from,mName);
            mFirebaseRef.push().setValue(answer1);
            txtSend.setText("");
//            listData.add(answer1);
//            itemAdapter.notifyDataSetChanged();
//            itemAdapter=new AnswerAdapter(ListAnswer.this,R.layout.pii_answer,listData,from);
//            listView.setAdapter(itemAdapter);
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    private void getListAswer() {
        final Query query2=mFirebaseRef.orderByChild("id_ques").equalTo(id_ques);
        query2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> value1 = (Map<String, Object>) snapshot.getValue();

                Answer answer= new Answer(value1.get("ans").toString(), value1.get("id").toString(),
                        id_ques, value1.get("id_user").toString(),"0",value1.get("mName").toString(),"0");
                listData.add(answer);
                itemAdapter.notifyDataSetChanged();
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

        Query query= mFire.orderByChild("id_ques").equalTo(id_ques);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Objects> value2= (Map<String, Objects>) dataSnapshot.getValue();
                for (int i=0;i<listData.size();i++){
                    if (listData.get(i).getId().toString().equals(value2.get("id_ans"))){
                        if (from.equals(value2.get("id_user"))) listData.get(i).setVoted("1");
                        int vot= Integer.parseInt(listData.get(i).getVoteNumber())+1;
                        listData.get(i).setVoteNumber(vot+"");
                        itemAdapter.notifyDataSetChanged();
                    }
                }
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

    }

    private void getButton() {
        txtNameQues=(TextView)findViewById(R.id.txtNameQuestion);
        txtQues=(TextView)findViewById(R.id.txtQuestion);
        listView=(ListView)findViewById(R.id.listAnswer);
        txtSend=(EditText)findViewById(R.id.txtSend);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE); //tham chiếu tới INPUT service
        imm.hideSoftInputFromWindow(txtSend.getWindowToken(), 0); //ẩn bàn phím
        bntSend=(ImageButton)findViewById(R.id.bntSend);
        bntFLQues=(Button)findViewById(R.id.bntFollowQues);
//        profilePictureView=(ProfilePictureView)findViewById(R.id.avatarQuestionListAns);
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
//                    new AlertDialog.Builder(ListAnswer.this).setTitle("Lỗi kết nối").setMessage("Kiểm tra lại kết nối mạng!")
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

    private void getData(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("DATA");
        FIREBASE_URL=bundle.getString("URL");
        from=bundle.getString("from");
        id_ques=bundle.getString("id_ques");
        mName=bundle.getString("mName");
        ques=bundle.getString("ques");
        name_user_ques=bundle.getString("name_user_ques");
        id_user_ques=bundle.getString("id_user_ques");
        txtNameQues.setText("  "+name_user_ques);
        txtQues.setText("  "+ques);
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

    public void getButtonFollow() {
        Cursor cursor=database.query("tblQues",null,"id=?",new String[]{id_ques},null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount()!=0) {//Toast.makeText(ListAnswer.this,"Bạn đã theo dõi câu hỏi này rồi!",Toast.LENGTH_LONG).show();
            bntFLQues.setTextColor(getResources().getColor(R.color.colorPrimary));
            bntFLQues.setText("Bỏ theo dõi");
        }
    }
}
