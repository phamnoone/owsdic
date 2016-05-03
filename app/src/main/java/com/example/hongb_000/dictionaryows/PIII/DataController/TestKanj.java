package com.example.hongb_000.dictionaryows.PIII.DataController;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 15/07/2015.
 */
//CharSequenceArrayList
public class TestKanj implements Parcelable {
    private String Quetion;
    private String[] Anser;
    private int AnsCorect;


    public String[] getAnser() {
        return Anser;
    }

    public int getAnsCorect() {
        return AnsCorect;
    }

    public String getQuetion() {
        return this.Quetion;
    }

    public TestKanj(String quetion, String[] anser, int ansCorect) {
        Quetion = quetion;
        Anser = anser;
        AnsCorect = ansCorect;
    }

    public TestKanj(Parcel source) {
        Quetion = source.readString();
        Anser = source.createStringArray();
        AnsCorect = source.readInt();
    }

    public ArrayList<String> toArray() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(Quetion);
        list.add(Anser[0]);
        list.add(Anser[1]);
        list.add(Anser[2]);
        list.add(Anser[3]);
        list.add(String.valueOf(AnsCorect));
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Quetion);
        dest.writeStringArray(Anser);
        dest.writeInt(AnsCorect);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public TestKanj createFromParcel(Parcel in) {
            return new TestKanj(in);
        }

        public TestKanj[] newArray(int size) {
            return new TestKanj[size];
        }
    };


}
