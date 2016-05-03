package com.example.hongb_000.dictionaryows.KanjiRecognizer.library;

/**
 * Created by hongb on 8/26/2015.
 */
public interface KanjiComparer
{
    /**
     * Initialises the comparer. Should be called immediately after the
     * constructor.
     * @param info Kanji that the user drew
     */
    public void init(KanjiInfo info);

    /**
     * Compares against the given other kanji.
     * @param other Other kanji
     * @return Score in range 0 to 100
     */
    public float getMatchScore(KanjiInfo other);
}
