package com.example.hongb_000.dictionaryows.PII;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.R;
import com.firebase.client.Firebase;

import java.util.List;



/**
 * Created by USER on 6/30/2015.
 */
public class AnswerAdapter extends ArrayAdapter<Answer> {
    private static final String FIREBASE_URL = "https://androiddictionary.firebaseio.com";
    private Firebase mFirebaseRef,mFirebaseVote,mFirebaseCheckVote,mFire;
    private Activity myContext;
    private List<Answer> datas;
    private String mfrom;
    private String checkEmail="@";
    private String facebook="facebook:";


    public AnswerAdapter(Context context, int textViewResourceId,
                         List<Answer> objects,String from) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
        mfrom=from;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
//        mFirebaseRef=new Firebase(FIREBASE_URL).child("Answer");
        LayoutInflater inflater = myContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.pii_answer, null);
        TextView txtName=(TextView)rowView.findViewById(R.id.txtNameAnswer);
        txtName.setText(datas.get(position).getmName());
        TextView txtAnswer=(TextView)rowView.findViewById(R.id.txtAnswer);
        txtAnswer.setText(datas.get(position).getAns());
//        ProfilePictureView profilePictureView=(ProfilePictureView)rowView.findViewById(R.id.avatarAnswer);
//        if (!datas.get(position).getId_user().contains(checkEmail))
//            if (!datas.get(position).getId_user().contains("."))
//                if (datas.get(position).getId_user().contains(facebook)){
//                    GetID getID=new GetID(datas.get(position).getId_user());
//                    try {
//                        profilePictureView.setProfileId(getID.getId());
//                    }
//                    catch (OutOfMemoryError e2){
//                        System.out.println(getID.getId());
//
//                    }
//
//                }

        final TextView txtVote=(TextView)rowView.findViewById(R.id.txtVoteAnswer);
        txtVote.setText(datas.get(position).getVoteNumber() + " Thích");


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Xử lí lỗi dừng chương trình bắt buộc/////////////////////////////////////////////////////////////////////////////////////////

        final ImageButton bntVote=(ImageButton) rowView.findViewById(R.id.bntVote);
        if (datas.get(position).getVoted().toString().equals("1")) bntVote.setVisibility(View.GONE);
//        mFirebaseVote=new Firebase(FIREBASE_URL).child("Vote");
//        Query query=mFirebaseVote.orderByChild("id_ans").equalTo(datas.get(position).getId());
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map<String, Objects> value = (Map<String, Objects>) dataSnapshot.getValue();
//                if (mfrom.equals(value.get("id_user"))) {
//                    bntVote.setVisibility(View.GONE);
////                    bntVote.setImageResource(R.drawable.abc_ic_go_search_api_mtrl_alpha);
////                    bntVote.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////
////                        }
////                    });
//                }
////                int vot= Integer.parseInt(txtVote.getText().toString())+1;
////                txtVote.setText(vot+" Vote");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        bntVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                bntVote.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });


                mFire = new Firebase(FIREBASE_URL).child("Vote");
                Vote vote = new Vote(datas.get(position).getId(), mfrom, datas.get(position).getId_ques());
                mFire.push().setValue(vote);

                bntVote.setVisibility(View.GONE);
//                bntVote.setImageResource(R.drawable.abc_ic_go_search_api_mtrl_alpha);
            }
        });





        return rowView;
    }
}
