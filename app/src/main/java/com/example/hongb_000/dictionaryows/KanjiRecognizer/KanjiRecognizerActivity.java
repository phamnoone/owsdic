package com.example.hongb_000.dictionaryows.KanjiRecognizer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.KanjiRecognizer.library.InputStroke;
import com.example.hongb_000.dictionaryows.KanjiRecognizer.library.KanjiInfo;
import com.example.hongb_000.dictionaryows.KanjiRecognizer.library.KanjiList;
import com.example.hongb_000.dictionaryows.KanjiRecognizer.library.KanjiMatch;
import com.example.hongb_000.dictionaryows.MainActivity;
import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.DataBase.DataBase;
import com.example.hongb_000.dictionaryows.Search.DataBase.SearchDBHelper;
import com.example.hongb_000.dictionaryows.Search.DisplayMeanKanjiActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by hongb on 8/26/2015.
 */
public class KanjiRecognizerActivity extends AppCompatActivity{

    private DrawingPanel mDrawingPanel;

    private Button mBTUndo;
    private Button mBTClear;
    private Button mBTRedo;
    private HorizontalListView mLVKanjiRecog;
    private RelativeLayout mLayout;
    private TextView mTVNoteDrawKanji;

    private static final String NOTE = "Vẽ đúng số nét và đúng thứ tự nét để tăng khả năng chính xác";

    private ArrayAdapter<String> adapter;
    private Adapter mAdapter;
    private ArrayList<String> mListKanji;
    private ArrayList<Item> mListItem;

    private Toolbar mToolbar;
    private Cursor mCursorSearchByKanji;
    private SearchDBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kanji_recognizer_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myDB = new SearchDBHelper(this, DataBase.DB_NAME_KANJI, DataBase.DB_TABLE_KANJI);

        try {
            myDB.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            myDB.openDataBase();
        }catch (SQLException e) {
            throw e;
        }


        mListKanji  = new ArrayList<>();
        mListItem = new ArrayList<>();

        mDrawingPanel = new DrawingPanel(this, null);
        mLayout = (RelativeLayout) findViewById(R.id.drawing);
        mTVNoteDrawKanji = (TextView) findViewById(R.id.note_draw_kanji);
        mLayout.addView(mDrawingPanel);

        mBTUndo = (Button) findViewById(R.id.undo);
        mBTClear = (Button) findViewById(R.id.clear);
        mBTRedo = (Button) findViewById(R.id.redo);
        mLVKanjiRecog = (HorizontalListView) findViewById(R.id.list_kanji_recog);

        mBTUndo.setOnClickListener(new OnClickListener());
        mBTClear.setOnClickListener(new OnClickListener());
        mBTRedo.setOnClickListener(new OnClickListener());

        mAdapter = new Adapter(this);
        mLVKanjiRecog.setAdapter(mAdapter);

        mLVKanjiRecog.setOnItemClickListener(new OnItemClickListener());

