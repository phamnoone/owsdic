package com.example.hongb_000.dictionaryows.PII;

/**
 * Created by USER on 8/2/2015.
 */
public class GetID {
    private String uid;
    private String id="";
    private String check="0123456789";

    public GetID(String uid){
        this.uid=uid;
    }



    public String getId(){
        for (int i=0;i<uid.length();i++){
            if (check.indexOf(uid.charAt(i))!=-1)
                id=id+uid.charAt(i);
        }
        return id;

    }

}
