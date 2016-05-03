package com.example.hongb_000.dictionaryows.PIII.DataController;

/**
 * Created by phamn on 8/27/2015.
 */
public class HistoryData {
    String date;
    String nValue;
    String numberQ;
    String point;

    public HistoryData(String date, String nValue, String numberQ, String point) {
        this.date = date;
        this.nValue = nValue;
        this.numberQ = numberQ;
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public String getnValue() {
        return nValue;
    }

    public String getNumberQ() {
        return numberQ;
    }

    public String getPoint() {
        return point;
    }
}
