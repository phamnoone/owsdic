package com.example.hongb_000.dictionaryows.PII;

/**
 * Created by USER on 6/25/2015.
 */
public class Question {
    private String id;
    private String ques;
    private String id_user;
    private String mName;
    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Question() {
    }

    public Question(String id,String id_user, String ques,String mName) {
        this.id = id;
        this.ques= ques;
        this.id_user=id_user;
        this.mName=mName;
    }

    public String getId() {
        return id;
    }

    public String getQues() {
        return ques;
    }

    public String getId_user() {
        return id_user;
    }

    public String getmName() {
        return mName;
    }
}
