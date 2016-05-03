package com.example.hongb_000.dictionaryows.PIII.PTestActivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.PIII.DataController.TestKanj;
import com.example.hongb_000.dictionaryows.R;

import java.util.ArrayList;

/**
 * Created by phamn on 8/26/2015.
 */
public class MyAdapter extends ArrayAdapter<TestKanj> {
    Context context;
    int layoutId;
    ArrayList<TestKanj> listItem;
    ArrayList<Integer> listAns;

    public MyAdapter(Context context, int layoutId, ArrayList<TestKanj> listItem, ArrayList<Integer> listAns) {
        super(context, layoutId, listItem);
        this.context = context;
        this.layoutId = layoutId;
        this.listItem = listItem;
        this.listAns = listAns;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(layoutId, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.cau);
        ImageView imageView = (ImageView) row.findViewById(R.id.anh);

        textView.setText("Câu " + (position + 1));
        TestKanj testKanj = listItem.get(position);
        int ansCorect = testKanj.getAnsCorect();
        int ans = listAns.get(position);
        if (ans != 0) {
            if (ansCorect == ans)
                imageView.setImageResource(R.mipmap.corect);
            else
                imageView.setImageResource(R.mipmap.pasu);
        }
        return row;

    }
}
