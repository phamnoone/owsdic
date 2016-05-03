package com.example.hongb_000.dictionaryows.KanjiRecognizer.library;

/**
 * Created by hongb on 8/26/2015.
 */
public class KanjiMatch implements Comparable<KanjiMatch>
{
    private KanjiInfo kanji;
    private float score;

    /**
     * @param kanji Kanji
     * @param score Match score (higher is better)
     */
    KanjiMatch(KanjiInfo kanji, float score)
    {
        this.kanji = kanji;
        this.score = score;
    }

    /**
     * @return Score
     */
    public float getScore()
    {
        return score;
    }

    /**
     * @return Kanji info
     */
    public KanjiInfo getKanji()
    {
        return kanji;
    }

    @Override
    public int compareTo(KanjiMatch o)
    {
        if(score > o.score)
        {
            return -1;
        }
        else if(score < o.score)
        {
            return 1;
        }
        else
        {
            return kanji.getKanji().compareTo(o.kanji.getKanji());
        }
    }
}
