package com.example.hongb_000.dictionaryows.Search.Information;

/**
 * Created by hongb_000 on 7/27/2015.
 */
public class HistoryInfor {
    private String word;
    private String content;

    public HistoryInfor(String word, String content) {
        this.word = word;
        this.content = content;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