        mTVNoteDrawKanji.setText(NOTE);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.undo :
                    mDrawingPanel.onClickUndo();
                    break;
                case R.id.clear :
                    setNoteDrawkanji(false);
                    mDrawingPanel.onClickClear();
                    break;
                case R.id.redo :
                    mDrawingPanel.onClickRedo();
                    break;
            }
        }
    }

    public class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mCursorSearchByKanji = myDB.getInfoBySearchKanji(mListItem.get(i).getKanji(), DataBase.DB_COLUMN_KANJI);
            final String kanji = mCursorSearchByKanji.getString(mCursorSearchByKanji.getColumnIndex("kanji"));
            final String hanviet = mCursorSearchByKanji.getString(mCursorSearchByKanji.getColumnIndex("hanviet"));
            final String radical = mCursorSearchByKanji.getString(mCursorSearchByKanji.getColumnIndex("radical"));
            final String onread = mCursorSearchByKanji.getString(mCursorSearchByKanji.getColumnIndex("onread"));
            final String kunread = mCursorSearchByKanji.getString(mCursorSearchByKanji.getColumnIndex("kunread_meaning"));
            final int jplt = mCursorSearchByKanji.getInt(mCursorSearchByKanji.getColumnIndex("jlpt"));

            Intent intent = new Intent(KanjiRecognizerActivity.this, DisplayMeanKanjiActivity.class);
            intent.putExtra("kanji", kanji);
            intent.putExtra("hanviet", hanviet);
            intent.putExtra("radical", radical);
            intent.putExtra("onread", onread);
            intent.putExtra("kunread", kunread);
            intent.putExtra("jplt", jplt);
            startActivity(intent);
        }
    }

    private class Adapter extends ArrayAdapter<Item> {

        public Adapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.kanji_recong_item, null);
            }

            TextView kanjiTextView = (TextView) convertView.findViewById(R.id.kanji_recog_item);
            kanjiTextView.setText(mListItem.get(position).getKanji());
            return convertView;
        }

        @Override
        public int getCount() {
            return mListItem.size();
        }
    }

    private class Item {
        private String kanji;

        public Item(String kanji) {
            this.kanji = kanji;
        }

        public String getKanji() {
            return kanji;
        }

        public void setKanji(String kanji) {
            this.kanji = kanji;
        }
    }





    public class DrawingPanel extends View implements View.OnTouchListener {

        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;
        private ArrayList<Path> paths = new ArrayList<>();
        private ArrayList<Path> undoPaths = new ArrayList<>();
        private Bitmap mBitmap;
        private int color = 0xFFFF0000;

        private ArrayList<InputStroke> mLinkedList = new ArrayList<InputStroke>();
        private ArrayList<InputStroke> undoLinkedList = new ArrayList<InputStroke>();
        private Handler mHandler;
        private KanjiList mKanjiList;


        public DrawingPanel(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
            setFocusableInTouchMode(true);


            try {
                InputStream is = getResources().getAssets().open("strokes-20100823.xml");
                mKanjiList = new KanjiList(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHandler = new Handler();

            this.setOnTouchListener(this);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(8);

            mPath = new Path();
            paths.add(mPath);
            mCanvas = new Canvas();
            paths.clear();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onDraw(Canvas canvas) {

            for (Path path : paths) {
                canvas.drawPath(path, mPaint);
            }

            canvas.drawPath(mPath, mPaint);
        }

        private static final float TOUCH_TOLERANCE = 0;
        private float startX, startY, lastX, lastY;

        private void touch_start(float x, float y) {

            setNoteDrawkanji(true);

            mPath.reset();
            mPath.moveTo(x, y);
            startX = x;
            startY = y;
            lastX = startX;
            lastY = startY;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - lastX);
            float dy = Math.abs(y - lastY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2);
                lastX = x;
                lastY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(lastX, lastY);

            mCanvas.drawPath(mPath, mPaint);

            paths.add(mPath);
            mPath = new Path();

            InputStroke stroke = new InputStroke(startX, startY, lastX, lastY);
            mLinkedList.add(stroke);
            mHandler.kanjiChanged(mLinkedList);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        public void onClickUndo() {
            if (paths.size() > 0) {
                undoPaths.add(paths.remove(paths.size() - 1));

                undoLinkedList.add(mLinkedList.remove(mLinkedList.size() - 1));
                mHandler.kanjiChanged(mLinkedList);
                invalidate();
            }
        }

        public void onClickRedo() {
            if (undoPaths.size() > 0) {
                paths.add(undoPaths.remove(undoPaths.size() - 1));

                mLinkedList.add(undoLinkedList.remove(undoLinkedList.size() - 1));
                mHandler.kanjiChanged(mLinkedList);
                invalidate();
            }
        }

        public void onClickClear() {
            if (paths.size() > 0) {
                paths.clear();

                mLinkedList.clear();
                mHandler.kanjiChanged(mLinkedList);
                invalidate();
            }
        }

        public class Handler {
            public Handler() {
            }

            public void kanjiChanged(ArrayList<InputStroke> strokes) {
                new SearchTask(strokes).execute();
            }
        }

        public class SearchTask extends AsyncTask<Void, Void, Void> {

            private KanjiInfo potentialKanji;

            public SearchTask(ArrayList<InputStroke> strokes) {
                potentialKanji = new KanjiInfo("?");
                for (InputStroke stroke : strokes) {
                    potentialKanji.addStroke(stroke);
                }
                potentialKanji.finish();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                final KanjiMatch[] matches = mKanjiList.getTopMatches(potentialKanji, KanjiInfo.MatchAlgorithm.STRICT, null);

                mListItem.clear();
                for (KanjiMatch match : matches) {

                    Item item = new Item(match.getKanji().getKanji());
                    mListItem.add(item);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                //adapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(KanjiRecognizerActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setNoteDrawkanji (boolean isDrawing) {
        if (!isDrawing) {
            mTVNoteDrawKanji.setText(NOTE);
        } else {
            mTVNoteDrawKanji.setText("");
        }
    }
}
