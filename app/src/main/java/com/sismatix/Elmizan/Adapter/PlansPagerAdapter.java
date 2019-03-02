package com.sismatix.Elmizan.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sismatix.Elmizan.Fregment.Dynamic_Library_freg;

public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return Dynamic_Library_freg.newInstance(position);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}