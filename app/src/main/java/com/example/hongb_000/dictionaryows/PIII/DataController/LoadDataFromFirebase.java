package com.example.hongb_000.dictionaryows.PIII.DataController;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by phamn on 8/29/2015.
 */
public class LoadDataFromFirebase {
    private static final String FIREBASE_URL = "https://ows-chat.firebaseio.com";
    private Firebase mFirebase;
    GetDataFirebase getDataFirebase;

    public LoadDataFromFirebase(Context context, final GetDataFirebase getDataFirebase) {
        Firebase.setAndroidContext(context);
        mFirebase = new Firebase("https://androiddictionary.firebaseio.com/ListData");
        this.getDataFirebase = getDataFirebase;
        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                getDataFirebase.setOnGetData(new UpdateData(
                        data.get("name").toString(),
                        data.get("link").toString(),
                        data.get("note").toString(),
                        data.get("numberofquetions").toString(),
                        false
                ));
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


}
