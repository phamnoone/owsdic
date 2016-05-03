package com.example.hongb_000.dictionaryows.PII;

/**
 * Created by USER on 6/29/2015.
 */
public class Answer {
    private String ans;
    private String id;
    private String id_ques;
    private String id_user;
    private String voteNumber;
    private String mName;
    private String voted;
    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Answer() {
    }
    public Answer(String ans, String id, String id_ques,String id_user,String mName) {
        this.ans = ans;
        this.id = id;
        this.id_ques=id_ques;
        this.id_user=id_user;
        this.mName=mName;
    }
    public Answer(String ans,String id,String id_ques,String id_user,String voteNumber,String mName,String voted){
        this.ans = ans;
        this.id = id;
        this.id_ques=id_ques;
        this.id_user=id_user;
        this.voteNumber=voteNumber;
        this.mName=mName;
        this.voted=voted;

    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {

        this.voted = voted;
    }

    public String getId_user() {
        return id_user;
    }

    public String getId_ques() {

        return id_ques;
    }

    public String getmName() {
        return mName;
    }

    public String getId() {

        return id;
    }

    public String getAns() {

        return ans;
    }

    public String getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(String voteNumber) {
        this.voteNumber = voteNumber;
    }
}
