package com.example.hongb_000.dictionaryows.PIII.DataController;

/**
 * Created by phamn on 8/28/2015.
 */
public class UpdateData {
    String name;
    String link;
    String note;
    String numberOfQuetions;
    boolean state;

    public void setState(boolean state) {
        this.state = state;
    }

    public UpdateData(String name, String link, String note, String numberOfQuetions, boolean state) {
        this.name = name;
        this.link = link;
        this.note = note;
        this.numberOfQuetions = numberOfQuetions;
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getNote() {
        return note;
    }

    public String getNumberOfQuetions() {
        return numberOfQuetions;
    }

    public String getFileName() {
        return link.split("/")[link.split("/").length - 1];
    }
}
