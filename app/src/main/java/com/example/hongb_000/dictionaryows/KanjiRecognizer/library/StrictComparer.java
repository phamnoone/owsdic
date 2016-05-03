package com.example.hongb_000.dictionaryows.KanjiRecognizer.library;

/**
 * Created by hongb on 8/26/2015.
 */
public class StrictComparer implements KanjiComparer
{
    private final static float STROKE_DIRECTION_WEIGHT = 1.0f;
    private final static float MOVE_DIRECTION_WEIGHT = 0.8f;
    private final static float STROKE_LOCATION_WEIGHT = 0.6f;

    private final static float CLOSE_WEIGHT = 0.7f;

    private Stroke.Location[] drawnStarts, drawnEnds;
    private Stroke.Direction[] drawnDirections, drawnMoves;

    /**
     * Initialises with given drawn kanji.
     * @param info Drawn kanji
     */
    @Override
    public void init(KanjiInfo info)
    {
        drawnStarts = info.getStrokeStarts();
        drawnEnds = info.getStrokeEnds();
        drawnDirections = info.getStrokeDirections();
        drawnMoves = info.getMoveDirections();
    }

    /**
     * Compares against the given other kanji.
     * @param other Other kanji
     * @return Score in range 0 to 100
     */
    @Override
    public float getMatchScore(KanjiInfo other)
    {
        Stroke.Location[] otherStarts = other.getStrokeStarts(),
                otherEnds = other.getStrokeEnds();
        Stroke.Direction[] otherDirections = other.getStrokeDirections(),
                otherMoves = other.getMoveDirections();

        if(otherStarts.length != drawnStarts.length)
        {
            throw new IllegalArgumentException(
                    "Can only compare with same match length");
        }

        float score = 0;
        for(int i=0; i<drawnStarts.length; i++)
        {
            // Stroke direction
            if(drawnDirections[i] == otherDirections[i])
            {
                score += STROKE_DIRECTION_WEIGHT;
            }
            else if(drawnDirections[i].isClose(otherDirections[i]))
            {
                score += STROKE_DIRECTION_WEIGHT * CLOSE_WEIGHT;
            }

            // Move direction
            if(i>0)
            {
                if(drawnMoves[i-1] == otherMoves[i-1])
                {
                    score += MOVE_DIRECTION_WEIGHT;
                }
                else if(drawnMoves[i-1].isClose(otherMoves[i-1]))
                {
                    score += MOVE_DIRECTION_WEIGHT * CLOSE_WEIGHT;
                }
            }

            // Start and end locations
            if(drawnStarts[i] == otherStarts[i])
            {
                score += STROKE_LOCATION_WEIGHT;
            }
            else if(drawnStarts[i].isClose(otherStarts[i]))
            {
                score += STROKE_LOCATION_WEIGHT * CLOSE_WEIGHT;
            }
            if(drawnEnds[i] == otherEnds[i])
            {
                score += STROKE_LOCATION_WEIGHT;
            }
            else if(drawnEnds[i].isClose(otherEnds[i]))
            {
                score += STROKE_LOCATION_WEIGHT * CLOSE_WEIGHT;
            }
        }

        float max = drawnStarts.length * (STROKE_DIRECTION_WEIGHT
                + 2 * STROKE_LOCATION_WEIGHT)
                +	(drawnStarts.length - 1) * MOVE_DIRECTION_WEIGHT;

        return 100.0f * score / max;
    }
}
