package com.example.hongb_000.dictionaryows.KanjiRecognizer.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hongb on 8/26/2015.
 */
public abstract class StatsReporter
{
    private static final String baseUrl =
            "http://live.leafdigital.com/kanji/report.jsp";

    /**
     * Interface you can implement if you want to get information about the
     * phone-home process.
     */
    public interface Callback
    {
        /**
         * Called when phone-home is starting.
         */
        public void phoneHomeStart();

        /**
         * Called when phone-home ends.
         * @param ok True if it was successful
         */
        public void phoneHomeEnd(boolean ok);
    }

    /**
     * Phones home with information about the kanji match.
     * @param drawn Drawn kanji (should include the strokes that the user drew;
     *   this is the drawn kanji that you matched against other kanjis)
     * @param kanji Final selected kanji as string
     * @param algo Match algorithm that the user chose the final kanji from
     * @param ranking Ranking of kanji within selected match algorithm
     * @param clientName Client name (name of this client! Can be anything (up
     *   to 255 characters); include version if you like
     * @param callback Optional callback function for information about
     *   phone-home process; null if not required
     */
    public static void phoneHome(KanjiInfo drawn, String kanji, KanjiInfo.MatchAlgorithm algo,
                                 int ranking, String clientName, Callback callback)
    {
        try
        {
            new SendThread(baseUrl, "drawing=" + drawn.getFullSummary() + "&kanji=" + kanji
                    + "&algo=" + algo + "&ranking=" + ranking + "&clientname=" +
                    URLEncoder.encode(clientName, "UTF-8"), callback);
        }
        catch(UnsupportedEncodingException e)
        {
            // Can't happen
            throw new Error(e);
        }
    }

    private static class SendThread extends Thread
    {
        private Callback callback;
        private String url, post;

        private SendThread(String url, String post, Callback callback)
        {
            this.url = url;
            this.post = post;
            this.callback = callback;
            if(callback != null)
            {
                callback.phoneHomeStart();
            }
            start();
        }

        @Override
        public void run()
        {
            boolean ok = false;
            try
            {
                URL u = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)u.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(post.getBytes("UTF-8"));
                outputStream.close();

                InputStream input;
                try
                {
                    input = conn.getInputStream();
                }
                catch(IOException e)
                {
                    input = conn.getErrorStream();
                }
                if(input != null)
                {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(input, "UTF-8"));

                    String firstLine = reader.readLine();
                    if(firstLine == null)
                    {
                        // Empty response
                    }
                    else if(firstLine.equals("OK"))
                    {
                        ok = true;
                    }

                    reader.close();
                }
            }
            catch(IOException e)
            {
            }
            finally
            {
                if(callback != null)
                {
                    callback.phoneHomeEnd(ok);
                }
            }
        }
    }
}
