package com.example.hongb_000.dictionaryows.PII;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongb_000.dictionaryows.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 6/25/2015.
 */
public class Home extends ActionBarActivity{
    SQLiteDatabase database=null;
    private String FIREBASE_URL;
    private Firebase mFirebaseRef;
    private String from,mName;
    private EditText txtSeach;
    private Button bntFollow;
    private ImageButton bntSeach;
    ListView listQuestion;
    ArrayList<Question> listData=new ArrayList<Question>();
    QuestionAdapter itemAdapter=null;
    private ValueEventListener mConnectedListener;
    private Button bntCmt;
    private SearchView mSearchView;
    @InjectView(R.id.toolbar)
    Toolbar mToolBar;
    @InjectView(R.id.title)
    TextView mTVTitle;


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pii_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.inject(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTVTitle.setText("Tìm Kiếm");
        getData();
        getDatabase();
        database=openOrCreateDatabase("myData.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        Firebase.setAndroidContext(Home.this);
        mFirebaseRef = new Firebase(FIREBASE_URL).child("Question");
//        txtSeach=(EditText)findViewById(R.id.txtSeach);
//        bntSeach=(ImageButton)findViewById(R.id.bntSeach);
        bntFollow=(Button)findViewById(R.id.bntFollow);
        listQuestion=(ListView)findViewById(R.id.listQuestion);
        startLoadData();
        itemAdapter=new QuestionAdapter(Home.this,R.layout.pii_question,listData);
        bntCmt=(Button)findViewById(R.id.bntCmtQues);
        listQuestion.setAdapter(itemAdapter);
        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Question question = (Question) itemAdapter.getItem(i);
                Intent intent1 = new Intent(Home.this, ListAnswer.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("URL", FIREBASE_URL);
                bundle1.putString("from", from);
                bundle1.putString("mName", mName);
                bundle1.putString("id_ques", question.getId());
                bundle1.putString("ques", question.getQues());
                bundle1.putString("id_user_ques", question.getId_user());
                bundle1.putString("name_user_ques", question.getmName());
                intent1.putExtra("DATA", bundle1);
                startActivity(intent1);

            }
        });

        bntFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFollow = new Intent(Home.this, Follow.class);
                Bundle bundleFollow = new Bundle();
                bundleFollow.putString("URL", FIREBASE_URL);
                bundleFollow.putString("from", from);
                bundleFollow.putString("mName", mName);
                intentFollow.putExtra("DATA", bundleFollow);
                startActivity(intentFollow);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setQueryHint("Nhập từ tìm kiếm");

