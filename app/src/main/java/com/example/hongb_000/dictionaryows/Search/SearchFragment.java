package com.example.hongb_000.dictionaryows.Search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hongb_000.dictionaryows.KanjiRecognizer.KanjiRecognizerActivity;
import com.example.hongb_000.dictionaryows.MainActivity;
import com.example.hongb_000.dictionaryows.PVI.CaptureActivity;
import com.example.hongb_000.dictionaryows.R;
import com.example.hongb_000.dictionaryows.Search.Tabs.SlidingTabLayout;
import com.example.hongb_000.dictionaryows.Search.Tabs.TabSearchKanji;
import com.example.hongb_000.dictionaryows.Search.Tabs.TabSearchWord;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by hongb_000 on 7/20/2015.
 */
public class SearchFragment extends Fragment {

    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private SlidingTabLayout mTabs;
    private CharSequence titles[] = {"Word", "Kanji"};
    private final static int numOfTabs = 2;
    TabSearchWord mTabSearchWord = new TabSearchWord();
    TabSearchKanji mTabSearchKanji = new TabSearchKanji();

    private View mView;

    public static SubActionButton butonCamera, buttonDrawing;
    public static FloatingActionButton actionButtonWord, actionButtonKanji;
    public static FloatingActionMenu actionMenu;
    public static boolean isTabSearchWord = false;

    public SearchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_search, container, false);

            mAdapter = new ViewPagerAdapter(getFragmentManager(), titles, numOfTabs);

            mViewPager = (ViewPager) mView.findViewById(R.id.pager);
            mViewPager.setAdapter(mAdapter);

            mTabs = (SlidingTabLayout) mView.findViewById(R.id.tabs);
            mTabs.setDistributeEvenly(true);

            mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            });


            mTabs.setViewPager(mViewPager);

            //((MainActivity) getActivity()).setActionBarTitle("Tìm Kiếm");
        }

        detachFloatingButton();

        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    isTabSearchWord = true;

                    if (actionMenu != null && actionMenu.isOpen()) {
                        actionMenu.close(true);
                    }

                    if (actionButtonKanji != null) {
                        //actionButtonKanji.setVisibility(View.GONE);
                        actionButtonKanji.detach();
                        actionButtonKanji = null;
                    }

                    if (actionButtonWord == null) {
                        showButtonWord(getActivity());
                    } else {
                        //actionButtonWord.setVisibility(View.VISIBLE);
                    }
                } else if (position == 1) {
                    isTabSearchWord = false;

                    if (actionButtonWord != null) {
                        //actionButtonWord.setVisibility(View.GONE);
                        actionButtonWord.detach();
                        actionButtonWord = null;
                    }


                    if (actionButtonKanji == null) {
                        showButtonKanji(getActivity());
                    } else {
                        //actionButtonKanji.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return mView;
    }

    public void setQuery(String query) {
        if (mTabSearchWord != null) {
            mTabSearchWord.displayListWords(query);
        }

        if (mTabSearchKanji != null) {
            mTabSearchKanji.displayListKanji(query);
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence titles[];
        int numOfTabs;

        public ViewPagerAdapter(FragmentManager fm, CharSequence titles[], int numOfTabs) {
            super(fm);

            this.titles = titles;
            this.numOfTabs = numOfTabs;
        }


        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mTabSearchWord;
            } else {
                return mTabSearchKanji;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }

    public static void showButtonWord(final Activity activity) {
        ImageView icon = new ImageView(activity);
        icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_history_white_36dp));
        actionButtonWord = new FloatingActionButton.Builder(activity)
                .setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_action_red))
                .setContentView(icon)
                .build();

        actionButtonWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.startActivity(new Intent(activity, HistoryActivity.class));
            }
        });
    }

    public static void showButtonKanji (final Activity activity) {
        ImageView icon = new ImageView(activity);
        icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_plus_circle_outline_white_36dp));
        actionButtonKanji = new FloatingActionButton.Builder(activity)
                .setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_action_red))
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
        itemBuilder.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_action_blue));
        ImageView itemIcon1 = new ImageView(activity);
        itemIcon1.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_camera_white_24dp));
        butonCamera = itemBuilder.setContentView(itemIcon1).build();
        butonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, CaptureActivity.class));
            }
        });

        ImageView itemIcon2 = new ImageView(activity);
        itemIcon2.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_brush_white_24dp));
        buttonDrawing = itemBuilder.setContentView(itemIcon2).build();

        actionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(butonCamera)
                .addSubActionView(buttonDrawing)
                .attachTo(actionButtonKanji)
                .build();

        butonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, CaptureActivity.class));
            }
        });

        buttonDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, KanjiRecognizerActivity.class));
            }
        });
    }

    public static void detachFloatingButton() {
        if (actionButtonWord != null) {
            actionButtonWord.detach();
            actionButtonWord = null;
        } else if (actionButtonKanji != null){
            actionButtonKanji.detach();
            actionButtonKanji = null;
        }

        if (actionMenu != null && actionMenu.isOpen()) {
            actionMenu.close(true);
        }
    }

}
