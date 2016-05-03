//package com.example.hongb_000.dictionaryows.PII;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.hongb_000.dictionaryows.R;
//
//import java.util.List;
//
///**
// * Created by USER on 6/25/2015.
// */
//public class QuestionAdapter extends ArrayAdapter<Question> {
//    private Activity myContext;
//    private List<Question> datas;
//    private String checkEmail="@";
//    private String facebook="facebook:";
//    private static final String FIREBASE_URL = "https://androiddictionary.firebaseio.com";
//    public QuestionAdapter(Context context, int textViewResourceId,
//                           List<Question> objects) {
//        super(context, textViewResourceId, objects);
//        // TODO Auto-generated constructor stub
//        myContext = (Activity) context;
//        datas = objects;
//
//    }
//
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = myContext.getLayoutInflater();
//        View rowView = inflater.inflate(R.layout.pii_question, null);
//        TextView txtNameQuestion= (TextView) rowView.findViewById(R.id.txtNameQuestion);
//        txtNameQuestion.setText(datas.get(position).getmName());
//        TextView txtQuestion=(TextView)rowView.findViewById(R.id.txtQuestion);
//        txtQuestion.setText(datas.get(position).getQues());
//        Button bntComt=(Button)rowView.findViewById(R.id.bntCmtQues);
//        Button bntFollow=(Button)rowView.findViewById(R.id.bntFollowQues);
//        bntComt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(Home.this, ListAnswer.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("URL", FIREBASE_URL);
//                bundle1.putString("from", from);
//                bundle1.putString("mName", mName);
//                bundle1.putString("id_ques", datas.get(position).getId());
//                bundle1.putString("ques",datas.get(position).getQues());
//                bundle1.putString("id_user_ques",datas.get(position).getId_user());
//                bundle1.putString("name_user_ques",datas.get(position).getmName());
//                intent1.putExtra("DATA", bundle1);
//                startActivity(intent1);
//
//            }
//        });
////        ProfilePictureView profilePictureView = (ProfilePictureView)rowView.findViewById(R.id.avatarQues);
////        if (!datas.get(position).getId_user().contains(checkEmail))
////            if (!datas.get(position).getId_user().contains("."))
////                if (datas.get(position).getId_user().contains(facebook)){
////                    GetID getID=new GetID(datas.get(position).getId_user());
////                    profilePictureView.setProfileId(getID.getId());
////                }
//        return rowView;
//    }
//
//}