        EditText searchPlate = ((EditText) mSearchView.findViewById(R.id.search_src_text));
        searchPlate.setBackgroundResource(R.drawable.underlinetheme_edit_text_holo_light);
        searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchPlate.setTextColor(getResources().getColor(R.color.textColorPrimary));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Seach(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mTVTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
                MenuItemCompat.expandActionView(menu.findItem(R.id.action_search));
                mSearchView.requestFocus();
                InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(mSearchView, 0);
            }
        });

        return true;
    }


    private void Seach(String x) {
        int j=0;
        if(x.equals("")){
            new AlertDialog.Builder(Home.this).setTitle("Xin lỗi").setMessage("Mời bạn nhập từ cần tìm!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        }
        else {
            for (int i = 0; i < itemAdapter.getCount(); i++) {
                Question question = (Question) itemAdapter.getItem(i);
                if (x.equals(question.getQues())) {
                    Intent intentListAnsrer = new Intent(Home.this, ListAnswer.class);
                    Bundle bundleListAnswer = new Bundle();
                    bundleListAnswer.putString("URL", FIREBASE_URL);
                    bundleListAnswer.putString("from", from);
                    bundleListAnswer.putString("id_ques", question.getId());
                    bundleListAnswer.putString("mName",mName);
                    bundleListAnswer.putString("ques",question.getQues());
                    bundleListAnswer.putString("id_user_ques",question.getId_user());
                    bundleListAnswer.putString("name_user_ques",question.getmName());
                    intentListAnsrer.putExtra("DATA", bundleListAnswer);
                    startActivity(intentListAnsrer);
                    j++;
                    break;
                }
            }
            if (j == 0) {
                Intent intentPostData = new Intent(Home.this, PostData.class);
                Bundle bundlePostData = new Bundle();
                bundlePostData.putString("URL", FIREBASE_URL);
                bundlePostData.putString("from", from);
                bundlePostData.putString("ques", x);
                bundlePostData.putString("mName", mName);
                intentPostData.putExtra("DATA", bundlePostData);
                startActivity(intentPostData);

            }
        }
    }

    private void getData(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("DATA");
        FIREBASE_URL=bundle.getString("URL");
        from=bundle.getString("from");
        mName=bundle.getString("mName");
    }
    private void startLoadData() {
        final Query query = mFirebaseRef.orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> value1 = (Map<String, Object>) snapshot.getValue();
                Question question=new Question(value1.get("id").toString(),value1.get("id_user").toString(),value1.get("ques").toString(),value1.get("mName").toString());
                listData.add(question);
                itemAdapter.notifyDataSetChanged();
//                itemAdapter=new QuestionAdapter(Home.this,R.layout.pii_question,listData);
//                listQuestion.setAdapter(itemAdapter);
//                itemAdapter.registerDataSetObserver(new DataSetObserver() {
//                    @Override
//                    public void onChanged() {
//                        super.onChanged();
//                        listQuestion.setSelection(itemAdapter.getCount() - 1);
//                    }
//                });

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
//                    new AlertDialog.Builder(Home.this).setTitle("Lỗi kết nối").setMessage("Kiểm tra lại kết nối mạng!")
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify HistoryActivity parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_search) {
            //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    Intent intent1 = new Intent(Home.this, ListAnswer.class);
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
                        new AlertDialog.Builder(Home.this).setTitle("Bỏ theo dõi").setMessage("Bạn chắc chắn không muốn tiếp tục nhận thông tin từ câu hỏi này?")
                                .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        database.delete("tblQues","id=?",new String[]{id_ques});
                                        bntFollow.setTextColor(getResources().getColor(R.color.colorButtonFollow));
                                        bntFollow.setText("Theo Dõi");
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
                    else {new AlertDialog.Builder(Home.this).setTitle("Theo dõi").setMessage("Bạn muốn theo dõi câu hỏi này?")
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
                                            Toast.makeText(Home.this, "Xảy ra lỗi, chưa thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
                                        } else{
                                            Toast.makeText(Home.this, "Đã thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
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
            Cursor cursor=database.query("tblQues",null,"id=?",new String[]{datas.get(position).getId()},null,null,null);
            cursor.moveToFirst();
            if (cursor.getCount()!=0) {//Toast.makeText(ListAnswer.this,"Bạn đã theo dõi câu hỏi này rồi!",Toast.LENGTH_LONG).show();
                bntFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
                bntFollow.setText("Bỏ theo dõi");
            }
            return rowView;
        }

    }

//    private void followQues(final String id_ques,final String ques, final String id_user_ques, final String name_user_ques) {
//        Cursor cursor=database.query("tblQues",null,"id=?",new String[]{id_ques},null,null,null);
//        cursor.moveToFirst();
//        if (cursor.getCount()!=0) {//Toast.makeText(ListAnswer.this,"Bạn đã theo dõi câu hỏi này rồi!",Toast.LENGTH_LONG).show();
//            new AlertDialog.Builder(Home.this).setTitle("Bỏ theo dõi").setMessage("Bạn chắc chắn không muốn tiếp tục nhận thông tin từ câu hỏi này?")
//                    .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            database.delete("tblQues","id=?",new String[]{id_ques});
//                            bntFollow.setTextColor(getResources().getColor(R.color.colorButtonFollow));
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
//        else {new AlertDialog.Builder(Home.this).setTitle("Theo dõi").setMessage("Bạn muốn theo dõi câu hỏi này?")
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
//                                Toast.makeText(Home.this, "Xảy ra lỗi, chưa thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(Home.this, "Đã thêm vào danh sách theo dõi!", Toast.LENGTH_LONG).show();
//                                bntFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
