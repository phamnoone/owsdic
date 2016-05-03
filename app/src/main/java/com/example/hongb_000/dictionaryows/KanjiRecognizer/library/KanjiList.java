package com.example.hongb_000.dictionaryows.KanjiRecognizer.library;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by hongb on 8/26/2015.
 */
public class KanjiList
{
    private SortedMap<Integer, List<KanjiInfo>> kanji =
            new TreeMap<Integer, List<KanjiInfo>>();

    /**
     * Interface that can be used to receive progress information about search.
     */
    public interface Progress
    {
        /**
         * Called each time progress increases (from 0 through to max, inclusive).
         * @param done Amount of progress achieved
         * @param max Maximum at which task will be achieved
         */
        public void progress(int done, int max);
    }

    /**
     * Default constructor (blank list).
     */
    public KanjiList()
    {
    }

    /**
     * SAX handler.
     */
    private class Handler extends DefaultHandler
    {
        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException
        {
            if(qName.equals("kanji") || localName.equals("kanji"))
            {
                // Get unicode
                String id = attributes.getValue("unicode");
                if(id == null)
                {
                    throw new SAXException("<kanji> tag missing unicode=");
                }
                int codePoint;
                try
                {
                    codePoint = Integer.parseInt(id, 16);
                }
                catch(NumberFormatException e)
                {
                    throw new SAXException("<kanji> tag invalid unicode= (" + id + ")");
                }

                // Get strokes summary
                String full = attributes.getValue("strokes");
                if(full == null)
                {
                    throw new SAXException("<kanji> tag missing strokes=");
                }

                // Get kanji as string
                String kanjiString = new String(Character.toChars(codePoint));
                try
                {
                    add(new KanjiInfo(kanjiString, full));
                }
                catch(IllegalArgumentException e)
                {
                    throw new SAXException(e.getMessage());
                }
            }
        }
    }

    /**
     * Construct and load from XML file.
     * @param in Input stream
     * @throws IOException Any error
     */
    public KanjiList(InputStream in) throws IOException
    {
        // Parse data
        SAXParser parser;
        try
        {
            parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(in, new Handler());
            in.close();
        }
        catch(ParserConfigurationException e)
        {
            IOException x = new IOException("Failed to initialise SAX parser");
            x.initCause(e);
            throw x;
        }
        catch(SAXException e)
        {
            IOException x = new IOException("Failed to parse strokes file");
            x.initCause(e);
            throw x;
        }
    }

    /**
     * Adds HistoryActivity kanji to the list.
     * @param info Kanji to add
     */
    public synchronized void add(KanjiInfo info)
    {
        int count = info.getStrokeCount();
        List<KanjiInfo> list = kanji.get(count);
        if(list == null)
        {
            list = new LinkedList<KanjiInfo>();
            kanji.put(count, list);
        }
        list.add(info);
    }

    /**
     * @param strokeCount Stroke count
     * @return All kanji with that stroke count
     */
    public synchronized KanjiInfo[] getKanji(int strokeCount)
    {
        List<KanjiInfo> list = kanji.get(strokeCount);
        if(list == null)
        {
            return new KanjiInfo[0];
        }
        return list.toArray(new KanjiInfo[list.size()]);
    }

    /**
     * @param search Kanji to find
     * @return Info for that kanji
     * @throws IllegalArgumentException If kanji does not exist in list
     */
    public synchronized KanjiInfo find(String search)
    {
        for(List<KanjiInfo> list : kanji.values())
        {
            for(KanjiInfo info : list)
            {
                if(info.getKanji().equals(search))
                {
                    return info;
                }
            }
        }
        throw new IllegalArgumentException("Kanji '" + search + "' not found");
    }

    /**
     * Searches for closest matches.
     * @param compare Kanji to compare
     * @param algo Match algorithm to use
     * @param progress Progress reporter (null if not needed)
     * @return Top matches above search threshold
     * @throws IllegalArgumentException If match algorithm not set
     */
    public KanjiMatch[] getTopMatches(KanjiInfo compare,
                                      KanjiInfo.MatchAlgorithm algo, Progress progress)
            throws IllegalArgumentException
    {
        TreeSet<KanjiMatch> matches = new TreeSet<KanjiMatch>();

        List<KanjiInfo> list = new LinkedList<KanjiInfo>();
        if(compare.getStrokeCount() > 0)
        {
            synchronized(this)
            {
                // Do either -2 and +2, -1 and +1, or just 0
                int range = algo.getOut();
                int count = compare.getStrokeCount() - range;
                for(int i=0; i<2; i++)
                {
                    if(count > 0)
                    {
                        List<KanjiInfo> countList = kanji.get(count);
                        if(countList != null)
                        {
                            list.addAll(countList);
                        }
                    }
                    count += 2 * range;
                    if (range == 0)
                    {
                        break;
                    }
                }
            }
        }
        int max = list.size();
        if(progress != null)
        {
            progress.progress(0, max);
        }
        int i = 0;
        for(KanjiInfo other : list)
        {
            float score = compare.getMatchScore(other, algo);
            KanjiMatch match = new KanjiMatch(other, score);
            matches.add(match);
            if(progress != null)
            {
                progress.progress(++i, max);
            }
        }

        // Pull everything down to half match score
        LinkedList<KanjiMatch> results = new LinkedList<KanjiMatch>();
        float maxScore = -1;
        for(KanjiMatch match : matches)
        {
            if(maxScore == -1)
            {
                maxScore = match.getScore();
            }
            else
            {
                if(match.getScore() < maxScore * 0.75f)
                {
                    break;
                }
            }
            results.add(match);
        }

        return results.toArray(new KanjiMatch[results.size()]);
    }

    /**
     * Saves this list to an XML file.
     * @param out Stream to receive XML data
     * @param originalName Original filename of KanjiVG file
     * @throws IOException Any error
     */
    public void save(OutputStream out, String originalName) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                out, "UTF-8"));
        writer.write("<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<!--\n"
                + "Kanji strokes summary\n"
                + "\n"
                +	"Derived from KanjiVG file '" + originalName + "':\n"
                + "This work is distributed under the conditions of the Creative Commons \n"
                + "Attribution-Share Alike 3.0 Licence. This means you are free:\n"
                + "* to Share - to copy, distribute and transmit the work\n"
                + "* to Remix - to adapt the work\n"
                + "Under the following conditions:\n"
                + "* Attribution. You must attribute the work by stating your use of KanjiVG in\n"
                + "  your own copyright header and linking to KanjiVG's website\n"
                + "  (http://kanjivg.tagaini.net)\n"
                + "* Share Alike. If you alter, transform, or build upon this work, you may\n"
                + "  distribute the resulting work only under the same or similar license to this\n"
                + "  one.\n"
                + "\n"
                + "See http://creativecommons.org/licenses/by-sa/3.0/ for more details.\n"
                + "-->\n"
                + "<strokes>");

        for(List<KanjiInfo> list : kanji.values())
        {
            for(KanjiInfo character : list)
            {
                character.write(writer);
            }
        }

        writer.write("</strokes>");
        writer.close();
    }
}
