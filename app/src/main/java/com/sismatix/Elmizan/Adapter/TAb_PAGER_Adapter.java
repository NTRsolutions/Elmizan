package com.sismatix.Elmizan.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sismatix.Elmizan.Fregment.New_dynamic_Library_frag;
import com.sismatix.Elmizan.Model.Librarytablist_model;

import java.util.ArrayList;
import java.util.List;

    public class TAb_PAGER_Adapter extends FragmentPagerAdapter {
        New_dynamic_Library_frag frag;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Librarytablist_model> mFragmentTitleList = new ArrayList<>();
        private ViewPager viewPager;
        boolean visible;

        public TAb_PAGER_Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return New_dynamic_Library_frag.newInstance(
                    mFragmentTitleList.get(position).getCategory_id(), visible,
                    mFragmentTitleList.get(position).getCategory_title());
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, Librarytablist_model title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position).getCategory_title();
        }
    }

