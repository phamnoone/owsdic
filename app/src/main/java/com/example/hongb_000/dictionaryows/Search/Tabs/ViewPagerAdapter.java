package com.example.hongb_000.dictionaryows.Search.Tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by hongb_000 on 7/21/2015.
 */
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
            TabSearchWord tabSearchWord = new TabSearchWord();
            return tabSearchWord;
        } else {
            TabSearchKanji tabSearchKanji = new TabSearchKanji();
            return tabSearchKanji;
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
