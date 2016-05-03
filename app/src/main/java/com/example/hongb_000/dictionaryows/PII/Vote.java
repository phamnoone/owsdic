package com.example.hongb_000.dictionaryows.PII;

/**
 * Created by USER on 6/29/2015.
 */
public class Vote {
    private String id_user;
    private String id_ans;
    private String id_ques;
    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Vote() {
    }

    public Vote(String id_ans,String id_user,String id_ques) {
        this.id_user=id_user;
        this.id_ans=id_ans;
        this.id_ques=id_ques;
    }

    public String getId_user() {
        return id_user;
    }

    public String getId_ans() {
        return id_ans;
    }

    public String getId_ques() {
        return id_ques;
    }
}
