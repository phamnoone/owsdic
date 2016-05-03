package com.example.hongb_000.dictionaryows.PII;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongb_000.dictionaryows.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by USER on 8/20/2015.
 */
public class Follow extends ActionBarActivity {
    SQLiteDatabase database=null;
    ListView listFollow;
    private String FIREBASE_URL;
    private Firebase mFirebaseRef;
    private String from,mName;
    ArrayList<Question> list=new ArrayList<Question>();
    QuestionAdapter adapter=null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pii_follow);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Danh sách theo dõi của bạn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listFollow=(ListView)findViewById(R.id.listFollow);
        getData();
        getDatabase();
        database=openOrCreateDatabase("myData.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        if(database!=null){
            Cursor cursor=database.query("tblQues",null,null,null,null,null,null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Question question=new Question(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                list.add(question);
                cursor.moveToNext();
            }
            cursor.close();
        }
        adapter=new QuestionAdapter(Follow.this,R.layout.pii_question,list);
        listFollow.setAdapter(adapter);
        listFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = (Question) adapter.getItem(position);
                Intent intent1 = new Intent(Follow.this, ListAnswer.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("URL", FIREBASE_URL);
                bundle1.putString("from", from);
                bundle1.putString("mName", mName);
                bundle1.putString("id_ques", question.getId());
                bundle1.putString("ques",question.getQues());
                bundle1.putString("id_user_ques",question.getId_user());
                bundle1.putString("name_user_ques",question.getmName());
                intent1.putExtra("DATA", bundle1);
                startActivity(intent1);
            }
        });

    }

    private void getData() {
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("DATA");
        FIREBASE_URL=bundle.getString("URL");
        from=bundle.getString("from");
        mName=bundle.getString("mName");
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
//                ContentValues content=new ContentValues();
//                content.put("id","");
//                content.put("id_user","");
//                content.put("ques","");
//                content.put("mName","");
//                long authorid=database.insert("tblQues", null, content);
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
    private class QuestionAdapter extends ArrayAdapter<Question> {
        private Activity myContext;
        private List<Question> datas;
        private String checkEmail="@";
        private String facebook="facebook:";
        private static final String FIREBASE_URL = "https://androiddictionary.firebaseio.com";
        public QuestionAdapter(Context context, int textViewResourceId,
                               List<Question> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            myContext = (Activity) context;
            datas = objects;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.pii_question, null);
            TextView txtNameQuestion= (TextView) rowView.findViewById(R.id.txtNameQuestion);
            txtNameQuestion.setText("  "+datas.get(position).getmName());
            TextView txtQuestion=(TextView)rowView.findViewById(R.id.txtQuestion);
            txtQuestion.setText("  "+datas.get(position).getQues());
            Button bntComt=(Button)rowView.findViewById(R.id.bntCmtQues);
            final Button bntFollow=(Button)rowView.findViewById(R.id.bntFollowQues);
            bntComt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Follow.this, ListAnswer.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("URL", FIREBASE_URL);
                    bundle1.putString("from", from);
                    bundle1.putString("mName", mName);
                    bundle1.putString("id_ques", datas.get(position).getId());
                    bundle1.putString("ques",datas.get(position).getQues());
                    bundle1.putString("id_user_ques",datas.get(position).getId_user());
                    bundle1.putString("name_user_ques",datas.get(position).getmName());
                    intent1.putExtra("DATA", bundle1);
                    startActivity(intent1);

                }
            });
            bntFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 //                   followQues(datas.get(position).getId(),datas.get(position).getQues(),datas.get(position).getId_user(),datas.get(position).getmName());
                    final String id_ques=datas.get(position).getId(),id_user_ques=datas.get(position).getId_user(),
                            ques=datas.get(position).getQues(),name_user_ques=datas.get(position).getmName();
                    final Cursor cursor=database.query("tblQues",null,"id=?",new String[]{id_ques},null,null,null);
                    cursor.moveToFirst();
                    if (cursor.getCount()!=0) {//Toast.makeText(ListAnswer.this,"Bạn đã theo dõi câu hỏi này rồi!",Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(Follow.this).setTitle("Bỏ theo dõi").setMessage("Bạn chắc chắn không muốn tiếp tục nhận thông tin từ câu hỏi này?")
                                .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        database.delete("tblQues","id=?",new String[]{id_ques});
                                        bntFollow.setTextColor(getResources().getColor(R.color.colorButtonFollow));
                                        bntFollow.setText("Theo dõi");
                                        list.clear();
                                        database=openOrCreateDatabase("myData.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
                                        if(database!=null){
                                            Cursor cursor=database.query("tblQues",null,null,null,null,null,null);
                                            cursor.moveToFirst();
                                            while (!cursor.isAfterLast()){
                                                Question question=new Question(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                                                list.add(question);
                                                cursor.moveToNext();
                                            }
                                            cursor.close();
                                        }
                                        adapter=new QuestionAdapter(Follow.this,R.layout.pii_question,list);
                                        listFollow.setAdapter(adapter);
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
                    else {new AlertDialog.Builder(Follow.this).setTitle("Theo dõi").setMessage("Bạn muốn theo dõi câu hỏi này?")
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
                                            Toast.makeText(Follow.this, "Xảy ra lỗi, chưa thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
                                        } else{
                                            Toast.makeText(Follow.this, "Đã thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
                                            bntFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            bntFollow.setText("Bỏ theo dõi");
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
            });
//        ProfilePictureView profilePictureView = (ProfilePictureView)rowView.findViewById(R.id.avatarQues);
//        if (!datas.get(position).getId_user().contains(checkEmail))
//            if (!datas.get(position).getId_user().contains("."))
//                if (datas.get(position).getId_user().contains(facebook)){
//                    GetID getID=new GetID(datas.get(position).getId_user());
//                    profilePictureView.setProfileId(getID.getId());
//                }
            bntFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
            bntFollow.setText("Bỏ theo dõi");
            return rowView;
        }

    }
//    private void followQues(final String id_ques,final String ques, final String id_user_ques, final String name_user_ques) {
//        Cursor cursor=database.query("tblQues",null,"id=?",new String[]{id_ques},null,null,null);
//        cursor.moveToFirst();
//        if (cursor.getCount()!=0) {//Toast.makeText(ListAnswer.this,"Bạn đã theo dõi câu hỏi này rồi!",Toast.LENGTH_LONG).show();
//            new AlertDialog.Builder(Follow.this).setTitle("Bỏ theo dõi").setMessage("Bạn chắc chắn không muốn tiếp tục nhận thông tin từ câu hỏi này?")
//                    .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            database.delete("tblQues","id=?",new String[]{id_ques});
//                            dialog.cancel();
//                        }
//                    })
//                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    }).show();
//
//        }
//        else {new AlertDialog.Builder(Follow.this).setTitle("Theo dõi").setMessage("Bạn muốn theo dõi câu hỏi này?")
//                .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ContentValues content = new ContentValues();
//                        content.put("id", id_ques);
//                        content.put("id_user", id_user_ques);
//                        content.put("ques", ques);
//                        content.put("mName", name_user_ques);
//
//                        if (database != null) {
//                            long authorid = database.insert("tblQues", null, content);
//                            if (authorid == -1) {
//                                Toast.makeText(Follow.this, "Xảy ra lỗi, chưa thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(Follow.this, "Đã thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        dialog.cancel();
//                    }
//                })
//                .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                }).show();
//
//        }
//    }
}
