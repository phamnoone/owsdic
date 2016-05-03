package com.example.hongb_000.dictionaryows.PII;

/**
 * Created by USER on 6/25/2015.
 */

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.R;
import com.firebase.client.Query;

public class ListUsersAdapter extends FirebaseListAdapter<User> {


    private String id;
    private String from;
    private String to;


    public ListUsersAdapter (Query ref, Activity activity, int layout) {
        super(ref, User.class, layout, activity);
        this.id= id;
        this.from=from;
        this.to=to;
    }



    @Override
    protected void populateView(View view, User user) {
        String name = user.getName();
        TextView txtid = (TextView) view.findViewById(R.id.id);
        txtid.setText(name);
    }


}
