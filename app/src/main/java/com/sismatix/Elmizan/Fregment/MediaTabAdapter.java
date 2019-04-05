package com.sismatix.Elmizan.Fregment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MediaTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;


    public MediaTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MediaImages tab1 = new MediaImages();
                return tab1;
            case 1:
                MediaVideos tab2 = new MediaVideos();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}