package com.example.hongb_000.dictionaryows.PIV;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 19/07/2015.
 */
public class DataSetting {
    SharedPreferences pre;
    Context context;

    public DataSetting(Context context) {
        this.context = context;
        pre = context.getSharedPreferences("setting", Context.MODE_PRIVATE);

    }

    public void putMaxhistory(int max) {
        SharedPreferences.Editor edit;
        edit = pre.edit();
        edit.putInt("maxhistory", max);
        edit.commit();
    }

    public int getMaxHistory() {
        return pre.getInt("maxhistory",10);
    }


}
